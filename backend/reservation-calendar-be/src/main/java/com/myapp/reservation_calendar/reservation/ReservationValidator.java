package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationUpdateRequest;
import com.myapp.reservation_calendar.util.TimeConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ReservationValidator {

    public void validate(Reservation reservation) {
        validateCustomerName(reservation.getCustomerName());
        validateMenu(reservation.getMenu());
        validateCustomerPhone(reservation.getCustomerPhone());
        if (reservation.getPickupTime() != null) {
            validatePickupTimeKst(reservation.getPickupDate(), reservation.getPickupTime());
        }
        validateAmount(reservation.getAmount());
    }

    public void validateUpdatePickupTime(ReservationUpdateRequest request){
        if(request.pickupTime() != null && request.pickupDate() != null) {
            validatePickupTimeKst(request.pickupDate(), request.pickupTime());
        }
    }

    private void validateCustomerName(String customerName) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수 입력입니다.");
        }
    }

    private void validateMenu(String menu) {
        if (menu == null || menu.isBlank()) {
            throw new IllegalArgumentException("메뉴 이름은 필수 입력입니다.");
        }
    }

    private void validateCustomerPhone(String customerPhone) {
        if (customerPhone == null || customerPhone.isBlank()) {
            throw new IllegalArgumentException("예약자 번호는 필수 입력입니다.");
        }
        if (!customerPhone.matches("^010-\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException("연락처 형식이 올바르지 않습니다. 예) 010-1234-5678");
        }
    }

    private void validatePickupTimeKst(LocalDate pickupDate, LocalTime pickupTime) {
        LocalDateTime pickupDateTime = LocalDateTime.of(pickupDate, pickupTime);

        LocalDateTime nowKstLocal = TimeConverter.nowKst();

        if (pickupDateTime.isBefore(nowKstLocal)) {
            throw new IllegalArgumentException("예약 시간은 현재 시간 이후로 설정해야 합니다.");
        }
    }

    private void validateAmount(Integer amount) {
        if (amount == null) {
            throw new IllegalArgumentException("예약 금액은 빈 값일 수 없습니다.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("예약금액은 음수일 수 없습니다.");
        }
    }
}
