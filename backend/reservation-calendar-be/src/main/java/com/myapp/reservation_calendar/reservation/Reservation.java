package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Column(nullable = false)
    private Integer amount;

    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private Boolean paymentCompleted;
    private Boolean pickupCompleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    public static Reservation from(ReservationRequest request) {
        return Reservation.builder()
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .menu(request.menu())
                .amount(request.amount())
                .pickupDate(request.pickupDate())
                .pickupTime(request.pickupTime())
                .paymentCompleted(request.paymentCompleted())
                .pickupCompleted(request.pickupCompleted())
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
