package com.example.youtubedb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class Token{

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiresIn;

    @Builder

    public Token(String grantType, String accessToken, String refreshToken, Date accessTokenExpiresIn) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }
}