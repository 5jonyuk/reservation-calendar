package com.myapp.reservation_calendar.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private Duration accessTokenExpiration = Duration.ofMinutes(30);
    private Duration refreshTokenExpiration = Duration.ofDays(14);
}
