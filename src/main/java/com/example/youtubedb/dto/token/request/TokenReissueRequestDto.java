package com.example.youtubedb.dto.token.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenReissueRequestDto {
    private String accessToken;
    private String refreshToken;
    private Boolean isPc;

    @Builder
    public TokenReissueRequestDto(String accessToken, String refreshToken, Boolean isPc) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isPc = isPc;
    }
}