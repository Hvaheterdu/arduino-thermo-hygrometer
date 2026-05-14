package api.arduinothermohygrometer.provider;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.properties.SecurityProperties;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private final SecurityProperties securityProperties;

    public ApiKeyAuthenticationProvider(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String apiKey = Objects.requireNonNull(authentication.getCredentials()).toString();
        if (!securityProperties.apiKey().equals(apiKey)) {
            throw new BadCredentialsException("Invalid API key");
        }

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = securityProperties.apiRoles()
                                                                                  .stream()
                                                                                  .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                                                                  .toList();

        return new UsernamePasswordAuthenticationToken(
            "api-client",
            apiKey,
            simpleGrantedAuthorities
        );
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}