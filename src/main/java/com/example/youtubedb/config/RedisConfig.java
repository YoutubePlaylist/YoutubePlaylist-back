package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.JwtConfigYamlAdapter;
import com.example.youtubedb.config.jwt.RealTime;
import com.example.youtubedb.config.jwt.TokenService2;
import com.example.youtubedb.dto.member.request.SecuredPassword;
import com.example.youtubedb.repository.MemberRepository;
import com.example.youtubedb.service.ChangingPassword;
import com.example.youtubedb.service.ChangingPassword.EvictCache;
import com.example.youtubedb.service.ChangingPassword.EvictCache2;
import com.example.youtubedb.service.ChangingPassword.Sequence;
import com.example.youtubedb.service.ChangingPassword.UpdateDatabase;
import com.example.youtubedb.service.UpdateDatabase;
import java.util.Arrays;
import javax.swing.plaf.basic.BasicMenuUI.ChangeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
  @Autowired
  JwtConfigYamlAdapter jwtConfigYamlAdapter;
  @Autowired
  PasswordEncoder passwordEncoder;
  @Value("${spring.redis.host}")
  private String redisHost;
  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {

    return new LettuceConnectionFactory();
  }

  @Bean
  public TokenService2 tokenProvider2() {
    return new TokenService2(new RealTime(), jwtConfigYamlAdapter.toJwtConfig());
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    return redisTemplate;
  }

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  StringRedisTemplate template;

  @Bean
  public ChangingPassword changingPassword() {
    final ChangingPassword db = new UpdateDatabase(memberRepository);
    final EvictCache cache = new EvictCache(template);

    return new Sequence(Arrays.asList(cache, db));
  }



  @Bean
  public ChangingPassword changingPassword2() {
    final ChangingPassword db = new UpdateDatabase(memberRepository);
    final ChangingPassword cache = new EvictCache2(db, template);

    return cache;
  }
}
