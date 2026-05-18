package api.arduinothermohygrometer.util;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;
import jakarta.servlet.http.HttpServletRequest;

public final class ProblemDetailsUtil {
    private ProblemDetailsUtil() {
    }

    public static ProblemDetailsDto buildProblemDetail(HttpStatus httpStatus, String type, String title, String detail, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        return ProblemDetailsDto.builder()
                                .type("https://api.arduinothermohygrometer/errors/" + type)
                                .title(title)
                                .detail(detail)
                                .status(httpStatus.value())
                                .instance(request.getRequestURI())
                                .traceId(traceId != null
                                    ? traceId
                                    : "unknown")
                                .timestamp(LocalDateTime.now())
                                .errors(null)
                                .build();
    }
}
