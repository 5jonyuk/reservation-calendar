package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationDetailResponse(
        Long id,
        LocalDate pickupDate,
        LocalTime pickupTime,
        String customerName,
        String customerPhone,
        String menu,
        Integer amount,
        Boolean paymentCompleted,
        Boolean pickupCompleted,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt
) {
}
