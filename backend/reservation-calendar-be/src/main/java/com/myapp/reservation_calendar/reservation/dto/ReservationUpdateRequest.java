package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationUpdateRequest(
        LocalDate pickupDate,
        LocalTime pickupTime,
        String customerName,
        String customerPhone,
        String menu,
        Integer amount,
        String memo,
        Boolean paymentCompleted,
        Boolean pickupCompleted
) {
}
