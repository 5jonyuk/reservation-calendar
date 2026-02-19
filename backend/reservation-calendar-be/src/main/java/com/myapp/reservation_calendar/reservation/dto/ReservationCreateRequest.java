package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateRequest(
        String customerName,
        String customerPhone,
        String menu,
        Integer amount,
        String memo,
        LocalDate pickupDate,
        LocalTime pickupTime,
        Boolean paymentCompleted,
        Boolean pickupCompleted
) {
}
