package com.example.youtubedb.domain.token.refreshToken;

import com.example.youtubedb.config.jwt.time.RealTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefreshTokenConfig {
  @Bean
  public Mapping mapping() {
    return new Mapping(new PC(new RealTime()), new App(new RealTime()));
  }
}
