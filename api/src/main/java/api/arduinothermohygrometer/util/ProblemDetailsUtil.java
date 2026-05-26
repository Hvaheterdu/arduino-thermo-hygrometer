package api.arduinothermohygrometer.util;

import java.time.LocalDateTime;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import api.arduinothermohygrometer.dto.ProblemDetailsDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProblemDetailsUtil {
    public static ProblemDetailsDto buildProblemDetail(final HttpStatus httpStatus, final String type, final String title, final String detail,
            final HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        return ProblemDetailsDto.builder()
                .type("https://api.arduinothermohygrometer/errors/" + type)
                .title(title)
                .detail(detail)
                .status(httpStatus.value())
                .instance(request.getRequestURI())
                .traceId(traceId != null ? traceId : "unknown")
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
    }
}
