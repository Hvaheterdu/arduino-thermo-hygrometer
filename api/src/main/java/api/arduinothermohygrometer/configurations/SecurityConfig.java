package api.arduinothermohygrometer.configurations;

import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.headers(header -> header
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "connect-src 'none'; default-src 'none'; frame-ancestors 'none'; img-src 'none'; script-src 'none'; style-src 'none';"))
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.NO_REFERRER)));

        httpSecurity.securityMatcher(EndpointRequest.toAnyEndpoint());
        httpSecurity.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return httpSecurity.build();
    }
}
