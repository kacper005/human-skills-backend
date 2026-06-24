package com.talection.talection.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/**
 * Utility class for handling JWT tokens.
 *
 * <p>Inspiration taken from:
 * <a href="https://github.com/strazdinsg/app-dev/tree/main/security-demos/05-jwt-authentication">...</a>
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Generates a token for a user.
     *
     * @param userDetails the details of the user to generate a token for
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails) {
        final long timeNow = System.currentTimeMillis();
        final long millisecondsInHour = (long) 60 * 60 * 1000;
        final long timeAfterThreeHours = timeNow + 3 * millisecondsInHour;

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(timeNow))
                .setExpiration(new Date(timeAfterThreeHours))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract the username from a token.
     *
     * @param token the token to extract the username from
     * @return the username
     */
    public String extractUsername(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    /**
     * Extract a claim from a token.
     *
     * @param token the token to extract the claim from
     * @return the claim
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets the signing key.
     *
     * @return the signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    /**
     * Validate a token.
     *
     * @param token the token to validate
     * @param userDetails the user details to validate the token against
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return userDetails != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Check if a token is expired.
     *
     * @param token the token to check
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract the expiration date from a token.
     *
     * @param token the token to extract the expiration date from
     * @return the expiration date
     */
    private Date extractExpiration(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }
}
