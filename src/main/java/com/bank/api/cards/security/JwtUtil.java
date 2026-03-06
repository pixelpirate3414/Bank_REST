package com.bank.api.cards.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * A utility class providing methods for working with JSON Web Tokens (JWT), 
 * including token generation, validation, and extraction of user details.
 * 
 * This class is used by the authentication and authorization mechanisms to create and verify JWT tokens.
 */
@Component
public class JwtUtil {
    
    @Value("${security.jwt.secrete}")
    private String secrete;

    private SecretKey secretKey;

    @Value("${security.token.lifetime}")
    private Long expirationTime;

    @PostConstruct
    void init() {
        secretKey = generateSecretKey(secrete);
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param  details  the user details to generate the token for
     * @return          the generated JWT token
     */
    public String generateToken(UserDetails details) {
        return Jwts.builder()
            .subject(details.getUsername())
            .claim("role", details.getAuthorities())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    private SecretKey generateSecretKey(String secrete) {
        return Keys.hmacShaKeyFor(secrete.getBytes());
    }

    /**
     * Retrieves the username from a given JWT token.
     *
     * @param  token  the JWT token to extract the username from
     * @return        the username associated with the given token
     */
    public String getUserName(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    /**
     * Validates a JWT token against the provided user details.
     *
     * @param  token  the JWT token to validate
     * @param  details  the user details to validate against
     * @return          true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails details) {
        return getUserName(token).equals(details.getUsername()) && !tokenExpired(token);
    }

    /**
     * Checks if a given JWT token has expired.
     *
     * @param  token  the JWT token to check for expiration
     * @return        true if the token has expired, false otherwise
     */
    public boolean tokenExpired(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }
}
