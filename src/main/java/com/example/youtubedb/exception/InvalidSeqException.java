package com.example.youtubedb.exception;

public class InvalidSeqException extends RuntimeException {
    private final static String MESSAGE = "순서값이 올바르지 않습니다.";

    public InvalidSeqException() {
        super(MESSAGE);
    }
}
