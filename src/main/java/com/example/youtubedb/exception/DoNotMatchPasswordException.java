package com.example.youtubedb.exception;

public class DoNotMatchPasswordException extends RuntimeException {
    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public DoNotMatchPasswordException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
