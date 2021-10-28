package com.example.youtubedb.domain.token.accessToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessTokenConfig {
  @Bean
  AccessTokenProvider accessTokenProvider(CurrentTimeServer currentTimeServer) {
    return new AccessTokenProvider(currentTimeServer);
  }
}
