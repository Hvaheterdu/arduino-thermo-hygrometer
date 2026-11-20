package api.arduinothermohygrometer.configuration;

import java.io.IOException;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.filter.PreAuthenticationRateLimitFilter;
import api.arduinothermohygrometer.filter.RateLimitFilter;
import api.arduinothermohygrometer.properties.CorsProperties;
import api.arduinothermohygrometer.properties.RateLimitProperties;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
  private static final Long ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365L;

  private final CorsProperties corsProperties;
  private final ObjectMapper objectMapper;

  public SecurityConfig(final CorsProperties corsProperties, final ObjectMapper objectMapper) {
    this.corsProperties = corsProperties;
    this.objectMapper = objectMapper;
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authenticationException) ->
        writeProblemDetails(
            response,
            buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "unauthorized",
                "Unauthorized.",
                authenticationException.getMessage(),
                request));
  }

  @Bean
  AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) ->
        writeProblemDetails(
            response,
            buildProblemDetail(
                HttpStatus.FORBIDDEN,
                "forbidden",
                "Forbidden.",
                accessDeniedException.getMessage(),
                request));
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(false);
    corsConfiguration.setAllowedHeaders(corsProperties.allowedHeaders());
    corsConfiguration.setAllowedMethods(corsProperties.allowedMethods());
    corsConfiguration.setAllowedOrigins(corsProperties.allowedOrigins());
    corsConfiguration.setMaxAge(Duration.ofSeconds(3600L));

    var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return urlBasedCorsConfigurationSource;
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthoritiesClaimName("roles");
    authoritiesConverter.setAuthorityPrefix("ROLE_");

    var converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

    return converter;
  }

  @Bean
  PreAuthenticationRateLimitFilter preAuthenticationRateLimitingFilter(
      final ObjectMapper objectMapper, final RateLimitProperties rateLimitProperties) {
    return new PreAuthenticationRateLimitFilter(objectMapper, rateLimitProperties);
  }

  @Bean
  RateLimitFilter rateLimitingFilter(
      final ObjectMapper objectMapper, final RateLimitProperties rateLimitProperties) {
    return new RateLimitFilter(objectMapper, rateLimitProperties);
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      final HttpSecurity httpSecurity,
      final AuthenticationEntryPoint authenticationEntryPoint,
      final AccessDeniedHandler accessDeniedHandler,
      final JwtAuthenticationConverter jwtAuthenticationConverter,
      final PreAuthenticationRateLimitFilter preAuthenticationRateLimitFilter,
      final RateLimitFilter rateLimitFilter) {
    return httpSecurity
        .authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(HttpMethod.POST, "/auth/token")
                    .permitAll()
                    .requestMatchers(
                        "/actuator/health",
                        "/actuator/health/liveness",
                        "/actuator/health/readiness")
                    .permitAll()
                    .requestMatchers(
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers("/actuator/**")
                    .hasRole("ACTUATOR")
                    .requestMatchers("/api/**")
                    .hasRole("API_ADMIN")
                    .anyRequest()
                    .denyAll())
        .cors(
            httpSecurityCorsConfigurer ->
                httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(
            headersConfigurer ->
                headersConfigurer
                    .contentTypeOptions(withDefaults())
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                    .cacheControl(withDefaults())
                    .referrerPolicy(
                        referrerPolicyConfig ->
                            referrerPolicyConfig.policy(ReferrerPolicy.NO_REFERRER))
                    .httpStrictTransportSecurity(
                        hstsConfig -> {
                          hstsConfig.includeSubDomains(true);
                          hstsConfig.maxAgeInSeconds(ONE_YEAR_IN_SECONDS);
                        }))
        .exceptionHandling(
            httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
        .oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
        .addFilterBefore(preAuthenticationRateLimitFilter, BearerTokenAuthenticationFilter.class)
        .addFilterAfter(rateLimitFilter, BearerTokenAuthenticationFilter.class)
        .build();
  }

  private void writeProblemDetails(final HttpServletResponse response, final ProblemDetailsDto body)
      throws IOException {
    response.setStatus(body.getStatus());
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
