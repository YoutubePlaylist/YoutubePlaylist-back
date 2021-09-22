package com.example.youtubedb.config.jwt;

import java.time.LocalDateTime;

public class RealTime implements CurrentTimeServer {
  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }
}
