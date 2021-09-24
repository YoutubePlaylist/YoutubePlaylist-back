package com.example.youtubedb.unitTest.jwt;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.token.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    private final String SECRET = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";
    private final TokenProvider tokenProvider = new TokenProvider(SECRET);

    @Spy
    Authentication authentication;

    @Test
    void 토큰_생성() {
        // when
        Token token = tokenProvider.generateTokenDto(authentication, true);

        // then
        assertThat(token.getAccessToken()).isNotNull();
    }
}