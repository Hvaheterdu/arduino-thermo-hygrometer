package api.arduinothermohygrometer.controller;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import api.arduinothermohygrometer.base.WebMvcTestBase;
import api.arduinothermohygrometer.model.IssuedToken;
import api.arduinothermohygrometer.service.JwtTokenService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest extends WebMvcTestBase {
  @MockitoBean private JwtTokenService jwtTokenService;

  @MockitoBean private PasswordEncoder passwordEncoder;

  @Autowired private MockMvcTester mockMvcTester;

  ClassPathResource validIssuedTokenResponse =
      new ClassPathResource("testfiles/get_issued_token_response.json");

  @Test
  void givenValidCredentials_whenIssuingToken_thenReturn200OK() {
    when(securityProperties.username()).thenReturn("test-user");
    when(securityProperties.passwordHash()).thenReturn("encoded-password");
    when(passwordEncoder.matches("password", "encoded-password")).thenReturn(true);
    when(jwtTokenService.issueToken()).thenReturn(new IssuedToken("signed-token", "Bearer", 1800));

    MvcTestResult result =
        mockMvcTester
            .post()
            .uri("/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test-user\",\"password\":\"password\"}")
            .exchange();

    verify(jwtTokenService).issueToken();
    assertThat(result)
        .hasStatusOk()
        .hasHeader("Cache-Control", "no-store")
        .bodyJson()
        .isStrictlyEqualTo(validIssuedTokenResponse);
  }

  @Test
  void givenInvalidUsername_whenIssuingToken_thenReturn401Unauthorized() {
    when(securityProperties.username()).thenReturn("test-user");
    when(securityProperties.passwordHash()).thenReturn("encoded-password");

    MvcTestResult result =
        mockMvcTester
            .post()
            .uri("/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"other-user\",\"password\":\"password\"}")
            .exchange();

    verify(jwtTokenService, never()).issueToken();
    assertThat(result)
        .hasStatus(HttpStatus.UNAUTHORIZED)
        .failure()
        .isInstanceOf(BadCredentialsException.class)
        .hasMessage("Invalid credentials.");
  }

  @Test
  void givenInvalidPassword_whenIssuingToken_thenReturn401Unauthorized() {
    when(securityProperties.username()).thenReturn("test-user");
    when(securityProperties.passwordHash()).thenReturn("encoded-password");

    MvcTestResult result =
        mockMvcTester
            .post()
            .uri("/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test-user\",\"password\":\"invalid\"}")
            .exchange();

    verify(jwtTokenService, never()).issueToken();
    assertThat(result)
        .hasStatus(HttpStatus.UNAUTHORIZED)
        .failure()
        .isInstanceOf(BadCredentialsException.class)
        .hasMessage("Invalid credentials.");
  }

  @Test
  void givenMissingCredentials_whenIssuingToken_thenReturn400BadRequest() {
    MvcTestResult result =
        mockMvcTester
            .post()
            .uri("/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}")
            .exchange();

    verify(jwtTokenService, never()).issueToken();
    assertThat(result)
        .hasStatus(HttpStatus.BAD_REQUEST)
        .bodyJson()
        .hasPathSatisfying(
            "$.detail",
            path -> assertThat(path).asString().isEqualTo("One or more fields are invalid."))
        .hasPathSatisfying(
            "$.title", path -> assertThat(path).asString().isEqualTo("Entity validation error."))
        .hasPathSatisfying(
            "$.errors",
            path -> assertThat(path).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(2));
  }
}
