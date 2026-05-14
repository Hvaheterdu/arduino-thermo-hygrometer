package api.arduinothermohygrometer.exception;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import static api.arduinothermohygrometer.util.ProblemDetailUtil.buildProblemDetail;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders httpHeaders,
        @NonNull HttpStatusCode httpStatusCode,
        @NonNull WebRequest webRequest) {
        log.error("Method argument not valid exception with message={}.", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatusCode, "One or more fields are invalid.");
        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/validation-error"));
        problemDetail.setTitle("Entity validation error.");
        problemDetail.setInstance(URI.create(webRequest.getContextPath()));
        String traceId = MDC.get("traceId");
        if (traceId != null) {
            problemDetail.setProperty("traceId", traceId);
        }
        problemDetail.setProperty("timestamp", Instant.now());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
              errors.put(error.getField(), error.getDefaultMessage())
          );
        problemDetail.setProperty("errors", errors);

        return createResponseEntity(problemDetail, httpHeaders, httpStatusCode, webRequest);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found exception with message={}.", ex.getMessage());
        return buildProblemDetail(HttpStatus.NOT_FOUND, "resource-not-found", "Resource not found.", ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotCreatedException.class)
    public ProblemDetail handleResourceNotCreated(ResourceNotCreatedException ex, HttpServletRequest request) {
        log.error("Resource not created exception with message={}.", ex.getMessage());
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "resource-not-created", "Resource not created.", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Internal server error exception with message={}.", ex.getMessage());
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "internal-error", "Internal server error.", ex.getMessage(), request);
    }
}
