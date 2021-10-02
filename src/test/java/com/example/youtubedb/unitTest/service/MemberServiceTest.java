package com.example.youtubedb.unitTest.service;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.exception.InvalidBlankPasswordException;
import com.example.youtubedb.exception.InvalidRegexPasswordException;
import com.example.youtubedb.repository.interfaces.MemberRepository;
import com.example.youtubedb.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private StringRedisTemplate template;
    @Mock
    private ValueOperations<String, String> stringStringValueOperations;

    @Test
    void 비밀번호_유효성검사_정규식() {
        try {
            // given
            Method method = memberService.getClass().getDeclaredMethod("checkValidPassword", String.class);
            method.setAccessible(true);
            String password = "password1234";

            // when
            InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(memberService, password));

            // then
            assertThat(exception).hasCauseInstanceOf(InvalidRegexPasswordException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void 비밀번호_유효성검사_공백() {
        try {
            // given
            Method method = memberService.getClass().getDeclaredMethod("checkValidPassword", String.class);
            method.setAccessible(true);
            String password = "password 1234!";

            // when
            InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(memberService, password));

            // then
            assertThat(exception).hasCauseInstanceOf(InvalidBlankPasswordException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
