package com.myapp.reservation_calendar.reservation;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationValidator {

    public void validate(Reservation reservation){
        validateCustomerName(reservation.getCustomerName());
        validateMenu(reservation.getMenu());
        validateCustomerPhone(reservation.getCustomerPhone());
        validatePickupTime(reservation.getPickupTime());
    }

    private void validateCustomerName(String customerName){
        if(customerName != null && customerName.isBlank()){
            throw new IllegalArgumentException("예약자 이름은 필수 입력입니다.");
        }
    }

    private void validateMenu(String menu){
        if(menu != null && menu.isBlank()){
            throw new IllegalArgumentException("메뉴 이름은 필수 입력입니다.");
        }
    }

    private void validateCustomerPhone(String customerPhone){
        if(customerPhone == null || customerPhone.isBlank()){
            throw new IllegalArgumentException("예약자 번호는 필수 입력입니다.");
        }
        if(!customerPhone.matches("^010-\\d{4}-\\d{4}$")){
            throw new IllegalArgumentException("연락처 형식이 올바르지 않습니다. 예) 010-1234-5678");
        }
    }

    private void validatePickupTime(LocalDateTime pickupTime){
        if(pickupTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("픽업 시간은 현재 이후여야 합니다.");
        }
    }
}
