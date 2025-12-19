package api.arduinothermohygrometer.exceptions;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class, produces = "application/problem+json")
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest) {
        LOG.error("Method argument not valid exception with message={}.", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/validation-error"));
        problemDetail.setTitle("Model validation error.");
        problemDetail.setDetail("One or more fields are invalid.");
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(value = ResourceNotFoundException.class, produces = "application/problem+json")
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest httpServletRequest) {
        LOG.error("Resource not found exception with message={}.", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/resource-not-found"));
        problemDetail.setTitle("Resource not found.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(value = ResourceNotCreatedException.class, produces = "application/problem+json")
    public ProblemDetail handleResourceNotCreated(ResourceNotCreatedException ex,
                                                  HttpServletRequest httpServletRequest) {
        LOG.error("Resource not created exception with message={}.", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/resource-not-created"));
        problemDetail.setTitle("Resource not created.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(value = Exception.class, produces = "application/problem+json")
    public ProblemDetail handleGeneralException(Exception ex, HttpServletRequest httpServletRequest) {
        LOG.error("Internal server error exception with message={}.", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setType(URI.create("https://api.arduinothermohygrometer/errors/internal-error"));
        problemDetail.setTitle("Internal server error.");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(httpServletRequest.getRequestURI()));

        return problemDetail;
    }
}
