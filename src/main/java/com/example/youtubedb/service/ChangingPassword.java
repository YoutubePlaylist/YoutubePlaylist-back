package com.example.youtubedb.service;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.member.request.ChangingPasswordRequest;
import com.example.youtubedb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

public interface ChangingPassword {
  void changePassword(ChangingPasswordRequest request);

  interface CacheClient {
    void delete(String key);
  }

  @RequiredArgsConstructor
  class UpdateDatabase implements ChangingPassword {
    private final MemberRepository memberRepository;

    @Override
    public void changePassword(ChangingPasswordRequest request) {
      final Member member = memberRepository
        .findByLoginId(request.getMemberId())
        .orElseThrow(RuntimeException::new);

      member.setPassword(request.getNewPassword().getPassword());
      memberRepository.save(member);
    }
  }

  @RequiredArgsConstructor
  class EvictCache implements ChangingPassword {
    private final ChangingPassword delegate;
    private final CacheClient cacheClient;

    @Override
    public void changePassword(ChangingPasswordRequest request) {
      try {
        delegate.changePassword(request);

        cacheClient.delete("PC" + request.getMemberId());
        cacheClient.delete("APP" + request.getMemberId());
      } catch (Exception e) {
        throw e;
      }
    }
  }

  @RequiredArgsConstructor
  class RetryableDB implements ChangingPassword {
    private final int maxAttempt;
    private final ChangingPassword delegate;

    @Override
    public void changePassword(ChangingPasswordRequest request) {
      final RuntimeException acc = new RuntimeException();

      for(int i=0; i<maxAttempt; i++) {
        try {
          delegate.changePassword(request);
          return;
        } catch (Exception e) {
          acc.addSuppressed(e);
        }
      }
      throw acc;
    }
  }

  @RequiredArgsConstructor
  class RetryableCache implements CacheClient {
    private final int maxAttempt;
    private final CacheClient cacheClient;

    @Override
    public void delete(String key) {
      final RuntimeException acc = new RuntimeException();

      for (int i = 0; i < maxAttempt; i++) {
        try {
          cacheClient.delete(key);
          return;
        } catch (Exception e) {
          acc.addSuppressed(e);
        }
      }
      throw acc;
    }
  }

  @RequiredArgsConstructor
  class StringRedis implements CacheClient {
    private final StringRedisTemplate template;

    @Override
    public void delete(String key) {
      template.delete(key);
    }
  }
}
