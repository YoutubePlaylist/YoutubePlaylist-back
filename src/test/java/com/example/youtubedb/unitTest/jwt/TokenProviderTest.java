package com.example.youtubedb.unitTest.jwt;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    private final String SECRET = "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";
    private final TokenProvider tokenProvider = new TokenProvider(SECRET);

    @Spy
    Authentication authentication;

    @Test
    void 토큰_생성() {
        //given
        given(authentication.getName()).willReturn("userName");

        // when
        Token token = tokenProvider.generateTokenDto(authentication, true);
        System.out.println(token);



        // then
        assertThat(token.getAccessToken()).isNotNull();

        System.out.println(tokenProvider.parseClaims(token.getAccessToken()));
    }

//    private Authentication getCorrectAuthentication(){
//
//    }
}