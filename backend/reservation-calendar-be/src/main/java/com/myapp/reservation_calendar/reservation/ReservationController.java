package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationRequest;
import com.myapp.reservation_calendar.reservation.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse createReservation(ReservationRequest request){
        Reservation savedReservation = reservationService.createReservation(request);
        return ReservationMapper.toResponse(savedReservation);
    }
}
