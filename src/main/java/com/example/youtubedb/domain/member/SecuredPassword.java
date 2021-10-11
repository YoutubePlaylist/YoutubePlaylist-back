package com.example.youtubedb.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;

import static com.example.youtubedb.util.ContractUtil.requires;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecuredPassword {
  String password;

  private SecuredPassword(String password) {
    requires(password.length() >= 8);
    this.password = password;
  }

  @RequiredArgsConstructor
  @Component
  public static class Provider {
    private final PasswordEncoder passwordEncoder;

    public SecuredPassword create(String raw) {
      return new SecuredPassword(passwordEncoder.encode(raw));
    }
  }
}
