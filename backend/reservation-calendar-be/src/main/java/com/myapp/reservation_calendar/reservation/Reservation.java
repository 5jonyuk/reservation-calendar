package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationCreateRequest;
import com.myapp.reservation_calendar.reservation.dto.ReservationUpdateRequest;
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

    public static Reservation from(ReservationCreateRequest request) {
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

    public void updateFrom(ReservationUpdateRequest req) {
        if (req.pickupDate() != null) this.pickupDate = req.pickupDate();
        if (req.pickupTime() != null) this.pickupTime = req.pickupTime();
        if (req.customerName() != null) this.customerName = req.customerName();
        if (req.customerPhone() != null) this.customerPhone = req.customerPhone();
        if (req.menu() != null) this.menu = req.menu();
        if (req.amount() != null) this.amount = req.amount();
        if (req.paymentCompleted() != null) this.paymentCompleted = req.paymentCompleted();
        if (req.pickupCompleted() != null) this.pickupCompleted = req.pickupCompleted();
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
