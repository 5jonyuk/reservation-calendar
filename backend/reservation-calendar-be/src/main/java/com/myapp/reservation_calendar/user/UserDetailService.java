package com.myapp.reservation_calendar.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private static final String ERROR_MESSAGE_NULL_USER = "[ERROR] 해당 유저가 존재하지 않습니다.";
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NULL_USER));
    }
}
