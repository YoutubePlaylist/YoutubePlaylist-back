package com.example.youtubedb.config.jwt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.youtubedb.config.jwt.TokenProvider2.CurrentTimeServer;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TokenProvider2Test {
    @Test
    void test1() {
        // 구현을 먼저 생각하지 않는게 중요하다
        // 클라이언트 입장 interface를 먼저 생각한다
        //
        final TokenProvider2 tokenProvider = new TokenProvider2(LocalDateTime::now);
        final Token2 token = tokenProvider.create("id-123");

        final LocalDateTime plus = LocalDateTime.now().plus(Duration.ofMinutes(30));
        assertThat(token.loginId(), is("id-123"));
        assertThat(token.expirationAt(), is(plus));
        assertThat(token.signatureAlgorithm(), is(SignatureAlgorithm.HS512));
    }

    @Test
    void 토큰_만료_시간은_항상_동일하다() {
        // 구현을 먼저 생각하지 않는게 중요하다
        // 클라이언트 입장 interface를 먼저 생각한다

        // 테스트의 어려운 점
        // -> 의존성을 관리하는게 어렵다
        // 현재 시각, local 시각 의존성
        //
        // 문제점을 마주했다
        //
        //
        // SOLID : DIP dependency inversion principle
        //  - DIP는 추상화에 의존해라

        final LocalDateTime now = LocalDateTime.now();
        final CurrentTimeServer fakeTimeServer = () -> now;


        final TokenProvider2 tokenProvider = new TokenProvider2(fakeTimeServer);
        final Token2 token = tokenProvider.create("id-123");

        final LocalDateTime plus = now.plus(Duration.ofMinutes(30));
        assertThat(token.expirationAt(), is(plus));
        assertThat(token.expirationAt(), is(plus));
        assertThat(token.expirationAt(), is(plus));
    }
}