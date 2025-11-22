package com.myapp.reservation_calendar.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDetailService userDetailService;

    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User mockUser;
    private static final String mockUsername = "testUser";
    private static final String mockRawPassword = "raw_password";
    private static final String mockEncodedPassword = "encoded_password";


    @Test
    void authenticate_사용자이름과_패스워드가_일치하면_성공한다(){
        mockUser = User.builder()
                .username(mockUsername)
                .password(mockEncodedPassword)
                .build();
        when(userDetailService.loadUserByUsername(mockUsername)).thenReturn(mockUser);
        when(passwordEncoder.matches(mockRawPassword, mockEncodedPassword)).thenReturn(true);

        User result = userService.authenticate(mockUsername, mockRawPassword);

        assertThat(result).isNotNull().isEqualTo(mockUser);
        verify(userDetailService, times(1)).loadUserByUsername(mockUsername);
        verify(passwordEncoder, times(1)).matches(mockRawPassword, mockEncodedPassword);
    }

    @Test
    void authenticate_사용자이름과_패스워드가_일치하지_않으면_예외가_발생한다(){
        mockUser = User.builder()
                .username(mockUsername)
                .password(mockEncodedPassword)
                .build();
        when(userDetailService.loadUserByUsername(mockUsername)).thenReturn(mockUser);
        when(passwordEncoder.matches(mockRawPassword, mockEncodedPassword)).thenReturn(false);

        assertThatThrownBy(()-> userService.authenticate(mockUsername, mockRawPassword))
                .isInstanceOf(IllegalArgumentException.class);
        verify(userDetailService, times(1)).loadUserByUsername(mockUsername);
        verify(passwordEncoder, times(1)).matches(mockRawPassword, mockEncodedPassword);
    }
}