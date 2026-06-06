package api.arduinothermohygrometer.exception;

import static api.arduinothermohygrometer.util.ProblemDetailsUtil.buildProblemDetail;

import java.time.LocalDateTime;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import api.arduinothermohygrometer.dto.ProblemDetailsValidationErrorDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException methodArgumentNotValidException,
            @NonNull final HttpHeaders httpHeaders, @NonNull final HttpStatusCode httpStatusCode, @NonNull final WebRequest webRequest) {
        log.error("Method argument not valid exception with message={}.", methodArgumentNotValidException.getMessage());

        List<ProblemDetailsValidationErrorDto> errors = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toValidationError)
                .toList();
        String traceId = MDC.get("traceId");
        var body = ProblemDetailsDto.builder()
                .type("https://api.arduinothermohygrometer/errors/validation-error")
                .title("Entity validation error.")
                .detail("One or more fields are invalid.")
                .status(httpStatusCode.value())
                .instance(webRequest.getContextPath())
                .traceId(traceId != null ? traceId : "unknown")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return new ResponseEntity<>(body, httpHeaders, httpStatusCode);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetailsDto> handleResourceNotFound(final ResourceNotFoundException resourceNotFoundException,
            final HttpServletRequest request) {
        log.error("Resource not found exception with message={}.", resourceNotFoundException.getMessage());
        ProblemDetailsDto body = buildProblemDetail(HttpStatus.NOT_FOUND, "resource-not-found", "Resource not found.",
                resourceNotFoundException.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ResourceNotCreatedException.class)
    public ResponseEntity<ProblemDetailsDto> handleResourceNotCreated(final ResourceNotCreatedException resourceNotCreatedException,
            final HttpServletRequest request) {
        log.error("Resource not created exception with message={}.", resourceNotCreatedException.getMessage());
        ProblemDetailsDto body = buildProblemDetail(HttpStatus.BAD_REQUEST, "resource-not-created", "Resource not created.",
                resourceNotCreatedException.getMessage(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetailsDto> handleGeneralException(final Exception exception, final HttpServletRequest request) {
        log.error("Internal server error exception with message={}.", exception.getMessage());
        ProblemDetailsDto body = buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "internal-error", "Internal server error.",
                exception.getMessage(), request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ProblemDetailsValidationErrorDto toValidationError(final FieldError error) {
        return ProblemDetailsValidationErrorDto.builder()
                .description(error.getDefaultMessage())
                .parameter(error.getField())
                .header(null)
                .pointer("/" + error.getField())
                .build();
    }
}
