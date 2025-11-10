package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private String menu;

    private LocalDateTime pickupTime;
    private LocalDateTime dropTime;
    private boolean paymentCompleted;
    private boolean pickupCompleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    public static Reservation from(ReservationRequest request) {
        return Reservation.builder()
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .menu(request.menu())
                .pickupTime(request.pickupTime())
                .dropTime(request.dropTime())
                .paymentCompleted(request.paymentCompleted())
                .pickupCompleted(request.pickupCompleted())
                .build();
    }
}
