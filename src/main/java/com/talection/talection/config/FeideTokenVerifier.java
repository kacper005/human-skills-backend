package com.talection.talection.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeideTokenVerifier {
  @Value("${feide.issuer-uri}")
  private String issuerUri;

  @Value("${feide.client-id}")
  private String clientId;

  public Jwt verifyToken(String idToken) {
    if (idToken == null || idToken.isEmpty()) {
      throw new IllegalArgumentException("FEIDE Id token must not be null or empty");
    }
    if (issuerUri == null || issuerUri.isBlank() || "YOUR_FEIDE_ISSUER_URI".equals(issuerUri)) {
         throw new IllegalStateException("feide.issuer-uri is not configured");
    }
    if (clientId == null || clientId.isBlank() || "YOUR_FEIDE_CLIENT_ID".equals(clientId)) {
      throw new IllegalStateException("feide.client-id is not configured");
    }

    JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    NimbusJwtDecoder nimbusJwtDecoder = (NimbusJwtDecoder) jwtDecoder;

    OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(issuerUri);

    OAuth2TokenValidator<Jwt> audienceValidator = token -> {
      List<String> audience = token.getAudience();
      if (audience != null && audience.contains(clientId)) {
        return OAuth2TokenValidatorResult.success();
      }
      OAuth2Error error = new OAuth2Error(
            "invalid_token",
            "The required audience is missing",
            null
      );
      return OAuth2TokenValidatorResult.failure(error);
    };

    nimbusJwtDecoder.setJwtValidator(
      new DelegatingOAuth2TokenValidator<>(issuerValidator, audienceValidator)
    );

    try {
      return nimbusJwtDecoder.decode(idToken);
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid FEIDE ID token:", e);
    }
  }
}
