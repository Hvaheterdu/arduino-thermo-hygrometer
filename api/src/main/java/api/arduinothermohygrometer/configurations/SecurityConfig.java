package api.arduinothermohygrometer.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Long ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365L;

    private final List<String> allowedOrigins;

    public SecurityConfig(@Value("${cors.allowedOrigins}") List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        allowedOrigins.forEach(corsConfiguration::addAllowedOrigin);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers("/actuator/**").permitAll()
                                                          .requestMatchers("/v3/api-docs/**").permitAll()
                                                          .requestMatchers("/swagger-ui/**").permitAll()
                                                          .requestMatchers("/api/**").permitAll()
                                                          .anyRequest()
                                                          .denyAll()
            )
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
            .build();
    }
}
