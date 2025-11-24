package com.myapp.reservation_calendar.login;

import com.myapp.reservation_calendar.ApiResponse;
import com.myapp.reservation_calendar.config.jwt.TokenProvider;
import com.myapp.reservation_calendar.login.dto.LoginRequest;
import com.myapp.reservation_calendar.login.dto.LoginResponse;
import com.myapp.reservation_calendar.refreshToken.RefreshTokenService;
import com.myapp.reservation_calendar.user.User;
import com.myapp.reservation_calendar.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LoginControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenService refreshTokenService;
    @InjectMocks
    private LoginController loginController;

    private User mockUser;
    private static final String mockUsername = "testUser";
    private static final String mockPassword = "encoded_password";
    private static final String mockToken = "token";
    private static final String mockInvalidToken = "invalid_token";
    private static final Duration ACCESS_TOKEN_EXPIRED_AT = Duration.ofMinutes(30);
    private static final String errorMessage_401 = "[ERROR] 사용자 정보가 맞지 않습니다.";
    private static final String errorMessage_500 = "[ERROR] 서버 오류가 발생했습니다.";

    @Test
    void login_유효한_사용자가_토큰을_발급_성공시_200과_토큰_본문을_반환한다(){
        LoginRequest loginRequest = new LoginRequest(mockUsername, mockPassword);
        mockUser = User.builder()
                .username(mockUsername)
                .password(mockPassword)
                .build();

        when(userService.authenticate(mockUsername, mockPassword)).thenReturn(mockUser);
        when(tokenProvider.generateToken(mockUser, ACCESS_TOKEN_EXPIRED_AT)).thenReturn(mockToken);
        when(tokenProvider.generateRefreshToken(mockUser)).thenReturn(mockInvalidToken);
        doNothing().when(refreshTokenService).saveOrUpdate(anyLong(), anyString());

        ResponseEntity<ApiResponse<?>> response = loginController.login(loginRequest);
        ApiResponse<?> responseBody = response.getBody();


        assertThat(responseBody).isNotNull();
        LoginResponse loginResponse = (LoginResponse) responseBody.data();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody.success()).isTrue();
        assertThat(loginResponse.accessToken()).isEqualTo(mockToken);
        assertThat(loginResponse.refreshToken()).isEqualTo(mockInvalidToken);

        verify(userService, times(1)).authenticate(mockUsername, mockPassword);
        verify(tokenProvider, times(1)).generateToken(mockUser, ACCESS_TOKEN_EXPIRED_AT);
        verify(refreshTokenService, times(1)).saveOrUpdate(eq(mockUser.getId()), eq(mockInvalidToken));
    }

    @Test
    void login_로그인_정보가_불일치하면_401_에러코드와_예외가_발생한다(){
        LoginRequest loginRequest = new LoginRequest(mockUsername, mockPassword);
        mockUser = User.builder()
                .username(mockUsername)
                .password(mockPassword)
                .build();
        when(userService.authenticate(mockUsername, mockPassword))
                .thenThrow(new IllegalArgumentException(errorMessage_401));

        ResponseEntity<ApiResponse<?>> response = loginController.login(loginRequest);
        ApiResponse<?> responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseBody.message()).isEqualTo(errorMessage_401);
        assertThat(responseBody.success()).isFalse();
        verify(userService, times(1)).authenticate(mockUsername, mockPassword);
        verify(tokenProvider, never()).generateToken(mockUser, ACCESS_TOKEN_EXPIRED_AT);
    }

    @Test
    void login_예기치_못한_서버오류가_발생하면_500코드와_예외가_발생한다(){
        LoginRequest loginRequest = new LoginRequest(mockUsername, mockPassword);
        mockUser = User.builder()
                .username(mockUsername)
                .password(mockPassword)
                .build();

        when(userService.authenticate(mockUsername, mockPassword))
                .thenThrow(new RuntimeException(errorMessage_500));

        ResponseEntity<ApiResponse<?>> response = loginController.login(loginRequest);
        ApiResponse<?> responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseBody.message()).isEqualTo(errorMessage_500);
        verify(userService, times(1)).authenticate(mockUsername, mockPassword);
        verify(tokenProvider, never()).generateToken(mockUser, ACCESS_TOKEN_EXPIRED_AT);
    }
}