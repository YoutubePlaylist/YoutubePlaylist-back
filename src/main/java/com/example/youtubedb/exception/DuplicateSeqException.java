package com.example.youtubedb.exception;

public class DuplicateSeqException extends RuntimeException {
    private final static String MESSAGE = "순서가 중복됩니다.";

    public DuplicateSeqException() {
        super(MESSAGE);
    }
}
