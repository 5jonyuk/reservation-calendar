package com.myapp.reservation_calendar.refreshToken;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {
    private final Long userId = 1L;
    private final String initial_refreshToken = "initial_refreshToken";
    private final String new_refreshToken = "new_refreshToken";
    @Test
    void of_Refresh_토큰_객체를_정확하게_생성해야_한다(){
        RefreshToken refreshToken = RefreshToken.of(userId, initial_refreshToken);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getRefreshToken()).isEqualTo("initial_refreshToken");
        assertThat(refreshToken.getUserId()).isEqualTo(1L);
        assertThat(refreshToken.getId()).isNull(); // ID는 DB 저장 후 할당되므로 null이 맞음
    }

    @Test
    void update_새로운_리프레시_토큰으로_값을_변경한다(){
        RefreshToken refreshToken = RefreshToken.of(userId, initial_refreshToken);
        refreshToken.update(new_refreshToken);

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getRefreshToken()).isEqualTo("new_refreshToken");
        assertThat(refreshToken.getUserId()).isEqualTo(1L);
        assertThat(refreshToken.getId()).isNull();
    }
}