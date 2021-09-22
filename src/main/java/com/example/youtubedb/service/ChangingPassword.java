package com.example.youtubedb.service;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.member.request.ChangingPasswordRequest;
import com.example.youtubedb.repository.MemberRepository;
import java.util.List;
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
        .findByLoginId(request.memberId())
        .orElseThrow(RuntimeException::new);

      member.setPassword(request.newPassword().toString());
      memberRepository.save(member);
    }
  }

  @RequiredArgsConstructor
  class EvictCache implements ChangingPassword {
    private final StringRedisTemplate template;

    @Override
    public void changePassword(ChangingPasswordRequest request) {
      template.delete("PC" + request.memberId());
      template.delete("APP" + request.memberId());
    }
  }

  // 1. cache는 지워졌는데 db는 연산 실패
  // 2. db는 연산 성공했는데 cache는 삭제 실패

  @RequiredArgsConstructor
  class Sequence implements ChangingPassword {
    private final List<ChangingPassword> changingPasswords;

    @Override
    public void changePassword(ChangingPasswordRequest request) {
      for (ChangingPassword changingPassword : changingPasswords) {
        changingPassword.changePassword(request);
      }
    }
  }

  @RequiredArgsConstructor
  class EvictCache2 implements ChangingPassword {
    private final ChangingPassword delegate;
    private final CacheClient cacheClient;

    @Override
    public void changePassword(ChangingPasswordRequest request) {
      try {
        delegate.changePassword(request);

        cacheClient.delete("PC" + request.memberId());
        cacheClient.delete("APP" + request.memberId());
      } catch (Exception e) {
        throw e;
      }
    }
  }

  @RequiredArgsConstructor
  class Retryable implements CacheClient {
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

    }
  }
}