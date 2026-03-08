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
 * Вспомогательный класс, предоставляющий методы для работы с JSON Web Tokens (JWT),
 * включая генерацию токенов, проверку и извлечение данных пользователя
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
     * Генерирует JW токен для указанного пользователя.
     * @param details данные пользователя, содержащие имя и роли
     * @return сгенерированный JWT токен
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

    /**
     * Генерирует секретный ключ для подписи JWT на основе строки секрета.
     * @param secrete строковое значение секрета
     * @return сгенерированный {@link SecretKey}
     */
    private SecretKey generateSecretKey(String secrete) {
        return Keys.hmacShaKeyFor(secrete.getBytes());
    }

    /**
     * Извлекает имя пользователя из JW токена.
     * @param token JW токен
     * @return имя пользователя, указанное в токене
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
     * Проверяет валидность JWT токена.
     * @param token JW токен
     * @param details данные пользователя
     * @return {@code true} если токен валиден, иначе {@code false}
     */
    public boolean validateToken(String token, UserDetails details) {
        return getUserName(token).equals(details.getUsername()) && !tokenExpired(token);
    }

    /**
     * Проверяет истек ли срок действия JWT токена.
     * @param token JW токен
     * @return {@code true} если токен истёк, иначе {@code false}
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
