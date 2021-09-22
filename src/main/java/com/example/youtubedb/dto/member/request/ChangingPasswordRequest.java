package com.example.youtubedb.dto.member.request;

import static com.example.youtubedb.Contracts.requires;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class ChangingPasswordRequest {
  private final String memberId;
  private final SecuredPassword newPassword;

  ChangingPasswordRequest(String memberId, SecuredPassword newPassword) {
    requires(memberId.length() > 0);

    this.memberId = memberId;
    this.newPassword = newPassword;
  }
}

// OOP - 객체 생성에 대한 것도 우리가 충분히 조작할수있다 - 디자인패턴:생성패턴
// if else

