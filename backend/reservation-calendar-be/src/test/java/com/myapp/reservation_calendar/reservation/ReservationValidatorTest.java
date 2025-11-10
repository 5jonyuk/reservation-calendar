package com.myapp.reservation_calendar.reservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationValidatorTest {
    ReservationValidator validator = new ReservationValidator();

    @Test
    void 정상적인_예약_정보는_예외_없이_통과된다(){
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("에그파이")
                .amount(5000)
                .build();

        assertThatCode(() -> validator.validate(reservation))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void 예약자의_이름이_빈_값이거나_공백이면_예외가_발생한다(String customerName) {
        Reservation reservation = Reservation.builder()
                .customerName(customerName)
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .amount(5500)
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void 예약메뉴가_빈_값이거나_공백이면_예외가_발생한다(String menu) {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu(menu)
                .amount(5500)
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void 예약자의_연락처가_빈_값이거나_공백이면_예외가_발생한다(String customerPhone) {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone(customerPhone)
                .menu("브라우니")
                .amount(5500)
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"01012341234", "010;5123;4567", "010 2000 3000"})
    void 예약자의_연락처가_형식에_맞지_않으면_예외가_발생한다(String customerPhone) {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone(customerPhone)
                .menu("브라우니")
                .amount(5500)
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 픽업_시간이_현재보다_이전이면_예외가_발생한다() {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .amount(5500)
                .pickupTime(LocalDateTime.now().minusHours(2))
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void 예약금액이_빈_값이거나_공백이면_예외가_발생한다(Integer amount) {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .amount(amount)
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -200})
    void 예약금액이_음수이면_예외가_발생한다(Integer amount) {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .amount(amount)
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 필수_값이_하나라도_누락되어있으면_예외가_발생한다(){
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }
}