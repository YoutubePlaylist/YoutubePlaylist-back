package com.example.youtubedb.exception;

public class InvalidAccessException extends RuntimeException {
    private static final String MESSAGE = "올바르지 못한 접근입니다.";

    public InvalidAccessException() {
        super(MESSAGE);
    }
}
