package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationValidator reservationValidator;

    @Transactional
    public Reservation createReservation(ReservationCreateRequest request){
        Reservation reservation = Reservation.from(request);
        reservationValidator.validate(reservation);
        return reservationJpaRepository.save(reservation);
    }
}
