package com.example.youtubedb.config.jwt;

import java.time.LocalDateTime;

@FunctionalInterface
interface CurrentTimeServer {
  LocalDateTime now();
}
