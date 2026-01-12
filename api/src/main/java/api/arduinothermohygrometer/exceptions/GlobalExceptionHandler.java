package api.arduinothermohygrometer.exceptions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String BASE_URL = "https://api.arduinothermohygrometer/errors/";

    @Override
    protected ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        @NonNull HttpHeaders httpHeaders,
        @NonNull HttpStatusCode httpStatusCode,
        @NonNull WebRequest webRequest) {
        LOG.error("Method argument not valid exception with message={}.", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatusCode, "One or more fields are invalid.");
        problemDetail.setType(URI.create(BASE_URL + "validation-error"));
        problemDetail.setTitle("Model validation error.");

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
        LOG.error("Resource not found exception with message={}.", ex.getMessage());

        return buildProblemDetail(HttpStatus.NOT_FOUND, "resource-not-found", "Resource not found.", ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotCreatedException.class)
    public ProblemDetail handleResourceNotCreated(ResourceNotCreatedException ex, HttpServletRequest request) {
        LOG.error("Resource not created exception with message={}.", ex.getMessage());

        return buildProblemDetail(HttpStatus.BAD_REQUEST, "resource-not-created", "Resource not created.", ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceMappingFailedException.class)
    public ProblemDetail handleResourceMappingFailed(ResourceMappingFailedException ex, HttpServletRequest request) {
        LOG.error("Resource mapping failed exception with message={}.", ex.getMessage());

        return buildProblemDetail(HttpStatus.BAD_REQUEST, "resource-mapping-failed", "Resource mapping failed.", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex, HttpServletRequest request) {
        LOG.error("Internal server error exception with message={}.", ex.getMessage());

        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "internal-error", "Internal server error.", ex.getMessage(), request);
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String type, String title, String detail,
        HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(BASE_URL + type));
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }
}
