package api.arduinothermohygrometer.configurations;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Long ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365L;

    private final List<String> allowedOrigins;

    public SecurityConfig(@Value("${cors.allowedOrigins}") List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Bean
    protected UrlBasedCorsConfigurationSource corsFilter() {
        var source = new UrlBasedCorsConfigurationSource();
        var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        allowedOrigins.forEach(config::addAllowedOrigin);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);

        return source;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsFilter()))
            .headers(headers ->
                headers.contentSecurityPolicy(contentSecurityPolicyConfig ->
                           contentSecurityPolicyConfig.policyDirectives("connect-src 'none'; default-src 'none'; frame-ancestors 'none'; img-src 'none'; script-src 'none'; style-src 'none';"))
                       .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.NO_REFERRER))
                       .httpStrictTransportSecurity(hstsConfig -> {
                           hstsConfig.includeSubDomains(true);
                           hstsConfig.maxAgeInSeconds(ONE_YEAR_IN_SECONDS);
                       }))
            .build();
    }
}
