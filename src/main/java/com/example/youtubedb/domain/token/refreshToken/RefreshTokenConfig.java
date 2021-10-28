package com.example.youtubedb.domain.token.refreshToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefreshTokenConfig {
  @Bean
  public Mapping mapping(CurrentTimeServer currentTimeServer) {
    return new Mapping(new PC(currentTimeServer), new App(currentTimeServer));
  }

  @Bean
  RefreshTokenProvider refreshTokenProvider(CurrentTimeServer currentTimeServer) {
    return new RefreshTokenProvider(currentTimeServer);
  }
}
