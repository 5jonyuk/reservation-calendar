package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.*;

public final class ReservationMapper {
    private ReservationMapper() {
    }

    public static ReservationCreateResponse toCreateResponse(Reservation reservation) {
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

    public static ReservationMonthReadResponse toMonthReadResponse(Reservation reservation) {
        return new ReservationMonthReadResponse(
                reservation.getPickupDate(),
                reservation.getPickupTime()
        );
    }

    public static ReservationDetailResponse toDetailResponse(Reservation reservation) {
        return new ReservationDetailResponse(
                reservation.getPickupDate(),
                reservation.getPickupTime(),
                reservation.getCustomerName(),
                reservation.getCustomerPhone(),
                reservation.getMenu(),
                reservation.getAmount(),
                reservation.getPaymentCompleted(),
                reservation.getPickupCompleted(),
                reservation.getCreatedAt(),
                reservation.getLastUpdatedAt()
        );
    }

    public static ReservationDayResponse toDayResponse(Reservation reservation) {
        return new ReservationDayResponse(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getPickupDate(),
                reservation.getPickupTime()
        );
    }

    public static ReservationUpdateResponse toUpdateResponse(Reservation reservation) {
        return new ReservationUpdateResponse(
                reservation.getId(),
                reservation.getPickupDate(),
                reservation.getPickupTime(),
                reservation.getCustomerName(),
                reservation.getCustomerPhone(),
                reservation.getMenu(),
                reservation.getAmount(),
                reservation.getPaymentCompleted(),
                reservation.getPickupCompleted(),
                reservation.getLastUpdatedAt()
        );
    }
}
