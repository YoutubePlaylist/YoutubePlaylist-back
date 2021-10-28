package com.example.youtubedb.domain.token.refreshToken;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Mapping {
  private final Device pc;
  private final Device app;

  public Device device(boolean isPC) {
    if (isPC) {
      return pc;
    }
    return app;
  }
}
