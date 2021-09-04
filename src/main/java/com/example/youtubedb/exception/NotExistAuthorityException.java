package com.example.youtubedb.exception;

public class NotExistAuthorityException extends RuntimeException {
    private final static String MESSAGE = "토큰 내 권한이 없습니다.";

    public NotExistAuthorityException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
