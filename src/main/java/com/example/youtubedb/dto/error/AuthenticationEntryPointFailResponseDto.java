package com.example.youtubedb.dto.error;

import com.example.youtubedb.dto.BaseResponseFailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
public class AuthenticationEntryPointFailResponseDto extends BaseResponseFailDto {
    @Schema(description = "에러 상태 코드", defaultValue = "401")
    private final int status;
    @Schema(description = "에러 메시지", defaultValue = "응답 실패 원인")
    private final String message;

    @Builder
    public AuthenticationEntryPointFailResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}