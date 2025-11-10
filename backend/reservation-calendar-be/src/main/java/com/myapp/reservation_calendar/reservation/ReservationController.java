package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ReservationCreateResponse createReservation(@RequestBody ReservationCreateRequest request) {
        Reservation savedReservation = reservationService.createReservation(request);
        return ReservationMapper.toCreateResponse(savedReservation);
    }

    @GetMapping("/month")
    public List<ReservationMonthReadResponse> findByMonth(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        return reservationService.findByMonth(yearMonth);
    }

    @GetMapping("/date")
    public List<ReservationDayResponse> findByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reservationService.getReservationDay(date);
    }

    @GetMapping("/{id}")
    public ReservationDetailResponse getReservationDetail(@PathVariable Long id) {
        return reservationService.getReservationDetail(id);
    }
}
