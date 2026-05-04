package api.arduinothermohygrometer.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@DisplayName("Security configuration MVC slice integration tests.")
@SpringBootTest
class SecurityConfigIT {
    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private OpenApiConfig openApiConfig;

    @Test
    @DisplayName("Health endpoint returns 200 OK and UP status with no API key.")
    void givenNoApiKey_whenGettingHealth_thenReturn200OKAndUpBody() {
        mockMvcTester.get()
                     .uri("/actuator/health")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .bodyJson()
                     .hasPathSatisfying("$.status",
                         path -> assertThat(path).asString().isEqualTo("UP"))
                     .doesNotHavePath("$.components");
    }

    @Test
    @DisplayName("Health endpoint returns 200 OK and components body with API key.")
    void givenApiKey_whenGettingHealth_thenReturn200OKAndComponentsInBody() {
        mockMvcTester.get()
                     .uri("/actuator/health")
                     .header("X-API-KEY", "api-secret-key")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .bodyJson()
                     .hasPath("$.components");
    }

    @Test
    @DisplayName("Liveness probe returns 200 OK and UP status with no API key.")
    void givenNoApiKey_whenGettingLivenessProbe_thenReturn200OKAndUpBody() {
        mockMvcTester.get()
                     .uri("/actuator/health/liveness")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .bodyJson()
                     .hasPathSatisfying("$.status",
                         path -> assertThat(path).asString().isEqualTo("UP"));
    }

    @Test
    @DisplayName("Readiness probe returns 200 OK and UP status with no API key.")
    void givenNoApiKey_whenGettingReadinessProbe_thenReturn200OKAndUpBody() {
        mockMvcTester.get()
                     .uri("/actuator/health/readiness")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .bodyJson()
                     .hasPathSatisfying("$.status",
                         path -> assertThat(path).asString().isEqualTo("UP"));
    }

    @Test
    @DisplayName("Health endpoint when accessed without API key applies security headers to response.")
    void givenNoApiKey_whenGettingHealth_thenApplySecurityHeadersToResponse() {
        mockMvcTester.get()
                     .uri("/actuator/health")
                     .exchange()
                     .assertThat()
                     .hasHeader("X-Content-Type-Options", "nosniff")
                     .hasHeader("X-Frame-Options", "DENY")
                     .hasHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
                     .hasHeader("Content-Security-Policy",
                         "connect-src 'self'; "
                             + "default-src 'self'; "
                             + "frame-ancestors 'none'; "
                             + "img-src 'self' data:; "
                             + "script-src 'self'; "
                             + "style-src 'self' 'unsafe-inline';")
                     .hasHeader("Referrer-Policy", "no-referrer")
                     .hasStatusOk();
    }

    @Test
    @DisplayName("Health endpoint when accessed without API key over HTTPS applies HSTS header to response.")
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
    @DisplayName("Info endpoint when accessed without API key returns 401 UNAUTHORIZED.")
    void givenNoApiKey_whenGettingInfo_thenReturn401Unauthorized() {
        mockMvcTester.get()
                     .uri("/actuator/info")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Random endpoint when accessed without API key returns 401 UNAUTHORIZED.")
    void givenNoApiKey_whenGettingRandomEndpoint_thenReturn401Unauthorized() {
        mockMvcTester.get()
                     .uri("/random-endpoint")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Random endpoint when accessed with API key returns 403 FORBIDDEN.")
    void givenApiKey_whenGettingRandomEndpoint_thenReturn403Forbidden() {
        mockMvcTester.get()
                     .uri("/random-endpoint")
                     .header("X-API-KEY", "api-secret-key")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.FORBIDDEN);
    }
}
