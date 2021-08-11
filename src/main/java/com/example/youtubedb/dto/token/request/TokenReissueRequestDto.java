package com.example.youtubedb.dto.token.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenReissueRequestDto {
    private String accessToken;
    private String refreshToken;
    private Boolean isPC;

    @Builder
    public TokenReissueRequestDto(String accessToken, String refreshToken, Boolean isPC) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isPC = isPC;
    }
}