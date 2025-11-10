package com.myapp.reservation_calendar.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPickupDateBetween(LocalDate startDay, LocalDate endDay);
    Optional<Reservation> findById(Long id);
    List<Reservation> findByPickupDate(LocalDate pickupDate);
}
