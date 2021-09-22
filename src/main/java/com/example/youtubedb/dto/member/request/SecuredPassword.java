package com.example.youtubedb.dto.member.request;

import static com.example.youtubedb.Contracts.requires;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecuredPassword {
  String password;




  private SecuredPassword(String password) {
    requires(password.length() > 10);
    this.password = password;
  }





  @RequiredArgsConstructor
  public static class Provider {
    private final PasswordEncoder passwordEncoder;

    public SecuredPassword create(String raw) {
      return new SecuredPassword(passwordEncoder.encode(raw));
    }
  }





  interface MyPasswordEncoder {
    String encode(String raw);
  }







  @RequiredArgsConstructor
  class SpringEncoder implements MyPasswordEncoder {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String raw) {
      return passwordEncoder.encode(raw);
    }
  }

  class Nothing implements MyPasswordEncoder {
    @Override
    public String encode(String raw) {
      return raw;
    }
  }
}
