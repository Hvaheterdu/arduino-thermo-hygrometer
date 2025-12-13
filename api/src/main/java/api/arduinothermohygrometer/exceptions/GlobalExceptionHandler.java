package api.arduinothermohygrometer.exceptions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/validation-error"));
        problemDetail.setTitle("Model validation error.");
        problemDetail.setDetail("One or more fields are invalid.");
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest httpServletRequest) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/resource-not-found"));
        problemDetail.setTitle("Resource not found.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(ResourceNotCreatedException.class)
    public ProblemDetail handleResourceNotCreated(ResourceNotCreatedException ex,
            HttpServletRequest httpServletRequest) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/resource-not-created"));
        problemDetail.setTitle("Resource not created.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex, HttpServletRequest httpServletRequest) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/internal-error"));
        problemDetail.setTitle("Internal server error.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        return problemDetail;
    }
}
