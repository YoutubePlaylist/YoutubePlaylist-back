package com.example.youtubedb.dto.error;

import com.example.youtubedb.dto.BaseResponseFailDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerErrorFailResponseDto extends BaseResponseFailDto {
    @Schema(description = "에러 상태 코드", defaultValue = "500")
    private int status;
    @Schema(description = "에러 메시지", defaultValue = "응답 실패 원인")
    private String message;

    @Builder
    public ServerErrorFailResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
