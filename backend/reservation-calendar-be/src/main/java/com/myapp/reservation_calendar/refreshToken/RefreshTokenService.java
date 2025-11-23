package com.myapp.reservation_calendar.refreshToken;

import com.myapp.reservation_calendar.config.jwt.JwtProperties;
import com.myapp.reservation_calendar.config.jwt.TokenProvider;
import com.myapp.reservation_calendar.user.User;
import com.myapp.reservation_calendar.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String ERR_MESSAGE_INVALID_TOKEN = "유효하지 않거나 만료된 토큰 입니다.";
    private static final String ERR_MESSAGE_NOT_REFRESH_TOKEN_IN_DB = "DB에 저장되어 있지 않은 Refresh Token 입니다.";
    private static final String ERR_MESSAGE_ALREADY_LOGOUT = "이미 로그아웃 처리되었거나 존재하지 않는 토큰입니다.";

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;
    private final JwtProperties jwtProperties;

    @Transactional
    public void saveOrUpdate(Long userId, String newRefreshToken) {
        Optional<RefreshToken> OptionalRefreshToken = refreshTokenRepository.findByUserId(userId);

        if (OptionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = OptionalRefreshToken.get();
            refreshToken.update(newRefreshToken);
            refreshTokenRepository.save(refreshToken);
        } else {
            refreshTokenRepository.save(RefreshToken.of(userId, newRefreshToken));
        }
    }

    @Transactional
    public String reIssueAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException(ERR_MESSAGE_INVALID_TOKEN);
        }
        RefreshToken storedToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException(ERR_MESSAGE_NOT_REFRESH_TOKEN_IN_DB));

        Long userId = storedToken.getUserId();
        User user = userDetailService.loadUserByUserId(userId);

        return tokenProvider.generateToken(user, jwtProperties.getAccessTokenExpiration());
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException(ERR_MESSAGE_INVALID_TOKEN);
        }

        RefreshToken storedToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException(ERR_MESSAGE_ALREADY_LOGOUT));

        refreshTokenRepository.delete(storedToken);
    }
}