package api.arduinothermohygrometer.filter;

import java.io.IOException;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import api.arduinothermohygrometer.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private static final String INVALID_API_KEY = "Invalid API-KEY.";
    private static final List<String> EXCLUDED_URLS = List.of(
        "/actuator/health",
        "/actuator/health/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs",
        "/v3/api-docs/**"
    );

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final SecurityProperties securityProperties;

    public ApiKeyFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return EXCLUDED_URLS.stream().anyMatch((pattern -> antPathMatcher.match(pattern, requestURI)));
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final @NonNull HttpServletResponse response, final @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        final String validApiKey = securityProperties.apiKey();
        final String requestApiKey = request.getHeader(securityProperties.apiHeader());
        if (requestApiKey == null || !requestApiKey.equals(validApiKey)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, INVALID_API_KEY);
            return;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            "api-key-user",
            null,
            List.of(new SimpleGrantedAuthority(securityProperties.apiRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }
}
