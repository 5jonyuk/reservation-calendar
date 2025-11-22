package com.myapp.reservation_calendar.config.jwt;

import com.myapp.reservation_calendar.user.User;
import com.myapp.reservation_calendar.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private TokenProvider tokenProvider;

    private static final String mockSecretKey = "asdfgqwer123rasdfasdrqwerqdfasdf!";

    @Test
    void generateToken_유저가_null_일_때_예외가_발생한다() {
        User user = null;
        Duration expiry = Duration.ofDays(14);

        // void 메소드에서 예외 던지기
        doThrow(new IllegalArgumentException("유저 정보가 존재하지 않습니다."))
                .when(userValidator).validate(user);

        // TokenProvider.generateToken 호출 시 userValidator.validate(user)에서 예외 발생
        assertThatThrownBy(() -> tokenProvider.generateToken(user, expiry))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유저 정보가 존재하지 않습니다.");

        verify(userValidator).validate(user);
    }

    @Test
    void generateToken_유저_정보와_만료기간을_전달해_토큰을_만들_수_있다() {
        when(jwtProperties.getSecretKey()).thenReturn(mockSecretKey);
        when(jwtProperties.getIssuer()).thenReturn("ohjonghyuk");
        tokenProvider.init(); // @PostConstruct 수동 호출

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getUsername()).thenReturn("testUser");

        String token = tokenProvider.generateToken(user, Duration.ofDays(14));

        assertThat(token).isNotNull();
        assertThat(tokenProvider.getUserId(token)).isEqualTo(1L);
    }

    @Test
    void getAuthentication_토큰으로부터_Authentication_객체를_반환한다() {
        when(jwtProperties.getSecretKey()).thenReturn(mockSecretKey);
        when(jwtProperties.getIssuer()).thenReturn("ohjonghyuk");
        tokenProvider.init(); // @PostConstruct 수동 호출

        String userName = "testUser";

        String token = JwtFactory.builder()
                .subject(userName)
                .build()
                .createToken(jwtProperties);

        Authentication auth = tokenProvider.getAuthentication(token);

        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("testUser");
        assertThat(auth.getAuthorities()).anyMatch(
                authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        assertThat(((UserDetails) auth.getPrincipal()).getUsername()).isEqualTo(userName);
    }

    @Test
    void validateToken_만료된_토큰일_때_false를_반환한다() {
        when(jwtProperties.getSecretKey()).thenReturn(mockSecretKey);
        when(jwtProperties.getIssuer()).thenReturn("ohjonghyuk");
        tokenProvider.init(); // @PostConstruct 수동 호출

        String expiredToken = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validateToken(expiredToken);

        assertThat(result).isFalse();
    }

    @Test
    void validateToken_유효한_토큰일_때_true를_반환한다() {
        when(jwtProperties.getSecretKey()).thenReturn(mockSecretKey);
        when(jwtProperties.getIssuer()).thenReturn("ohjonghyuk");
        tokenProvider.init(); // @PostConstruct 수동 호출

        String validToken = JwtFactory.withDefaultValues().createToken(jwtProperties);

        boolean result = tokenProvider.validateToken(validToken);

        assertThat(result).isTrue();
    }

    @Test
    void getUserId_토큰으로_유저_ID를_가져올_수_있다() {
        when(jwtProperties.getSecretKey()).thenReturn(mockSecretKey);
        when(jwtProperties.getIssuer()).thenReturn("ohjonghyuk");
        tokenProvider.init(); // @PostConstruct 수동 호출

        Long userId = 1L;

        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        Long result = tokenProvider.getUserId(token);

        assertThat(result).isEqualTo(userId);
    }
}