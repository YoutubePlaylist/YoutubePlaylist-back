package com.example.youtubedb.dto.token.resposne;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.domain.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TokenResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "토큰")
    private final Token response;

    public TokenResponseDto(Token response) {
        this.response = response;
    }
}