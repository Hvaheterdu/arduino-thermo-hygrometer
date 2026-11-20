package api.arduinothermohygrometer.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

public class PreAuthenticationRateLimitFilter extends OncePerRequestFilter {
  private final RateLimitProperties rateLimitProperties;
  private final ObjectMapper objectMapper;

  private final Cache<String, Bucket> buckets;

  public PreAuthenticationRateLimitFilter(
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
    Limit limit = limitFor(request.getRequestURI());
    if (limit == null) {
      filterChain.doFilter(request, response);
      return;
    }

    String keyPrefix = request.getRequestURI().startsWith("/api/") ? "api:" : "auth:";
    Bucket bucket = computeBucket(keyPrefix + request.getRemoteAddr(), limit);
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
    if (!probe.isConsumed()) {
      writeRateLimitResponse(request, response, probe, limit);
      return;
    }

    filterChain.doFilter(request, response);
  }

  private Limit limitFor(final String path) {
    if ("/auth/token".equals(path)) {
      return new Limit(
          rateLimitProperties.authenticationCapacity(),
          rateLimitProperties.authenticationRefillPeriod());
    }
    if (path.startsWith("/api/")) {
      return new Limit(rateLimitProperties.apiCapacity(), rateLimitProperties.apiRefillPeriod());
    }

    return null;
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

  private void writeRateLimitResponse(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final ConsumptionProbe probe,
      final Limit limit)
      throws IOException {
    long retryAfterSeconds =
        Math.max(1L, Duration.ofNanos(probe.getNanosToWaitForRefill()).toSeconds());
    long resetEpochSeconds =
        Instant.now().plusNanos(probe.getNanosToWaitForReset()).getEpochSecond();

    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
    response.setHeader("X-RateLimit-Limit", String.valueOf(limit.capacity()));
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
