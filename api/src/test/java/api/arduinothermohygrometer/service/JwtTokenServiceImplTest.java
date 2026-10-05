package api.arduinothermohygrometer.service;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import api.arduinothermohygrometer.model.IssuedToken;
import api.arduinothermohygrometer.properties.SecurityProperties;
import api.arduinothermohygrometer.service.implementation.JwtTokenServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class JwtTokenServiceImplTest {
  private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(30);

  @Mock private Jwt jwt;

  @Mock private SecurityProperties securityProperties;

  @Mock private JwtEncoder jwtEncoder;

  @InjectMocks private JwtTokenServiceImpl jwtTokenService;

  @Captor private ArgumentCaptor<JwtEncoderParameters> jwtEncoderParametersArgumentCaptor;

  @Test
  void givenTokenConfiguration_whenIssuingToken_thenEncodeExpectedClaimsAndReturnMetadata() {
    when(jwt.getTokenValue()).thenReturn("signed-token");
    when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
    when(securityProperties.jwtIssuer()).thenReturn("test-issuer");
    when(securityProperties.username()).thenReturn("test-user");
    when(securityProperties.jwtAudience()).thenReturn("test-audience");
    when(securityProperties.accessTokenTtl()).thenReturn(ACCESS_TOKEN_TTL);
    when(securityProperties.roles()).thenReturn(List.of("ACTUATOR", "API_ADMIN"));

    IssuedToken issuedToken = jwtTokenService.issueToken();

    verify(jwtEncoder).encode(jwtEncoderParametersArgumentCaptor.capture());
    JwtEncoderParameters jwtEncoderParameters = jwtEncoderParametersArgumentCaptor.getValue();
    JwtClaimsSet claims = jwtEncoderParameters.getClaims();
    assertThat(jwtEncoderParameters.getJwsHeader()).isNotNull();
    assertThat(jwtEncoderParameters.getJwsHeader().getAlgorithm()).isEqualTo(MacAlgorithm.HS512);
    assertThat(claims.getClaimAsString("iss")).isEqualTo("test-issuer");
    assertThat(claims.getSubject()).isEqualTo("test-user");
    assertThat(claims.getAudience()).containsExactly("test-audience");
    assertThat(claims.getId()).isNotBlank();
    assertThat(claims.getIssuedAt()).isNotNull();
    assertThat(claims.getExpiresAt()).isEqualTo(claims.getIssuedAt().plus(ACCESS_TOKEN_TTL));
    assertThat(claims.getClaimAsStringList("roles")).containsExactly("ACTUATOR", "API_ADMIN");
    assertThat(issuedToken.accessToken()).isEqualTo("signed-token");
    assertThat(issuedToken.tokenType()).isEqualTo("Bearer");
    assertThat(issuedToken.expiresIn()).isEqualTo(ACCESS_TOKEN_TTL.toSeconds());
  }
}
