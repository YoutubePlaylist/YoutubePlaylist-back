package com.example.youtubedb.exception;

public class OverNomMemberMaxListException extends RuntimeException {
    private static final String MESSAGE = "비회원 playlist 제한을 초과하였습니다.";
    public OverNomMemberMaxListException() {
        super(MESSAGE);
    }

    public static String getErrorMessage() {
        return MESSAGE;
    }
}
