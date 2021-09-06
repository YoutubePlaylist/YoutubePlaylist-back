package com.example.youtubedb.exception;

public class DuplicateMemberException extends RuntimeException {
    private static final String MESSAGE = "ID가 중복된 회원입니다.";

    public DuplicateMemberException() {
        super(MESSAGE);
    }
}
