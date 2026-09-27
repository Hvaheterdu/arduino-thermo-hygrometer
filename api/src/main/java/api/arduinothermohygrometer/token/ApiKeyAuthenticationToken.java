package api.arduinothermohygrometer.token;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public final class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
  private final String apiKey;

  private ApiKeyAuthenticationToken(
      final String apiKey, final Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.apiKey = apiKey;
  }

  public static ApiKeyAuthenticationToken unauthenticated(final String apiKey) {
    return new ApiKeyAuthenticationToken(apiKey, List.of());
  }

  public static ApiKeyAuthenticationToken authenticated(
      final Collection<? extends GrantedAuthority> authorities) {
    var token = new ApiKeyAuthenticationToken(null, authorities);
    token.setAuthenticated(true);
    return token;
  }

  @Override
  public Object getCredentials() {
    return apiKey;
  }

  @Override
  public Object getPrincipal() {
    return "api-client";
  }
}
