package api.arduinothermohygrometer.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@DisplayName("Tests for security configuration.")
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Test
    @DisplayName("Given actuator health endpoint when accessed then return 200 OK")
    void givenActuatorEndpoint_whenAccessed_thenReturn200OK() {
        mockMvcTester.get()
                     .uri("/actuator/health")
                     .header("X-API-KEY", "api-key-secret")
                     .exchange()
                     .assertThat()
                     .hasStatusOk();
    }

    @Test
    @DisplayName("Given actuator health endpoint when accessed then apply security headers")
    void givenActuatorEndpoint_whenAccessed_thenApplySecurityHeaders() {
        mockMvcTester.get()
                     .uri("/actuator/health")
                     .header("X-API-KEY", "api-key-secret")
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
    @DisplayName("Given actuator health endpoint when accessed over HTTPS then apply HSTS header")
    void givenActuatorEndpoint_whenAccessedOverHttps_thenApplyHstsHeader() {
        mockMvcTester.get()
                     .uri("/actuator/health")
                     .secure(true)
                     .header("X-API-KEY", "api-key-secret")
                     .exchange()
                     .assertThat()
                     .hasHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains")
                     .hasStatusOk();
    }

    @Test
    @DisplayName("Given non actuator endpoint when accessed then return 403 FORBIDDEN")
    void givenNonActuatorEndpoint_whenAccessed_thenReturn403Forbidden() {
        mockMvcTester.get()
                     .uri("/random-endpoint")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Given actuator endpoint when accessed without API-KEY then return 403 FORBIDDEN")
    void givenEndpointWithoutApiKey_whenAccessed_thenThrowInvalidApiKeyException() {
        mockMvcTester.get()
                     .uri("/actuator")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.FORBIDDEN);
    }
}
