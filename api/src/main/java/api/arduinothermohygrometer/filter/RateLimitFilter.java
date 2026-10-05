package api.arduinothermohygrometer.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.model.Limit;
import api.arduinothermohygrometer.properties.RateLimitProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;

public class RateLimitFilter extends OncePerRequestFilter {
  private final ObjectMapper objectMapper;
  private final RateLimitProperties rateLimitProperties;
  private final Cache<String, Bucket> buckets;

  public RateLimitFilter(
      final ObjectMapper objectMapper, final RateLimitProperties rateLimitProperties) {
    this.objectMapper = objectMapper;
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
    Limit limit = limitFor(path);
    if (limit == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String key = rateLimitKey(request);
    Bucket bucket = computeBucket(key, limit);
    ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
    if (!consumptionProbe.isConsumed()) {
      buildRateLimitProblemDetails(request, response, consumptionProbe, limit);
      return;
    }

    addRateLimitHeaders(response, consumptionProbe, limit);
    filterChain.doFilter(request, response);
  }

  private Limit limitFor(final String path) {
    if (path.startsWith("/api/")) {
      return new Limit(rateLimitProperties.apiCapacity(), rateLimitProperties.apiRefillPeriod());
    }

    return null;
  }

  private String rateLimitKey(final HttpServletRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return "principal:" + sha512(authentication.getName());
    }

    return "ip:" + request.getRemoteAddr();
  }

  private static String sha512(final String value) {
    try {
      byte[] digest =
          MessageDigest.getInstance("SHA-512").digest(value.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-512 is not available", exception);
    }
  }

  private Bucket computeBucket(final String key, final Limit limit) {
    return buckets.get(
        key,
        _ ->
            Bucket.builder()
                .addLimit(
                    bandwidth ->
                        bandwidth
                            .capacity(limit.capacity())
                            .refillGreedy(limit.capacity(), limit.refillPeriod()))
                .build());
  }

  private void addRateLimitHeaders(
      final HttpServletResponse response, final ConsumptionProbe probe, final Limit limit) {
    long resetEpochSeconds =
        Instant.now().plusNanos(probe.getNanosToWaitForReset()).getEpochSecond();
    response.setHeader("X-RateLimit-Limit", String.valueOf(limit.capacity()));
    response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
    response.setHeader("X-RateLimit-Reset", String.valueOf(resetEpochSeconds));
  }

  private void buildRateLimitProblemDetails(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final ConsumptionProbe consumptionProbe,
      final Limit limit)
      throws IOException {
    long retryAfterSeconds =
        Math.max(1L, Duration.ofNanos(consumptionProbe.getNanosToWaitForRefill()).toSeconds());

    addRateLimitHeaders(response, consumptionProbe, limit);
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));

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
