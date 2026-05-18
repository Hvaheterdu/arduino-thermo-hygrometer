package api.arduinothermohygrometer.configuration;

import java.io.IOException;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.filter.ApiKeyFilter;
import api.arduinothermohygrometer.filter.RateLimitingFilter;
import api.arduinothermohygrometer.properties.CorsProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;
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

    public SecurityConfig(CorsProperties corsProperties, ObjectMapper objectMapper, SecurityProperties securityProperties) {
        this.corsProperties = corsProperties;
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    @Bean
    public ApiKeyFilter apiKeyFilter(AuthenticationManager authenticationManager) {
        return new ApiKeyFilter(authenticationManager, objectMapper, securityProperties);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setAllowedHeaders(corsProperties.allowedHeaders());
        corsConfiguration.setAllowedMethods(corsProperties.allowedMethods());
        corsConfiguration.setAllowedOrigins(corsProperties.allowedOrigins());
        corsConfiguration.setMaxAge(Duration.ofSeconds(3600L));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public RateLimitingFilter rateLimitingFilter() {
        return new RateLimitingFilter(objectMapper, securityProperties);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ApiKeyFilter apiKeyFilter, RateLimitingFilter rateLimitingFilter) {
        return httpSecurity
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers("/actuator/health",
                                                              "/actuator/health/liveness", "/actuator/health/readiness").permitAll()
                                                          .requestMatchers("/swagger-ui.html",
                                                              "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                                                          .requestMatchers("/actuator/**").hasRole("ACTUATOR")
                                                          .requestMatchers("/api/**").hasRole("API_ADMIN")
                                                          .anyRequest().denyAll()
            )
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .headers(headersConfigurer ->
                headersConfigurer.contentTypeOptions(withDefaults())
                                 .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                                 .cacheControl(withDefaults())
                                 .contentSecurityPolicy(contentSecurityPolicyConfig ->
                                     contentSecurityPolicyConfig.policyDirectives("connect-src 'self'; "
                                         + "default-src 'self'; "
                                         + "frame-ancestors 'none'; "
                                         + "img-src 'self' data:; "
                                         + "script-src 'self'; "
                                         + "style-src 'self' 'unsafe-inline';")
                                 )
                                 .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicy.NO_REFERRER))
                                 .httpStrictTransportSecurity(hstsConfig -> {
                                     hstsConfig.includeSubDomains(true);
                                     hstsConfig.maxAgeInSeconds(ONE_YEAR_IN_SECONDS);
                                 })
            )
            .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer
                    .authenticationEntryPoint((request, response, authenticationException) -> {
                        ProblemDetailsDto body = buildProblemDetail(
                            HttpStatus.UNAUTHORIZED,
                            "unauthorized",
                            "Unauthorized.",
                            authenticationException.getMessage(),
                            request
                        );
                        writeProblemDetails(response, body);
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        ProblemDetailsDto body = buildProblemDetail(
                            HttpStatus.FORBIDDEN,
                            "forbidden",
                            "Forbidden.",
                            accessDeniedException.getMessage(),
                            request
                        );
                        writeProblemDetails(response, body);
                    })
            )
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(rateLimitingFilter, ApiKeyFilter.class)
            .build();
    }

    private void writeProblemDetails(HttpServletResponse response, ProblemDetailsDto body) throws IOException {
        response.setStatus(body.getStatus());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
