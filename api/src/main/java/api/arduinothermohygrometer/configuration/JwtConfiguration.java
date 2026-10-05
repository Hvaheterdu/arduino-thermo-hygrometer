package api.arduinothermohygrometer.configuration;

import java.util.Base64;
import java.util.Collection;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import api.arduinothermohygrometer.properties.SecurityProperties;

@Configuration
public class JwtConfiguration {
  private final SecurityProperties securityProperties;

  public JwtConfiguration(final SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Bean
  SecretKey jwtSecretKey() {
    byte[] keyBytes = Base64.getDecoder().decode(securityProperties.jwtSecretBase64());
    if (keyBytes.length < 64) {
      throw new IllegalStateException("JWT secret length is incorrect.");
    }

    return new SecretKeySpec(keyBytes, "HmacSHA512");
  }

  @Bean
  JwtDecoder jwtDecoder(final SecretKey jwtSecretKey) {
    var nimbusJwtDecoder =
        NimbusJwtDecoder.withSecretKey(jwtSecretKey).macAlgorithm(MacAlgorithm.HS512).build();
    OAuth2TokenValidator<Jwt> issuerValidator =
        JwtValidators.createDefaultWithIssuer(securityProperties.jwtIssuer());
    OAuth2TokenValidator<Jwt> audienceValidator =
        new JwtClaimValidator<Collection<String>>(
            "aud", audience -> audience.contains(securityProperties.jwtAudience()));
    nimbusJwtDecoder.setJwtValidator(
        new org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator<>(
            issuerValidator, audienceValidator));

    return nimbusJwtDecoder;
  }

  @Bean
  JwtEncoder jwtEncoder(final SecretKey jwtSecretKey) {
    return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey));
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
