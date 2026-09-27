package api.arduinothermohygrometer.filter;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import api.arduinothermohygrometer.converter.ApiKeyAuthenticationConverter;
import api.arduinothermohygrometer.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiKeyFilter extends AuthenticationFilter {
  public ApiKeyFilter(
      final AuthenticationManager authenticationManager,
      final SecurityProperties securityProperties,
      final AuthenticationEntryPoint authenticationEntryPoint) {
    super(authenticationManager, new ApiKeyAuthenticationConverter(securityProperties));

    setRequestMatcher(request -> request.getRequestURI().startsWith("/api/"));
    setSuccessHandler(new ContinueChainAuthenticationSuccessHandler());
    setFailureHandler(authenticationFailureHandler(authenticationEntryPoint));
  }

  private static AuthenticationFailureHandler authenticationFailureHandler(
      final AuthenticationEntryPoint authenticationEntryPoint) {
    return authenticationEntryPoint::commence;
  }

  private static final class ContinueChainAuthenticationSuccessHandler
      implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
        final @NonNull HttpServletRequest request,
        final @NonNull HttpServletResponse response,
        final @NonNull Authentication authentication)
        throws IOException, ServletException {}

    @Override
    public void onAuthenticationSuccess(
        final @NonNull HttpServletRequest request,
        final @NonNull HttpServletResponse response,
        final @NonNull FilterChain chain,
        final @NonNull Authentication authentication)
        throws IOException, ServletException {
      chain.doFilter(request, response);
    }
  }
}
