package com.example.youtubedb.config.jwt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.youtubedb.config.jwt.TokenProvider2.Token2;
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
        final TokenProvider2 tokenProvider = new TokenProvider2();
        final Token2 token = tokenProvider.create();

        assertThat(token.loginId(), is("id-123"));
        assertThat(token.expirationAt(), is(LocalDateTime.now().plus(Duration.ofMinutes(30))));
        assertThat(token.signatureAlgorithm(), is(SignatureAlgorithm.HS512));
    }
}