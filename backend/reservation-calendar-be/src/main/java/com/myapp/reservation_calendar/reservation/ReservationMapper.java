package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationResponse;

public final class ReservationMapper {
    public static ReservationResponse toResponse(Reservation reservation){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getCustomerPhone(),
                reservation.getMenu(),
                reservation.getAmount(),
                reservation.getPickupDate(),
                reservation.getPickupTime(),
                reservation.getPaymentCompleted(),
                reservation.getPickupCompleted(),
                reservation.getCreatedAt(),
                reservation.getLastUpdatedAt()
        );
    }
}
