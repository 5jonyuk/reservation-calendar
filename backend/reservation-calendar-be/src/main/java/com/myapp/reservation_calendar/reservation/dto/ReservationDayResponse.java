package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDayResponse(
        Long id,
        String customerName,
        LocalDate pickupDate,
        LocalTime pickupTime,
        Boolean pickupCompleted,
        Integer amount,
        String menu
) {
}
