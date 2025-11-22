package com.myapp.reservation_calendar.user;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validate(User user) {
        validateUser(user);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("유저정보가 존재하지 않습니다.");
        }
    }
}
