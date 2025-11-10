package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationCreateRequest;
import com.myapp.reservation_calendar.reservation.dto.ReservationCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ReservationCreateResponse createReservation(@RequestBody ReservationCreateRequest request){
        Reservation savedReservation = reservationService.createReservation(request);
        return ReservationMapper.toResponse(savedReservation);
    }
}
