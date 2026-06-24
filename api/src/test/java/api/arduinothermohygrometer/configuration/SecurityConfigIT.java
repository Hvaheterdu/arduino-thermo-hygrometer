package api.arduinothermohygrometer.configuration;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SecurityConfigIT {
    @MockitoBean
    private OpenApiConfig openApiConfig;

    @Autowired
    private MockMvcTester mockMvcTester;

    @Nested
    class HealthEndpoint {
        @Test
        void givenNoApiKey_whenGettingHealth_thenReturn200OkAndUpBody() {
            mockMvcTester.get()
                         .uri("/actuator/health")
                         .exchange()
                         .assertThat()
                         .hasStatusOk()
                         .bodyJson()
                         .hasPathSatisfying("$.status", path -> assertThat(path).asString().isEqualTo("UP"))
                         .doesNotHavePath("$.components");
        }

        @Test
        void givenNoApiKey_whenGettingLivenessProbe_thenReturn200OkAndUpBody() {
            mockMvcTester.get()
                         .uri("/actuator/health/liveness")
                         .exchange()
                         .assertThat()
                         .hasStatusOk()
                         .bodyJson()
                         .hasPathSatisfying("$.status", path -> assertThat(path).asString().isEqualTo("UP"));
        }

        @Test
        void givenNoApiKey_whenGettingReadinessProbe_thenReturn200OkAndUpBody() {
            mockMvcTester.get()
                         .uri("/actuator/health/readiness")
                         .exchange()
                         .assertThat()
                         .hasStatusOk()
                         .bodyJson()
                         .hasPathSatisfying("$.status", path -> assertThat(path).asString().isEqualTo("UP"));
        }
    }

    @Nested
    class SecurityHeaders {
        @Test
        void givenNoApiKey_whenGettingHealth_thenApplySecurityHeadersToResponse() {
            mockMvcTester.get()
                         .uri("/actuator/health")
                         .exchange()
                         .assertThat()
                         .hasHeader("X-Content-Type-Options", "nosniff")
                         .hasHeader("X-Frame-Options", "DENY")
                         .hasHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
                         .hasHeader("Referrer-Policy", "no-referrer")
                         .hasStatusOk();
        }

        @Test
        void givenNoApiKey_whenGettingHealth_thenApplyHstsHeaderToResponse() {
            mockMvcTester.get()
                         .uri("/actuator/health")
                         .secure(true)
                         .exchange()
                         .assertThat()
                         .hasHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains")
                         .hasStatusOk();
        }

        @Test
        void givenNoApiKey_whenGettingInfo_thenReturn401Unauthorized() {
            mockMvcTester.get()
                         .uri("/actuator/info")
                         .exchange()
                         .assertThat()
                         .hasStatus(HttpStatus.UNAUTHORIZED);
        }

        @Test
        void givenNoApiKey_whenGettingRandomEndpoint_thenReturn401Unauthorized() {
            mockMvcTester.get()
                         .uri("/random-endpoint")
                         .exchange()
                         .assertThat()
                         .hasStatus(HttpStatus.UNAUTHORIZED);
        }

        @Test
        void givenApiKey_whenGettingRandomEndpoint_thenReturn403Forbidden() {
            mockMvcTester.get()
                         .uri("/random-endpoint")
                         .header("X-API-KEY", "api-secret-key")
                         .exchange()
                         .assertThat()
                         .hasStatus(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    class RateLimitingFilter {
        @Test
        void givenValidApiKey_whenNotExceedingRateLimit_thenReturn404NotFound() {
            UUID id = UUID.randomUUID();
            for (int i = 0; i < 95; i++) {
                mockMvcTester.get()
                             .uri("/api/v1/batteries/{id}", id)
                             .header("X-API-KEY", "api-secret-key")
                             .exchange()
                             .assertThat()
                             .hasStatus(HttpStatus.NOT_FOUND);
            }
        }

        @Test
        void givenValidApiKey_whenExceedingRateLimit_thenReturn429TooManyRequests() {
            UUID id = UUID.randomUUID();
            for (int i = 0; i < 5; i++) {
                mockMvcTester.get()
                             .uri("/api/v1/batteries/{id}", id)
                             .header("X-API-KEY", "api-secret-key")
                             .exchange()
                             .assertThat()
                             .hasStatus(HttpStatus.NOT_FOUND);
            }

            MvcTestResult result = mockMvcTester.get().uri("/api/v1/batteries/{id}", id).header("X-API-KEY", "api-secret-key").exchange();

            assertThat(result)
                .hasStatus(HttpStatus.TOO_MANY_REQUESTS)
                .bodyJson()
                .hasPathSatisfying("$.type", path ->
                    assertThat(path).asString().isEqualTo("https://api.arduinothermohygrometer/errors/rate-limit"))
                .hasPathSatisfying("$.title", path ->
                    assertThat(path).asString().isEqualTo("Too Many Requests."))
                .hasPathSatisfying("$.detail", path ->
                    assertThat(path).asString().isEqualTo("Rate limit exceeded. Try again later."))
                .hasPathSatisfying("$.status", path ->
                    assertThat(path).asNumber().isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value()));
        }
    }
}
