package com.myapp.reservation_calendar.config.jwt;

import com.myapp.reservation_calendar.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
    private final JwtProperties jwtProperties;
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());
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
                new org.springframework.security.core.userdetails.User(claims.getSubject(),"",authorities),
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
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            // MalformedJwtException: JWT 구조가 잘못되었을 때
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            // ExpiredJwtException: 토큰이 만료되었을 때
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            // UnsupportedJwtException: 지원되지 않는 JWT 형식일 때
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException: 토큰이 null이거나 비었을 때
            System.err.println("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unknown error during token validation: " + e.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
