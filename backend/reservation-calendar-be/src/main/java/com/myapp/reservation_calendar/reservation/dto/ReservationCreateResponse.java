package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationCreateResponse(
        Long id,
        String customerName,
        String customerPhone,
        String menu,
        Integer amount,
        LocalDate pickupDate,
        LocalTime pickupTime,
        Boolean paymentCompleted,
        Boolean pickupCompleted,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}
