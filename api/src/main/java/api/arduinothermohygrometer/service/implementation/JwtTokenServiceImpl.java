package api.arduinothermohygrometer.service.implementation;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.model.IssuedToken;
import api.arduinothermohygrometer.properties.SecurityProperties;
import api.arduinothermohygrometer.service.JwtTokenService;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
  private final Clock clock;
  private final JwtEncoder jwtEncoder;
  private final SecurityProperties securityProperties;

  public JwtTokenServiceImpl(
      final JwtEncoder jwtEncoder, final SecurityProperties securityProperties) {
    this.clock = Clock.systemUTC();
    this.jwtEncoder = jwtEncoder;
    this.securityProperties = securityProperties;
  }

  public IssuedToken issueToken() {
    Instant issuedAt = clock.instant();
    Instant expiresAt = issuedAt.plus(securityProperties.accessTokenTtl());

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer(securityProperties.jwtIssuer())
            .subject(securityProperties.username())
            .audience(java.util.List.of(securityProperties.jwtAudience()))
            .id(UUID.randomUUID().toString())
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .claim("roles", securityProperties.roles())
            .build();

    JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(header, claims));

    return new IssuedToken(
        jwt.getTokenValue(), "Bearer", securityProperties.accessTokenTtl().toSeconds());
  }
}
