package api.arduinothermohygrometer.configuration;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import api.arduinothermohygrometer.filter.ApiKeyFilter;
import api.arduinothermohygrometer.properties.CorsProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private static final Long ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365L;

    private final CorsProperties corsProperties;
    private final SecurityProperties securityProperties;

    public SecurityConfig(CorsProperties corsProperties, SecurityProperties securityProperties) {
        this.corsProperties = corsProperties;
        this.securityProperties = securityProperties;
    }

    @Bean
    public ApiKeyFilter apiKeyFilter(AuthenticationManager authenticationManager) {
        return new ApiKeyFilter(authenticationManager, securityProperties);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(corsProperties.allowedHeaders());
        corsConfiguration.setAllowedMethods(corsProperties.allowedMethods());
        corsConfiguration.setAllowedOrigins(corsProperties.allowedOrigins());

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ApiKeyFilter apiKeyFilter) {
        return httpSecurity
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers("/actuator/health").permitAll()
                                                          .requestMatchers("/actuator/health/liveness").permitAll()
                                                          .requestMatchers("/actuator/health/readiness").permitAll()
                                                          .requestMatchers("/swagger-ui.html",
                                                              "/swagger-ui/**",
                                                              "/v3/api-docs",
                                                              "/v3/api-docs/**"
                                                          ).permitAll()
                                                          .requestMatchers("/actuator/**").hasRole("ACTUATOR")
                                                          .requestMatchers("/api/**").hasRole("API_ADMIN")
                                                          .anyRequest().denyAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
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
                        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, authenticationException.getMessage());
                        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/unauthorized"));
                        problemDetail.setTitle("Unauthorized.");
                        problemDetail.setInstance(URI.create(request.getRequestURI()));
                        problemDetail.setProperty("timestamp", Instant.now());

                        writeProblemDetails(response, problemDetail);
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, accessDeniedException.getMessage());
                        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/forbidden"));
                        problemDetail.setTitle("Forbidden.");
                        problemDetail.setInstance(URI.create(request.getRequestURI()));
                        problemDetail.setProperty("timestamp", Instant.now());

                        writeProblemDetails(response, problemDetail);
                    })
            )
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    private void writeProblemDetails(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
        response.setStatus(problemDetail.getStatus());
        response.setContentType("application/problem+json");
        response.getWriter().write(problemDetail.toString());
    }
}
