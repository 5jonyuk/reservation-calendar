package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationCreateResponse;

public final class ReservationMapper {
    public static ReservationCreateResponse toResponse(Reservation reservation){
        return new ReservationCreateResponse(
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
