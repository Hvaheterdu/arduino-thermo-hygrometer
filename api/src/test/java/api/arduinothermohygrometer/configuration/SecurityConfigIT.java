package api.arduinothermohygrometer.configuration;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SecurityConfigIT {
  @MockitoBean private OpenApiConfig openApiConfig;

  @Autowired private MockMvcTester mockMvcTester;

  @Autowired private ObjectMapper objectMapper;

  @Nested
  class HealthEndpoint {
    @Test
    void givenNoJwt_whenGettingHealth_thenReturn200OkAndUpBody() {
      mockMvcTester
          .get()
          .uri("/actuator/health")
          .exchange()
          .assertThat()
          .hasStatusOk()
          .bodyJson()
          .hasPathSatisfying("$.status", path -> assertThat(path).asString().isEqualTo("UP"))
          .doesNotHavePath("$.components");
    }

    @Test
    void givenNoJwt_whenGettingLivenessProbe_thenReturn200OkAndUpBody() {
      mockMvcTester
          .get()
          .uri("/actuator/health/liveness")
          .exchange()
          .assertThat()
          .hasStatusOk()
          .bodyJson()
          .hasPathSatisfying("$.status", path -> assertThat(path).asString().isEqualTo("UP"));
    }

    @Test
    void givenNoJwt_whenGettingReadinessProbe_thenReturn200OkAndUpBody() {
      mockMvcTester
          .get()
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
    void givenNoJwt_whenGettingHealth_thenApplySecurityHeadersToResponse() {
      mockMvcTester
          .get()
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
    void givenNoJwt_whenGettingHealth_thenApplyHstsHeaderToResponse() {
      mockMvcTester
          .get()
          .uri("/actuator/health")
          .secure(true)
          .exchange()
          .assertThat()
          .hasHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains")
          .hasStatusOk();
    }

    @Test
    void givenNoJwt_whenGettingInfo_thenReturn401Unauthorized() {
      mockMvcTester
          .get()
          .uri("/actuator/info")
          .exchange()
          .assertThat()
          .hasStatus(HttpStatus.UNAUTHORIZED);
    }
  }

  @Nested
  class JwtAuthentication {
    @Test
    void givenValidCredentials_whenIssuingToken_thenReturnBearerToken() {
      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/auth/token")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"test-user\",\"password\":\"password\"}")
              .exchange();

      assertThat(result)
          .hasStatusOk()
          .bodyJson()
          .hasPathSatisfying("$.tokenType", path -> assertThat(path).asString().isEqualTo("Bearer"))
          .hasPathSatisfying("$.accessToken", path -> assertThat(path).asString().isNotBlank())
          .hasPathSatisfying("$.expiresIn", path -> assertThat(path).asNumber().isEqualTo(1800));
    }

    @Test
    void givenInvalidCredentials_whenIssuingToken_thenReturn401Unauthorized() {
      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/auth/token")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"test-user\",\"password\":\"invalid\"}")
              .exchange();

      assertThat(result).hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void givenValidJwt_whenGettingApiEndpoint_thenAuthenticateRequest() throws Exception {
      MvcTestResult tokenResult =
          mockMvcTester
              .post()
              .uri("/auth/token")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"username\":\"test-user\",\"password\":\"password\"}")
              .exchange();

      JsonNode token = objectMapper.readTree(tokenResult.getResponse().getContentAsString());

      mockMvcTester
          .get()
          .uri("/api/v1/batteries")
          .param("registeredAt", LocalDateTime.parse("2026-01-04T12:00:00").toString())
          .param("dateOnly", String.valueOf(true))
          .header("Authorization", "Bearer " + token.get("accessToken").asString())
          .exchange()
          .assertThat()
          .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void givenInvalidJwt_whenGettingApiEndpoint_thenReturn401Unauthorized() {
      mockMvcTester
          .get()
          .uri("/api/v1/batteries")
          .param("registeredAt", LocalDateTime.parse("2026-01-04T12:00:00").toString())
          .param("dateOnly", String.valueOf(true))
          .header("Authorization", "Bearer invalid-token")
          .exchange()
          .assertThat()
          .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void givenNoJwt_whenGettingApiEndpoint_thenReturn401Unauthorized() {
      mockMvcTester
          .get()
          .uri("/api/v1/batteries")
          .param("registeredAt", LocalDateTime.parse("2026-01-04T12:00:00").toString())
          .param("dateOnly", String.valueOf(true))
          .exchange()
          .assertThat()
          .hasStatus(HttpStatus.UNAUTHORIZED);
    }
  }
}
