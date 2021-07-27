package com.example.youtubedb.exception;

public class NotExistPlaylistException extends RuntimeException {
    public NotExistPlaylistException() {
        super("존재하지 않는 플레이리스트입니다.");
    }
}
