package api.arduinothermohygrometer.configurations;

import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.securityMatcher(EndpointRequest.toAnyEndpoint());
        httpSecurity.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

        return httpSecurity.build();
    }
}
