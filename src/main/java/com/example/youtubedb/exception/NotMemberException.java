package com.example.youtubedb.exception;

public class NotMemberException extends RuntimeException {
    private static final String MESSAGE = "회원이 아닌 비회원입니다.";

    public NotMemberException() {
        super(MESSAGE);
    }
}
