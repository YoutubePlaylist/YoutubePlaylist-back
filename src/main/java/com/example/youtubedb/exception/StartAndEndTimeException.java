package com.example.youtubedb.exception;

public class StartAndEndTimeException extends RuntimeException {
    private static final String MESSAGE = "시간 설정이 바르지 않습니다.";
    public StartAndEndTimeException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
