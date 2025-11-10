package com.myapp.reservation_calendar.reservation.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class TimeConverterTest {

    @Test
    void nowKst는_UTC보다_9시간_빠르다() {
        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime nowKst = TimeConverter.nowKst();

        long hoursBetween = Duration.between(nowUtc, nowKst).toHours();

        assertThat(hoursBetween).isEqualTo(9);
    }
}