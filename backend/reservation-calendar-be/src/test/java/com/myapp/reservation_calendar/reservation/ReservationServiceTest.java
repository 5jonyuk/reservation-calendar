package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {
    LocalDateTime nowKst = LocalDateTime.now();
    @Mock
    private ReservationJpaRepository reservationJpaRepository;

    @Mock
    private ReservationValidator reservationValidator;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 예약_생성_성공() {
        ReservationRequest request = new ReservationRequest(
                "오종혁",
                "010-1234-5678",
                "브라우니",
                5000,
                nowKst.toLocalDate(),
                nowKst.toLocalTime().plusHours(1),
                false,
                false
        );
        Reservation savedReservation = Reservation.from(request);
        savedReservation = Reservation.builder()
                .id(1L)
                .customerName(savedReservation.getCustomerName())
                .customerPhone(savedReservation.getCustomerPhone())
                .menu(savedReservation.getMenu())
                .amount(savedReservation.getAmount())
                .pickupTime(savedReservation.getPickupTime())
                .paymentCompleted(savedReservation.getPaymentCompleted())
                .pickupCompleted(savedReservation.getPickupCompleted())
                .createdAt(LocalDateTime.now())
                .build();
        when(reservationJpaRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        Reservation result = reservationService.createReservation(request);

        verify(reservationValidator).validate(any(Reservation.class));
        verify(reservationJpaRepository).save(any(Reservation.class));
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCustomerName()).isEqualTo("오종혁");
        assertThat(result.getAmount()).isEqualTo(5000);
    }

    @Test
    void 예약_생성_실패() {
        ReservationRequest request = new ReservationRequest(
                "오종혁",
                "010-1234-5678",
                "브라우니",
                -5000,
                nowKst.toLocalDate(),
                nowKst.toLocalTime().plusHours(1),
                false,
                false
        );

        doThrow(IllegalArgumentException.class)
                .when(reservationValidator).validate(any(Reservation.class));

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}