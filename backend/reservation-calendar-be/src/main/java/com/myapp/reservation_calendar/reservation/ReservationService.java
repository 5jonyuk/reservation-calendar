package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationCreateRequest;
import com.myapp.reservation_calendar.reservation.dto.ReservationDayResponse;
import com.myapp.reservation_calendar.reservation.dto.ReservationDetailResponse;
import com.myapp.reservation_calendar.reservation.dto.ReservationMonthReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationValidator reservationValidator;

    @Transactional
    public Reservation createReservation(ReservationCreateRequest request) {
        Reservation reservation = Reservation.from(request);
        reservationValidator.validate(reservation);
        return reservationJpaRepository.save(reservation);
    }

    public List<ReservationMonthReadResponse> findByMonth(YearMonth yearMonth) {
        LocalDate startDay = yearMonth.atDay(1);
        LocalDate endDay = yearMonth.atEndOfMonth();

        return reservationJpaRepository.findByPickupDateBetween(startDay, endDay)
                .stream()
                .map(ReservationMapper::toMonthReadResponse)
                .toList();
    }

    public ReservationDetailResponse getReservationDetail(Long id) {
        Reservation reservation = reservationJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 없습니다."));
        return ReservationMapper.toDetailResponse(reservation);
    }

    public List<ReservationDayResponse> getReservationDay(LocalDate date) {
        return reservationJpaRepository.findByPickupDate(date)
                .stream()
                .map(ReservationMapper::toDayResponse)
                .toList();
    }

}
