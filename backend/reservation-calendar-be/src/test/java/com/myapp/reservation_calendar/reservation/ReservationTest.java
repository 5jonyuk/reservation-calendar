package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    @Test
    void from_메서드는_Request의_필드를_정확히_Reservation으로_복사한다() {
        ReservationRequest request = new ReservationRequest(
                "오종혁",
                "010-1234-5678",
                "아메리카노",
                3000,
                LocalDateTime.of(2025, 11, 10, 15, 0),
                null,
                false,
                false
        );

        Reservation reservation = Reservation.from(request);

        assertThat(reservation.getCustomerName()).isEqualTo("오종혁");
        assertThat(reservation.getCustomerPhone()).isEqualTo("010-1234-5678");
        assertThat(reservation.getMenu()).isEqualTo("아메리카노");
        assertThat(reservation.getAmount()).isEqualTo(3000);
        assertThat(reservation.getPickupTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 15, 0));
        assertThat(reservation.getDropTime()).isNull();
        assertThat(reservation.isPaymentCompleted()).isFalse();
        assertThat(reservation.isPickupCompleted()).isFalse();
    }
}