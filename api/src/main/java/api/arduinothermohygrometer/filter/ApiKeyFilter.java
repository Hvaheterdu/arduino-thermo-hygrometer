package api.arduinothermohygrometer.filter;

import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    public ApiKeyFilter(final AuthenticationManager authenticationManager,
                        final ObjectMapper objectMapper,
                        final SecurityProperties securityProperties) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(securityProperties.apiHeaderName());
        if (apiKey == null || apiKey.isBlank()) {
            writeProblemDetails(HttpStatus.UNAUTHORIZED, "unauthorized", "Unauthorized.", "Missing API key.", request, response);
            return;
        }

        try {
            var authRequest = new UsernamePasswordAuthenticationToken(null, apiKey);
            Authentication authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (final AuthenticationException _) {
            writeProblemDetails(HttpStatus.FORBIDDEN, "forbidden", "Forbidden.", "Invalid API key", request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeProblemDetails(final HttpStatus httpStatus,
                                     final String type,
                                     final String title,
                                     final String detail,
                                     final HttpServletRequest request,
                                     final HttpServletResponse response) throws IOException {
        ProblemDetailsDto body = buildProblemDetail(httpStatus, type, title, detail, request);
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
