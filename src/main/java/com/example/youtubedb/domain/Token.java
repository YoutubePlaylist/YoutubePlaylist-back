package com.example.youtubedb.domain;

import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
public class Token{

    private String grantType;
    private String accessToken;
    @Setter
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