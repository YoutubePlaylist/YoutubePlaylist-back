package com.example.youtubedb.exception;

public class NotExistPlayException extends RuntimeException {
    private final static String MESSAGE = "해당 영상이 존재하지 않습니다.";

    public NotExistPlayException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
