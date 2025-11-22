package com.myapp.reservation_calendar.login;

import com.myapp.reservation_calendar.config.jwt.TokenProvider;
import com.myapp.reservation_calendar.login.dto.LoginRequestDTO;
import com.myapp.reservation_calendar.user.User;
import com.myapp.reservation_calendar.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LoginController {
    private static final Duration ACCESS_TOKEN_EXPIRED_AT = Duration.ofMinutes(30);
    private static final String ERROR_MESSAGE_500 = "[ERROR] 서버 오류가 발생했습니다.";
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            User user = userService.authenticate(loginRequest.username(), loginRequest.password());
            String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_EXPIRED_AT);
            return ResponseEntity.ok(accessToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ERROR_MESSAGE_500);
        }
    }
}
