package com.myapp.reservation_calendar.reservation.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeConverter {
    private TimeConverter() { }

    public static LocalDateTime nowKst(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
