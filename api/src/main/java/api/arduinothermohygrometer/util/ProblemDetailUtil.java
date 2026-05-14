package api.arduinothermohygrometer.util;

import java.net.URI;
import java.time.Instant;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import jakarta.servlet.http.HttpServletRequest;

public class ProblemDetailUtil {
    private static final String BASE_URL = "https://api.arduinothermohygrometer/errors/";

    public static ProblemDetail buildProblemDetail(HttpStatus httpStatus, String type, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setType(URI.create(BASE_URL + type));
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        String traceId = MDC.get("traceId");
        if (traceId != null) {
            problemDetail.setProperty("traceId", traceId);
        }
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
