package com.example.youtubedb.exception;

public class NotExistRequestValueException extends RuntimeException {
    public NotExistRequestValueException() {
        super("필요값이 없습니다.");
    }
}
