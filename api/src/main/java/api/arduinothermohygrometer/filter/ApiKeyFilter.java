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

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final SecurityProperties securityProperties;

    public ApiKeyFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final @NonNull HttpServletResponse response, final @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        boolean whitelisted = securityProperties.whitelist()
                                                .stream()
                                                .anyMatch(pattern -> antPathMatcher.match(pattern, requestURI));
        if (whitelisted) {
            filterChain.doFilter(request, response);
            return;
        }

        final String apiKey = securityProperties.apiKey();
        final String requestApiKey = request.getHeader(securityProperties.apiHeader());
        if (apiKey == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, INVALID_API_KEY);
            return;
        }

        if (requestApiKey == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, INVALID_API_KEY);
            return;
        }

        if (!requestApiKey.equals(apiKey)) {
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
