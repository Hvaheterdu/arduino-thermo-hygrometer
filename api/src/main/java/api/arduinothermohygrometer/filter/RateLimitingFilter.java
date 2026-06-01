package api.arduinothermohygrometer.filter;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.properties.SecurityProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class RateLimitingFilter extends OncePerRequestFilter {
    private static final Duration DURATION = Duration.ofSeconds(600);
    private static final long TOKENS = 100;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;
    private final SecurityProperties securityProperties;

    public RateLimitingFilter(final ObjectMapper objectMapper, final SecurityProperties securityProperties) {
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(securityProperties.apiHeaderName());
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = request.getRemoteAddr();
        }

        Bucket bucket = computeBuckets(apiKey);
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
        if (!consumptionProbe.isConsumed()) {
            buildRateLimitProblemDetails(request, response, consumptionProbe);
            return;
        }

        long secondsToReset = LocalDateTime.now()
                .plusNanos(consumptionProbe.getNanosToWaitForReset())
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
        response.setHeader("X-RateLimit-Limit", String.valueOf(TOKENS));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(consumptionProbe.getRemainingTokens()));
        response.setHeader("X-RateLimit-Reset", String.valueOf(secondsToReset));

        filterChain.doFilter(request, response);
    }

    private void buildRateLimitProblemDetails(final HttpServletRequest request, final HttpServletResponse response,
            final ConsumptionProbe consumptionProbe) throws IOException {
        long retryAfterSeconds = Duration.ofNanos(consumptionProbe.getNanosToWaitForRefill()).toSeconds();
        long resetEpochSeconds = LocalDateTime.now()
                .plusNanos(consumptionProbe.getNanosToWaitForReset())
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
        response.setHeader("X-RateLimit-Limit", String.valueOf(TOKENS));
        response.setHeader("X-RateLimit-Remaining", "0");
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetEpochSeconds));

        ProblemDetailsDto body = buildProblemDetail(
                HttpStatus.TOO_MANY_REQUESTS,
                "rate-limit",
                "Too Many Requests.",
                "Rate limit exceeded. Try again later.",
                request);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    private Bucket computeBuckets(final String apiKey) {
        return buckets.computeIfAbsent(apiKey,
                id -> Bucket.builder()
                        .addLimit(limit -> limit.capacity(TOKENS).refillGreedy(TOKENS, DURATION))
                        .build());
    }
}
