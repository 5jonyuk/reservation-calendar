package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.ApiResponse;
import com.myapp.reservation_calendar.reservation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private static final String ERR_MESSAGE_SERVER_ERROR = "[ERROR] 서버오류가 발생했습니다. 관리자에게 문의해주세요.";

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createReservation(@RequestBody ReservationCreateRequest request) {
        try {
            Reservation savedReservation = reservationService.createReservation(request);
            ReservationCreateResponse response = ReservationMapper.toCreateResponse(savedReservation);

            return ResponseEntity.ok(ApiResponse.success(response, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(ERR_MESSAGE_SERVER_ERROR));
        }
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

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationUpdateRequest request) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, request);
            ReservationUpdateResponse response = ReservationMapper.toUpdateResponse(updatedReservation);

            return ResponseEntity.ok(ApiResponse.success(response, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(ERR_MESSAGE_SERVER_ERROR));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteReservation(
            @PathVariable Long id
    ) {
        try {
            reservationService.deleteReservation(id);
            ReservationDeleteResponse response = ReservationMapper.toDeleteResponse(id);

            return ResponseEntity.ok(ApiResponse.success(response, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(ERR_MESSAGE_SERVER_ERROR));
        }
    }
}
