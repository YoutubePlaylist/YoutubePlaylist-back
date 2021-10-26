package com.example.youtubedb.member.login.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class LoginRequest {
  private String loginId;
  private final String rawPassword;
}