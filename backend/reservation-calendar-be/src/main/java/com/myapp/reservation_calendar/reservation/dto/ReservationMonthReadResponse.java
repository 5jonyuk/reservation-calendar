package com.myapp.reservation_calendar.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationMonthReadResponse(
        LocalDate pickupDate,
        LocalTime pickupTime
) {
}
