package com.myapp.reservation_calendar.config.jwt;

import com.myapp.reservation_calendar.user.User;
import com.myapp.reservation_calendar.user.UserValidator;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private static final String ERROR_MESSAGE_SIGNATURE_EXCEPTION = "[ERROR] Invalid JWT signature: ";
    private static final String ERROR_MESSAGE_MAL_FORMED_JWT_EXCEPTION = "[ERROR] Invalid JWT token: ";
    private static final String ERROR_MESSAGE_EXPIRED_JWT_EXCEPTION = "[ERROR] JWT token is expired: ";
    private static final String ERROR_MESSAGE_UNSUPPORTED_JWT_EXCEPTION = "[ERROR] JWT token is unsupported: ";
    private static final String ERROR_MESSAGE_TOKEN_EMPTY_OR_NULL = "[ERROR] JWT claims string is empty: ";
    private static final String ERROR_MESSAGE_UNKNOWN = "[ERROR] Unknown error during token validation: ";

    private final JwtProperties jwtProperties;
    private final UserValidator userValidator;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user, Duration expiredAt) {
        userValidator.validate(user);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());
        return makeToken(expiry, user);
    }

    public String generateRefreshToken(User user) {
        userValidator.validate(user);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration().toMillis());
        return makeToken(expiry, user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()

                .issuer(jwtProperties.getIssuer())
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .claim("id", user.getId()) // 비공개 클레임

                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),
                token,
                authorities
        );
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key) // SecretKey를 사용하여 서명을 검증
                    .build()
                    .parseSignedClaims(token); // 토큰 파싱 시 유효하지 않으면 예외 발생

            return true;
        } catch (SignatureException e) {
            // SignatureException: 서명이 일치하지 않을 때 (토큰 위변조)
            System.err.println(ERROR_MESSAGE_SIGNATURE_EXCEPTION + e.getMessage());
        } catch (MalformedJwtException e) {
            // MalformedJwtException: JWT 구조가 잘못되었을 때
            System.err.println(ERROR_MESSAGE_MAL_FORMED_JWT_EXCEPTION + e.getMessage());
        } catch (ExpiredJwtException e) {
            // ExpiredJwtException: 토큰이 만료되었을 때
            System.err.println(ERROR_MESSAGE_EXPIRED_JWT_EXCEPTION + e.getMessage());
        } catch (UnsupportedJwtException e) {
            // UnsupportedJwtException: 지원되지 않는 JWT 형식일 때
            System.err.println(ERROR_MESSAGE_UNSUPPORTED_JWT_EXCEPTION + e.getMessage());
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException: 토큰이 null이거나 비었을 때
            System.err.println(ERROR_MESSAGE_TOKEN_EMPTY_OR_NULL + e.getMessage());
        } catch (Exception e) {
            System.err.println(ERROR_MESSAGE_UNKNOWN + e.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
