package com.example.youtubedb.exception;

public class InvalidBlankPasswordException extends RuntimeException {
    private final static String MESSAGE = "비밀번호에 공백이 존재합니다.";

    public InvalidBlankPasswordException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
