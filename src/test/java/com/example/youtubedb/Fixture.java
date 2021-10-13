package com.example.youtubedb;

import com.example.youtubedb.config.jwt.time.ConstantTime;
import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.config.jwt.time.RealTime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Fixture {
  public static CurrentTimeServer oldTime() {
    return new ConstantTime(LocalDateTime.of(1998, 2, 25, 12, 12).toInstant(ZoneOffset.UTC));
  }

  public static CurrentTimeServer curTime() {
    return new RealTime();
  }
}
