package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDateTime;

public record ReservationRequest(
        String customerName,
        String customerPhone,
        String menu,
        LocalDateTime pickupTime,
        LocalDateTime dropTime,
        boolean paymentCompleted,
        boolean pickupCompleted
) {
}
