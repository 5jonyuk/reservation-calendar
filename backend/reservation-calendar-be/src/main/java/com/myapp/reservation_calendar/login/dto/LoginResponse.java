package com.myapp.reservation_calendar.login.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}
