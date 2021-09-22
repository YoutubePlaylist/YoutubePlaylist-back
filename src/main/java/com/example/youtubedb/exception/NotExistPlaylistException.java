package com.example.youtubedb.exception;

public class NotExistPlaylistException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 플레이리스트입니다.";

    public NotExistPlaylistException() {
        super(MESSAGE);
    }
}
