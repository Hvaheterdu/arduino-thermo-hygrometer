package api.arduinothermohygrometer.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import api.arduinothermohygrometer.base.WebMvcTestBase;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@DisplayName("Security configuration MVC slice integration tests.")
@WebMvcTest
class SecurityConfigIT extends WebMvcTestBase {
    @Autowired
    private MockMvcTester mockMvcTester;

    @BeforeEach
    void setup() {
        when(securityProperties.apiKey()).thenReturn("api-key-secret");
        when(securityProperties.apiHeader()).thenReturn("X-API-KEY");
        when(securityProperties.apiRole()).thenReturn("ROLE_API");
    }

    @Test
    @DisplayName("Root health endpoint should be public and return UP status.")
    void givenNoApiKey_whenGettingRootHealth_thenReturnsStatusOk() {
        mockMvcTester.get()
                     .uri("/health")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .hasBodyTextEqualTo("{\"status\":\"UP\"}");
    }

    @Test
    @DisplayName("Liveness probe should be public and return UP status.")
    void givenNoApiKey_whenGettingLivenessProbe_thenReturnsStatusOkAndUpBody() {
        mockMvcTester.get()
                     .uri("/health/liveness")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .hasBodyTextEqualTo("{\"status\":\"UP\"}");
    }

    @Test
    @DisplayName("Readiness probe should be public and return UP status.")
    void givenNoApiKey_whenGettinReadinessProbe_thenReturnsStatusOkAndUpBody() {
        mockMvcTester.get()
                     .uri("/health/readiness")
                     .exchange()
                     .assertThat()
                     .hasStatusOk()
                     .hasBodyTextEqualTo("{\"status\":\"UP\"}");
    }

    @Test
    @DisplayName("Root health endpoint when accessed then apply security headers.")
    void givenNoApiKey_whenGettingRootHealth_thenApplySecurityHeaders() {
        mockMvcTester.get()
                     .uri("/health")
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
    @DisplayName("Root health endpoint when accessed over HTTPS then apply HSTS header.")
    void givenNoApiKey_whenGettingRootHealth_thenApplyHstsHeader() {
        mockMvcTester.get()
                     .uri("/health")
                     .secure(true)
                     .exchange()
                     .assertThat()
                     .hasHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains")
                     .hasStatusOk();
    }

    @Test
    @DisplayName("Given actuator endpoint when accessed without API-KEY then return 403 FORBIDDEN.")
    void givenNoApiKey_whenGettingRootInfo_thenReturn403Forbidden() {
        mockMvcTester.get()
                     .uri("/info")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Given non actuator endpoint when accessed then return 403 FORBIDDEN.")
    void givenNoApiKey_whenGettingRandomEndpoint_thenReturn403Forbidden() {
        mockMvcTester.get()
                     .uri("/random-endpoint")
                     .exchange()
                     .assertThat()
                     .hasStatus(HttpStatus.FORBIDDEN);
    }
}
