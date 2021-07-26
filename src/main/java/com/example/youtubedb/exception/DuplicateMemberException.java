package com.example.youtubedb.exception;

public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super("ID가 중복된 회원입니다.");
    }
}
