package com.example.youtubedb.exception;

public class NotExistMemberException extends RuntimeException {
    public NotExistMemberException() {
        super("존재하지 않는 회원입니다.");
    }
}
