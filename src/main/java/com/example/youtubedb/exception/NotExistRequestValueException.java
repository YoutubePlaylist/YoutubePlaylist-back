package com.example.youtubedb.exception;

public class NotExistRequestValueException extends RuntimeException {
    private static final String MESSAGE = "필요값이 없습니다.";

    public NotExistRequestValueException() {
        super(MESSAGE);
    }
}
