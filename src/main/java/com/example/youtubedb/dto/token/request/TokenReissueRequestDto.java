package com.example.youtubedb.dto.token.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TokenReissueRequestDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
    @NotNull
    private Boolean isPC;

    @Builder
    public TokenReissueRequestDto(String accessToken, String refreshToken, Boolean isPC) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isPC = isPC;
    }
}