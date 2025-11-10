package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDateTime;

public record ReservationRequest(
        String customerName,
        String customerPhone,
        String menu,
        Integer amount,
        LocalDateTime pickupTime,
        LocalDateTime dropTime,
        Boolean paymentCompleted,
        Boolean pickupCompleted
) {
}
