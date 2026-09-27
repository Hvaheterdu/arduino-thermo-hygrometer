package api.arduinothermohygrometer.converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import api.arduinothermohygrometer.properties.SecurityProperties;
import api.arduinothermohygrometer.token.ApiKeyAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;

public final class ApiKeyAuthenticationConverter implements AuthenticationConverter {
  private final SecurityProperties securityProperties;

  public ApiKeyAuthenticationConverter(final SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Override
  public Authentication convert(final HttpServletRequest request) {
    return ApiKeyAuthenticationToken.unauthenticated(
        request.getHeader(securityProperties.apiHeaderName()));
  }
}
