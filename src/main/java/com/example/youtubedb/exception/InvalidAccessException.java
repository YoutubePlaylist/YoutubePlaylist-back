package com.example.youtubedb.exception;

public class InvalidAccessException extends RuntimeException {
    public InvalidAccessException() {
        super("올바르지 못한 접근입니다.");
    }
}
