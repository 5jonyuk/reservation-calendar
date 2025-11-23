package com.myapp.reservation_calendar.refreshToken;

import com.myapp.reservation_calendar.ApiResponse;
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
    public ResponseEntity<ApiResponse<?>> reissueAccessToken(@RequestBody CreateAccessTokenRequest request) {
        try {
            String newAccessToken = refreshTokenService.reIssueAccessToken(request.refreshToken());
            CreateAccessTokenResponse response = new CreateAccessTokenResponse(newAccessToken);

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(ERR_MESSAGE_SERVER_ERR));
        }
    }
}
