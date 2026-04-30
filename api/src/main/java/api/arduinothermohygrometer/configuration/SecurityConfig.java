package api.arduinothermohygrometer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private static final Long ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365L;

    private final ApiKeyFilter apiKeyFilter;
    private final CorsProperties corsProperties;

    public SecurityConfig(ApiKeyFilter apiKeyFilter, CorsProperties corsProperties) {
        this.apiKeyFilter = apiKeyFilter;
        this.corsProperties = corsProperties;
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
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers("/actuator/**").authenticated()
                                                          .requestMatchers("/api/**").authenticated()
                                                          .requestMatchers("/swagger-ui/**").permitAll()
                                                          .requestMatchers("/v3/api-docs/**").permitAll()
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
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
