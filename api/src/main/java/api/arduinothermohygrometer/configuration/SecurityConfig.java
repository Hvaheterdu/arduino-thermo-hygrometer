package api.arduinothermohygrometer.configuration;

import java.io.IOException;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.filter.ApiKeyFilter;
import api.arduinothermohygrometer.filter.RateLimitingFilter;
import api.arduinothermohygrometer.properties.CorsProperties;
import api.arduinothermohygrometer.properties.RateLimitProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;
import api.arduinothermohygrometer.provider.ApiKeyAuthenticationProvider;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
  private static final Long ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365L;

  private final CorsProperties corsProperties;
  private final ObjectMapper objectMapper;
  private final SecurityProperties securityProperties;

  public SecurityConfig(
      final CorsProperties corsProperties,
      final ObjectMapper objectMapper,
      final SecurityProperties securityProperties) {
    this.corsProperties = corsProperties;
    this.objectMapper = objectMapper;
    this.securityProperties = securityProperties;
  }

  @Bean
  ApiKeyFilter apiKeyFilter(
      final AuthenticationManager authenticationManager,
      final AuthenticationEntryPoint authenticationEntryPoint) {
    return new ApiKeyFilter(authenticationManager, securityProperties, authenticationEntryPoint);
  }

  @Bean
  AuthenticationManager authenticationManager(
      final ApiKeyAuthenticationProvider apiKeyAuthenticationProvider) {
    return new ProviderManager(apiKeyAuthenticationProvider);
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
  RateLimitingFilter rateLimitingFilter(final RateLimitProperties rateLimitProperties) {
    return new RateLimitingFilter(objectMapper, securityProperties, rateLimitProperties);
  }

  @Bean
  SecurityFilterChain securityFilterChain(
      final HttpSecurity httpSecurity,
      final ApiKeyFilter apiKeyFilter,
      final RateLimitingFilter rateLimitingFilter,
      final AuthenticationEntryPoint authenticationEntryPoint,
      final AccessDeniedHandler accessDeniedHandler) {
    return httpSecurity
        .authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
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
        .addFilterBefore(rateLimitingFilter, ApiKeyFilter.class)
        .addFilterAfter(apiKeyFilter, LogoutFilter.class)
        .build();
  }

  private void writeProblemDetails(final HttpServletResponse response, final ProblemDetailsDto body)
      throws IOException {
    response.setStatus(body.getStatus());
    response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
