package api.arduinothermohygrometer.filter;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;

public class ApiKeyFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final SecurityProperties securityProperties;

    public ApiKeyFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(securityProperties.apiHeaderName());
        if (apiKey == null || apiKey.isBlank()) {
            writeProblemDetails(HttpStatus.UNAUTHORIZED,
                "unauthorized",
                "Unauthorized.",
                "Missing API key.",
                request,
                response
            );
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(null, apiKey);
            Authentication authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException ex) {
            writeProblemDetails(HttpStatus.FORBIDDEN,
                "forbidden",
                "Forbidden.",
                "Invalid API key",
                request,
                response
            );
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeProblemDetails(HttpStatus httpStatus, String type, String title, String detail, HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        ProblemDetailsDto body = buildProblemDetail(httpStatus, type, title, detail, request);

        response.setStatus(httpStatus.value());
        response.setContentType("application/problem+json");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
