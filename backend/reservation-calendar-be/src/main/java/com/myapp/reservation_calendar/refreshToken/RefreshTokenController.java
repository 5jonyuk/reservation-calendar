package com.myapp.reservation_calendar.refreshToken;

import com.myapp.reservation_calendar.refreshToken.dto.CreateAccessTokenRequest;
import com.myapp.reservation_calendar.refreshToken.dto.CreateAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class RefreshTokenController {
    private static final String ERR_MESSAGE_SERVER_ERR = "토큰 재발급 중 서버 오류가 발생했습니다.";
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueAccessToken(@RequestBody CreateAccessTokenRequest request) {
        try {
            String newAccessToken = refreshTokenService.reIssueAccessToken(request.refreshToken());

            return ResponseEntity.ok(new CreateAccessTokenResponse(newAccessToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ERR_MESSAGE_SERVER_ERR);
        }
    }
}
