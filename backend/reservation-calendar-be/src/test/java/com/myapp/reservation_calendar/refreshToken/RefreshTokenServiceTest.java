package com.myapp.reservation_calendar.refreshToken;

import com.myapp.reservation_calendar.config.jwt.JwtProperties;
import com.myapp.reservation_calendar.config.jwt.TokenProvider;
import com.myapp.reservation_calendar.user.User;
import com.myapp.reservation_calendar.user.UserDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private static final String ERR_MESSAGE_INVALID_TOKEN = "유효하지 않거나 만료된 Refresh Token 입니다.";
    private static final String ERR_MESSAGE_NOT_REFRESH_TOKEN_IN_DB = "DB에 저장되어 있지 않은 Refresh Token 입니다.";
    private final Long userId = 1L;
    private final String new_refreshToken = "new_refreshToken";
    private final String old_refreshToken = "old_refreshToken";
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserDetailService userDetailService;
    @Mock
    private JwtProperties jwtProperties;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void saveOrUpdate_기존토큰이_없을때_새로운_리프레시토큰을_저장해야_한다() {
        when(refreshTokenRepository.findByUserId(userId)).thenReturn(Optional.empty());

        refreshTokenService.saveOrUpdate(userId, new_refreshToken);

        verify(refreshTokenRepository, times(1)).findByUserId(userId);
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void saveOrUpdate_기존토큰이_있을때_토큰값을_갱신하고_저장해야_한다() {
        RefreshToken oldRefreshToken = RefreshToken.of(userId, old_refreshToken);
        when(refreshTokenRepository.findByUserId(userId))
                .thenReturn(Optional.of(oldRefreshToken));

        refreshTokenService.saveOrUpdate(userId, new_refreshToken);

        assertThat(oldRefreshToken.getRefreshToken()).isEqualTo(new_refreshToken);
        verify(refreshTokenRepository, times(1)).findByUserId(userId);
        verify(refreshTokenRepository, times(1)).save(oldRefreshToken);
    }

    @Test
    void reissueAccessToken_유효한_리프레시토큰으로_새_액세스토큰을_재발급해야_한다() {
        RefreshToken oldRefreshToken = RefreshToken.of(userId, old_refreshToken);
        User user = User.builder()
                .username("testUser")
                .password("testPassword")
                .build();
        when(tokenProvider.validateToken(old_refreshToken)).thenReturn(true);
        when(refreshTokenRepository.findByRefreshToken(old_refreshToken)).thenReturn(Optional.of(oldRefreshToken));
        when(userDetailService.loadUserByUserId(userId)).thenReturn(user);
        when(tokenProvider.generateToken(user, jwtProperties.getAccessTokenExpiration()))
                .thenReturn("new_access_token");

        String newAccessToken = refreshTokenService.reIssueAccessToken(old_refreshToken);

        assertThat(newAccessToken).isEqualTo("new_access_token");
        verify(tokenProvider, times(1)).validateToken(old_refreshToken);
        verify(refreshTokenRepository, times(1)).findByRefreshToken(old_refreshToken);
        verify(userDetailService, times(1)).loadUserByUserId(userId);
        verify(tokenProvider, times(1)).generateToken(user, jwtProperties.getAccessTokenExpiration());
    }

    @Test
    void reIssueAccessToken_유효하지_않은_토큰일경우_예외가_발생한다() {
        when(tokenProvider.validateToken(old_refreshToken)).thenReturn(false);

        assertThatThrownBy(() -> refreshTokenService.reIssueAccessToken(old_refreshToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERR_MESSAGE_INVALID_TOKEN);
        verify(refreshTokenRepository, never()).findByRefreshToken(old_refreshToken);
        verify(userDetailService, never()).loadUserByUserId(userId);
    }

    @Test
    void reIssueAccessToken_DB에_리프레시_토큰이_없을경우_예외가_발생한다(){
        when(tokenProvider.validateToken(old_refreshToken)).thenReturn(true);
        when(refreshTokenRepository.findByRefreshToken(old_refreshToken)).thenReturn(Optional.empty());

        assertThatThrownBy(()->refreshTokenService.reIssueAccessToken(old_refreshToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERR_MESSAGE_NOT_REFRESH_TOKEN_IN_DB);
        verify(tokenProvider, times(1)).validateToken(old_refreshToken);
        verify(userDetailService, never()).loadUserByUserId(userId);
    }
}