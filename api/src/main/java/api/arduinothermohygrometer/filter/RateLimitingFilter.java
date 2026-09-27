package api.arduinothermohygrometer.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.properties.RateLimitProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;

public class RateLimitingFilter extends OncePerRequestFilter {
  private final ObjectMapper objectMapper;
  private final SecurityProperties securityProperties;
  private final RateLimitProperties rateLimitProperties;

  private final Cache<String, Bucket> buckets;

  public RateLimitingFilter(
      final ObjectMapper objectMapper,
      final SecurityProperties securityProperties,
      final RateLimitProperties rateLimitProperties) {
    this.objectMapper = objectMapper;
    this.securityProperties = securityProperties;
    this.rateLimitProperties = rateLimitProperties;
    this.buckets =
        Caffeine.newBuilder()
            .maximumSize(rateLimitProperties.maximumBuckets())
            .expireAfterAccess(rateLimitProperties.bucketExpiration())
            .build();
  }

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    if (!path.startsWith("/api/")) {
      filterChain.doFilter(request, response);
      return;
    }

    String apiKey = request.getHeader(securityProperties.apiHeaderName());
    if (apiKey == null || apiKey.isBlank()) {
      apiKey = request.getRemoteAddr();
    }

    Bucket bucket = computeBucket(apiKey);
    ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
    if (!consumptionProbe.isConsumed()) {
      buildRateLimitProblemDetails(request, response, consumptionProbe);
      return;
    }

    long secondsToReset =
        LocalDateTime.now(ZoneId.systemDefault())
            .plusNanos(consumptionProbe.getNanosToWaitForReset())
            .atZone(ZoneId.systemDefault())
            .toEpochSecond();

    response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimitProperties.capacity()));
    response.setHeader(
        "X-RateLimit-Remaining", String.valueOf(consumptionProbe.getRemainingTokens()));
    response.setHeader("X-RateLimit-Reset", String.valueOf(secondsToReset));

    filterChain.doFilter(request, response);
  }

  private Bucket computeBucket(final String key) {
    return buckets.get(
        key,
        _ ->
            Bucket.builder()
                .addLimit(
                    limit ->
                        limit
                            .capacity(rateLimitProperties.capacity())
                            .refillGreedy(
                                rateLimitProperties.capacity(), rateLimitProperties.refillPeriod()))
                .build());
  }

  private void buildRateLimitProblemDetails(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final ConsumptionProbe consumptionProbe)
      throws IOException {
    long retryAfterSeconds =
        Duration.ofNanos(consumptionProbe.getNanosToWaitForRefill()).toSeconds();

    long resetEpochSeconds =
        LocalDateTime.now(ZoneId.systemDefault())
            .plusNanos(consumptionProbe.getNanosToWaitForReset())
            .atZone(ZoneId.systemDefault())
            .toEpochSecond();

    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
    response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimitProperties.capacity()));
    response.setHeader("X-RateLimit-Remaining", "0");
    response.setHeader("X-RateLimit-Reset", String.valueOf(resetEpochSeconds));

    ProblemDetailsDto body =
        buildProblemDetail(
            HttpStatus.TOO_MANY_REQUESTS,
            "rate-limit",
            "Too Many Requests.",
            "Rate limit exceeded. Try again later.",
            request);

    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
