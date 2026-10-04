package api.arduinothermohygrometer.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.AuthenticationApi;
import api.arduinothermohygrometer.dto.JwtTokenDto;
import api.arduinothermohygrometer.dto.JwtTokenResponse;
import api.arduinothermohygrometer.model.IssuedToken;
import api.arduinothermohygrometer.properties.SecurityProperties;
import api.arduinothermohygrometer.service.JwtTokenService;

@RestController
public class AuthenticationController implements AuthenticationApi {
  private final JwtTokenService jwtTokenService;
  private final PasswordEncoder passwordEncoder;
  private final SecurityProperties securityProperties;

  public AuthenticationController(
      final JwtTokenService jwtTokenService,
      final PasswordEncoder passwordEncoder,
      final SecurityProperties securityProperties) {
    this.jwtTokenService = jwtTokenService;
    this.passwordEncoder = passwordEncoder;
    this.securityProperties = securityProperties;
  }

  public ResponseEntity<JwtTokenResponse> issueToken(final JwtTokenDto jwtTokenDto) {
    if (jwtTokenDto == null || jwtTokenDto.getPassword() == null) {
      throw new BadCredentialsException("Empty credentials.");
    }

    boolean correctUsername = securityProperties.username().equals(jwtTokenDto.getUsername());
    boolean correctPassword =
        passwordEncoder.matches(jwtTokenDto.getPassword(), securityProperties.passwordHash());
    if (!correctUsername || !correctPassword) {
      throw new BadCredentialsException("Invalid credentials.");
    }

    IssuedToken issuedToken = jwtTokenService.issueToken();

    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(
            new JwtTokenResponse(
                issuedToken.accessToken(), issuedToken.tokenType(), issuedToken.expiresIn()));
  }
}
