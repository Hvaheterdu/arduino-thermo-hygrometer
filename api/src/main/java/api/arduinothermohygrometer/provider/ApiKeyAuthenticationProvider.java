package api.arduinothermohygrometer.provider;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.exception.MissingApiKeyException;
import api.arduinothermohygrometer.properties.SecurityProperties;
import api.arduinothermohygrometer.token.ApiKeyAuthenticationToken;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
  private final SecurityProperties securityProperties;

  public ApiKeyAuthenticationProvider(final SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Override
  public Authentication authenticate(final Authentication authentication) {
    String apiKey = (String) authentication.getCredentials();
    if (apiKey == null || apiKey.isBlank()) {
      throw new MissingApiKeyException();
    }

    if (!securityProperties.apiKey().equals(apiKey)) {
      throw new BadCredentialsException("Invalid API key");
    }

    List<SimpleGrantedAuthority> authorities =
        securityProperties.apiRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .toList();

    return ApiKeyAuthenticationToken.authenticated(authorities);
  }

  @Override
  public boolean supports(@NonNull final Class<?> authentication) {
    return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
