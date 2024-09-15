package com.encora.samples.nimbus.security.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Utility class for generating, parsing, and validating JWTs (JSON Web Tokens).
 */
@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  /**
   * Generates a JWT token for the given user ID.
   *
   * @param userId The ID of the user to generate the token for.
   * @return The generated JWT token.
   */
  public String generateJwtToken(String userId) {
    return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(Date.from(OffsetDateTime.now()
                    .plusMinutes(jwtExpirationMs)
                    .toInstant()))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  /**
   * Extracts the user ID from the given JWT token.
   *
   * @param token The JWT token to extract the user ID from.
   * @return The user ID extracted from the token.
   */
  public String getUserIdFromJwtToken(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

    return claims.getSubject();
  }

  /**
   * Validates the given JWT token.
   *
   * @param authToken The JWT token to validate.
   * @return True if the token is valid, false otherwise.
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (Exception e) {
      // Handle JWT validation exceptions (e.g., expired token)
      return false;
    }
  }
}

