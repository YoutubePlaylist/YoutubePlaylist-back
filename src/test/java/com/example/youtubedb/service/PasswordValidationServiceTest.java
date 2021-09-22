package com.example.youtubedb.service;

import com.example.youtubedb.exception.InvalidBlankPasswordException;
import com.example.youtubedb.exception.InvalidRegexPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordValidationServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    PasswordValidationService passwordValidationService;

    @Test
    void 비밀번호_정규식() {
        // given
        String invalidPassword = "password1234";

        // when, then
        assertThrows(InvalidRegexPasswordException.class, () -> passwordValidationService.checkValidPassword(invalidPassword));
    }

    @Test
    void 비밀번호_정규식_공백() {
        // given
        String invalidPassword = "password 1234*";

        // when, then
        assertThrows(InvalidBlankPasswordException.class, () -> passwordValidationService.checkValidPassword(invalidPassword));
    }
}