package com.myapp.reservation_calendar.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private static final String ERROR_MESSAGE_LOGIN = "[ERROR] 사용자 정보가 맞지 않습니다.";
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String username, String rawPassword) {
        User user = userDetailService.loadUserByUsername(username);
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException(ERROR_MESSAGE_LOGIN);
        }
        return user;
    }
}
