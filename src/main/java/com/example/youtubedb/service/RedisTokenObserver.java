package com.example.youtubedb.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenObserver implements RefreshTokenObserver<String, String> {
  private final StringRedisTemplate template;
  private final ValueOperations<String, String> operations;

  public RedisTokenObserver(StringRedisTemplate template) {
    this.template = template;
    this.operations = template.opsForValue();
  }

  @Override
  public void update(String key, String value) {
    operations.set(key, value);
  }

  @Override
  public void delete(String key) {
    template.delete(key);
  }

  @Override
  public String getValue(String key) {
    return operations.get(key);
  }
}
