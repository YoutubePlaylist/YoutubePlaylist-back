package com.example.youtubedb.exception;

public class InvalidRegexPasswordException extends RuntimeException {
    private final static String MESSAGE = "비밀번호 규칙을 지키지 못했습니다.";

    public InvalidRegexPasswordException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
