package com.example.youtubedb.exception;

public class DoNotChangePasswordException extends RuntimeException {
    private static final String MESSAGE = "기존 비밀번호와 같은 비밀번호 입니다.";

    public DoNotChangePasswordException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
