package com.example.youtubedb.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.youtubedb.util.ContractUtil.requires;

public class SecuredPasssword {
  String password;

  private SecuredPasssword(String password) {
    requires(password.length() >= 8);
    this.password = password;
  }

  @RequiredArgsConstructor
  public static class Provider {
    private final PasswordEncoder passwordEncoder;

    public SecuredPasssword create(String raw) {
      return new SecuredPasssword(passwordEncoder.encode(raw));
    }
  }
}
