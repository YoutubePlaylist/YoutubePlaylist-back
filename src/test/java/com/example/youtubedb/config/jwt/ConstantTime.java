package com.example.youtubedb.config.jwt;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ConstantTime implements CurrentTimeServer {
  private final LocalDateTime currentTime;

  @Override
  public LocalDateTime now() {
    return currentTime;
  }
}
