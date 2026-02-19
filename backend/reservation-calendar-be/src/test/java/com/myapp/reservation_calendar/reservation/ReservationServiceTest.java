package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    LocalDateTime nowKst = LocalDateTime.now();
    @Mock
    private ReservationJpaRepository reservationJpaRepository;

    @Mock
    private ReservationValidator reservationValidator;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약_생성_성공() {
        ReservationCreateRequest request = new ReservationCreateRequest(
                "오종혁",
                "010-1234-5678",
                "브라우니",
                5000,
                "메모 테스트",
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
        ReservationCreateRequest request = new ReservationCreateRequest(
                "오종혁",
                "010-1234-5678",
                "브라우니",
                -5000,
                null,
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

    @Test
    void 월별_예약조회_성공() {
        YearMonth yearMonth = YearMonth.of(2025, 11);
        LocalDate startDay = yearMonth.atDay(1);
        LocalDate endDay = yearMonth.atEndOfMonth();

        Reservation r = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .amount(5000)
                .pickupDate(LocalDate.of(2025, 11, 11))
                .pickupTime(LocalTime.of(14, 0))
                .build();
        when(reservationJpaRepository.findByPickupDateBetween(startDay, endDay)).thenReturn(List.of(r));

        List<ReservationMonthReadResponse> result = reservationService.findByMonth(yearMonth);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).pickupDate()).isEqualTo(LocalDate.of(2025, 11, 11));
        assertThat(result.get(0).pickupTime()).isEqualTo(LocalTime.of(14, 0));
    }

    @Test
    void 월별예약조회_예약없음() {
        YearMonth yearMonth = YearMonth.of(2025, 11);
        LocalDate startDay = yearMonth.atDay(1);
        LocalDate endDay = yearMonth.atEndOfMonth();

        when(reservationJpaRepository.findByPickupDateBetween(startDay, endDay)).thenReturn(List.of());

        List<ReservationMonthReadResponse> result = reservationService.findByMonth(yearMonth);

        assertThat(result).isEmpty();
    }

    @Test
    void 특정날짜_예약조회_성공() {
        LocalDate date = LocalDate.of(2025, 11, 11);

        Reservation r = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .amount(5000)
                .pickupDate(date)
                .pickupTime(LocalTime.of(14, 0))
                .build();
        when(reservationJpaRepository.findByPickupDateOrderByPickupTimeAsc(date)).thenReturn(List.of(r));

        List<ReservationDayResponse> result = reservationService.getReservationDay(date);

        assertThat(result.get(0).pickupDate()).isEqualTo(date);
        assertThat(result.get(0).pickupTime()).isEqualTo(LocalTime.of(14, 0));
        assertThat(result.get(0).customerName()).isEqualTo("오종혁");
    }

    @Test
    void 특정날짜_예약없음() {
        LocalDate date = LocalDate.of(2025, 11, 11);

        when(reservationJpaRepository.findByPickupDateOrderByPickupTimeAsc(date)).thenReturn(List.of());

        List<ReservationDayResponse> result = reservationService.getReservationDay(date);

        assertThat(result).isEmpty();
    }

    @Test
    void 예약상세조회_존재하지않는경우_예외발생() {
        Long id = 999L;
        when(reservationJpaRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getReservationDetail(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약상세조회_정상동작() {
        Long id = 1L;
        Reservation r = Reservation.builder()
                .pickupDate(LocalDate.of(2025, 11, 10))
                .pickupTime(LocalTime.of(14, 0))
                .customerName("오종혁")
                .menu("브라우니")
                .amount(5000)
                .build();

        when(reservationJpaRepository.findById(id))
                .thenReturn(Optional.of(r));

        ReservationDetailResponse result = reservationService.getReservationDetail(id);

        assertThat(result.customerName()).isEqualTo("오종혁");
        assertThat(result.menu()).isEqualTo("브라우니");
        assertThat(result.pickupDate()).isEqualTo("2025-11-10");
        assertThat(result.pickupTime()).isEqualTo("14:00:00");
        assertThat(result.amount()).isEqualTo(5000);
    }

    @Test
    void updateReservation_존재하지_않는_id면_예외_발생() {
        Long id = 1L;
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                nowKst.toLocalDate().plusDays(1),
                LocalTime.of(12, 0),
                null, null, null,
                null, null, null, null
        );
        when(reservationJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.updateReservation(id, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

//    @Test
//    void updateReservation_픽업시간_검증_예외_발생() {
//        Long id = 1L;
//        Reservation reservation = Reservation.builder()
//                .id(id)
//                .pickupDate(nowKst.toLocalDate())
//                .pickupTime(nowKst.toLocalTime().plusHours(1))
//                .menu("쫀득쿠키")
//                .amount(7000)
//                .customerName("오종혁")
//                .customerPhone("010-1234-5678")
//                .build();
//        when(reservationJpaRepository.findById(id))
//                .thenReturn(Optional.of(reservation));
//
//        ReservationUpdateRequest request = new ReservationUpdateRequest(
//                nowKst.toLocalDate(),
//                nowKst.toLocalTime().minusHours(1),
//                null, null, null,
//                null, null, null, null
//        );
//
//        doThrow(IllegalArgumentException.class)
//                .when(reservationValidator).validateUpdatePickupTime(request);
//
//        assertThatThrownBy(() -> reservationService.updateReservation(id, request))
//                .isInstanceOf(IllegalArgumentException.class);
//    }

    @Test
    void updateReservation_예약_수정_성공(){
        Long id = 1L;
        Reservation reservation = Reservation.builder()
                .id(id)
                .pickupDate(nowKst.toLocalDate())
                .pickupTime(nowKst.toLocalTime().plusHours(1))
                .menu("쫀득쿠키")
                .amount(7000)
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .build();
        when(reservationJpaRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        ReservationUpdateRequest request = new ReservationUpdateRequest(
                null,null,
                null, null, "쫀득쿠키, 에그파이",
                12500, "메모 테스트", null, null
        );

        Reservation updatedReservation = reservationService.updateReservation(id, request);

        assertThat(updatedReservation.getMenu()).isEqualTo("쫀득쿠키, 에그파이");
        assertThat(updatedReservation.getAmount()).isEqualTo(12500);
        assertThat(updatedReservation.getMemo()).isEqualTo("메모 테스트");
    }

    @Test
    void deleteReservation_존재하지_않는_id로_삭제시_예외발생(){
        Long invalidId = 999L;
        when(reservationJpaRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.deleteReservation(invalidId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteReservation_존재하는_id로_삭제시_정상동작(){
        Long id = 1L;
        Reservation r = Reservation.builder()
                .id(id)
                .pickupDate(nowKst.toLocalDate())
                .pickupTime(nowKst.toLocalTime().plusHours(1))
                .customerName("오종혁")
                .menu("블루베리 케이크")
                .amount(7000)
                .customerPhone("010-1234-5678")
                .build();

        when(reservationJpaRepository.findById(id)).thenReturn(Optional.of(r));

        reservationService.deleteReservation(id);

        verify(reservationJpaRepository).delete(r);
    }
}