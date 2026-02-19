package com.myapp.reservation_calendar.reservation;

import com.myapp.reservation_calendar.reservation.dto.ReservationUpdateRequest;
import com.myapp.reservation_calendar.util.TimeConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationValidatorTest {
    private static final String ERROR_MESSAGE_RESERVATION_PERSON_NAME_ESSENTIAL = "[ERROR] 예약자 이름은 필수 입력입니다.";
    private static final String ERROR_MESSAGE_MENU_ESSENTIAL = "[ERROR] 메뉴 이름은 필수 입력입니다.";
    private static final String ERROR_MESSAGE_RESERVATION_PERSON_PHONE_ESSENTIAL = "[ERROR] 예약자 번호는 필수 입력입니다.";
    private static final String ERROR_MESSAGE_INVALID_PHONE_REGEX = "[ERROR] 연락처 형식이 올바르지 않습니다. 예) 010-1234-5678";
    private static final String ERROR_MESSAGE_RESERVATION_TIME = "[ERROR] 예약 시간은 현재 시간 이후로 설정해야 합니다.";
    private static final String ERROR_MESSAGE_RESERVATION_AMOUNT_NOT_NULL = "[ERROR] 예약 금액은 빈 값일 수 없습니다.";
    private static final String ERROR_MESSAGE_RESERVATION_AMOUNT_NOT_NEGATIVE = "[ERROR] 예약금액은 음수일 수 없습니다.";

    ReservationValidator validator = new ReservationValidator();
    LocalDateTime nowKst = TimeConverter.nowKst();

    @Test
    void 정상적인_예약_정보는_예외_없이_통과된다() {
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
                .pickupDate(nowKst.toLocalDate())
                .pickupTime(nowKst.toLocalTime().minusHours(2))
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
    void 필수_값이_하나라도_누락되어있으면_예외가_발생한다() {
        Reservation reservation = Reservation.builder()
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .menu("브라우니")
                .build();

        assertThatThrownBy(() -> validator.validate(reservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 수정한_예약이_정상적이면_예약_수정에_성공한다() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .menu("브라우니")
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .amount(5000)
                .pickupTime(nowKst.toLocalTime().plusHours(1))
                .pickupDate(nowKst.toLocalDate().plusDays(1))
                .paymentCompleted(false)
                .pickupCompleted(false)
                .build();

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                nowKst.toLocalDate().plusDays(1),
                nowKst.toLocalTime().plusHours(1),
                "오종혁", "010-1234-1234", "에그파이",
                5000, "메모 테스트", true, true
        );

        reservation.updateFrom(updateRequest);

        assertThatCode(() -> validator.updateValidate(updateRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_수정할_땐_현재보다_과거여도_수정에_성공한다() {
        Reservation reservation = Reservation.builder()
                .id(1L)
                .menu("브라우니")
                .customerName("오종혁")
                .customerPhone("010-1234-5678")
                .amount(5000)
                .pickupTime(nowKst.toLocalTime().plusHours(1))
                .pickupDate(nowKst.toLocalDate().plusDays(1))
                .paymentCompleted(false)
                .pickupCompleted(false)
                .build();

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                nowKst.toLocalDate().minusDays(1),
                nowKst.toLocalTime().minusHours(1),
                "오종혁", "010-1234-1234", "에그파이",
                5000, null, true, true
        );

        reservation.updateFrom(updateRequest);

        assertThatCode(() -> validator.updateValidate(updateRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void 수정한_예약의_예약자_이름이_빈_값이면_예외가_발생한다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                nowKst.toLocalDate().plusDays(1),
                nowKst.toLocalTime().plusHours(1),
                null, "010-1234-1234", "에그파이",
                5500, null, true, true
        );
        assertThatThrownBy(() -> validator.updateValidate(updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MESSAGE_RESERVATION_PERSON_NAME_ESSENTIAL);
    }

    @Test
    void 수정한_예약의_메뉴가_빈_값이면_예외가_발생한다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                nowKst.toLocalDate().plusDays(1),
                nowKst.toLocalTime().plusHours(1),
                "오종혁", "010-1234-1234", null,
                5500, null, true, true
        );
        assertThatThrownBy(() -> validator.updateValidate(updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MESSAGE_MENU_ESSENTIAL);
    }

    @Test
    void 수정한_예약의_예약자_번호가_빈_값이면_예외가_발생한다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                nowKst.toLocalDate().plusDays(1),
                nowKst.toLocalTime().plusHours(1),
                "오종혁", null, "에그파이",
                5500, null, true, true
        );
        assertThatThrownBy(() -> validator.updateValidate(updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MESSAGE_RESERVATION_PERSON_PHONE_ESSENTIAL);
    }

    @Test
    void 수정한_예약의_예약_금액이_빈_값이면_예외가_발생한다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                nowKst.toLocalDate().plusDays(1),
                nowKst.toLocalTime().plusHours(1),
                "오종혁", "010-1234-1234", "에그파이",
                null, null, true, true
        );
        assertThatThrownBy(() -> validator.updateValidate(updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MESSAGE_RESERVATION_AMOUNT_NOT_NULL);
    }
}
