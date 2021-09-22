package com.example.youtubedb.dto.error;

import com.example.youtubedb.dto.BaseResponseFailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InvalidJwtFailResponseDto extends BaseResponseFailDto {
    private final int status;
    private final String message;

    @Builder
    public InvalidJwtFailResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
