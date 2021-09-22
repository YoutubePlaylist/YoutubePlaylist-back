package com.example.youtubedb.service;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.exception.DoNotChangePasswordException;
import com.example.youtubedb.exception.DoNotMatchPasswordException;
import com.example.youtubedb.exception.InvalidBlankPasswordException;
import com.example.youtubedb.exception.InvalidRegexPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PasswordValidationService {
    private final PasswordEncoder passwordEncoder;

    public void checkCorrectPassword(Member updateMember, String oldPassword, String newPassword) {
        if(!passwordEncoder.matches(oldPassword, updateMember.getPassword())){
            throw new DoNotMatchPasswordException();
        }
        if (oldPassword.equals(newPassword)) {
            throw new DoNotChangePasswordException();
        }
    }

    public void checkValidPassword(String password) {
        int min = 8;
        int max = 20;
        // 영어, 숫자, 특수문자 포함 min~max글자
        final String regex = "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + min + "," + max + "})$";
        // 공백 문자 정규식
        final String blankRegex = "(\\s)";

        Matcher matcher;

        // 공백 체크
        matcher = Pattern.compile(blankRegex).matcher(password);
        if (matcher.find()) {
            throw new InvalidBlankPasswordException();
        }

        // 정규식 체크
        matcher = Pattern.compile(regex).matcher(password);
        if (!matcher.find()) {
            throw new InvalidRegexPasswordException();
        }
    }
}
