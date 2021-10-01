package com.example.youtubedb.vo;

import lombok.Value;

@Value
public class TokenVO {
  String accessToken;
  String refreshToken;
}
