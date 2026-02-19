package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationCreateRequest;
import com.myapp.reservation_calendar.util.TimeConverter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {
    LocalDateTime nowKtc = TimeConverter.nowKst();
    @Test
    void from_메서드는_Request의_필드를_정확히_Reservation으로_복사한다() {
        ReservationCreateRequest request = new ReservationCreateRequest(
                "오종혁",
                "010-1234-5678",
                "아메리카노",
                3000,
                "메모 테스트",
                nowKtc.toLocalDate(),
                nowKtc.toLocalTime().plusHours(1),
                false,
                false
        );

        Reservation reservation = Reservation.from(request);

        assertThat(reservation.getCustomerName()).isEqualTo("오종혁");
        assertThat(reservation.getCustomerPhone()).isEqualTo("010-1234-5678");
        assertThat(reservation.getMenu()).isEqualTo("아메리카노");
        assertThat(reservation.getAmount()).isEqualTo(3000);
        assertThat(reservation.getMemo()).isEqualTo("메모 테스트");
        assertThat(reservation.getPickupDate()).isEqualTo(nowKtc.toLocalDate());
        assertThat(reservation.getPickupTime()).isEqualTo(nowKtc.toLocalTime().plusHours(1));
        assertThat(reservation.getPaymentCompleted()).isFalse();
        assertThat(reservation.getPickupCompleted()).isFalse();
    }
}
