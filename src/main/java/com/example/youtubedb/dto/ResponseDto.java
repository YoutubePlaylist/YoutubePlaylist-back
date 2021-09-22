package com.example.youtubedb.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDto {
    private final boolean success;
    private final Object response;
    private final ErrorDto error;
}
