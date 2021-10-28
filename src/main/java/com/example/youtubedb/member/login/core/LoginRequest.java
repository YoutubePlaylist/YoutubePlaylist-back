package com.example.youtubedb.member.login.core;

import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;

import static com.example.youtubedb.util.Contracts.requires;

@Getter
@Accessors(fluent = true)
@Value
public class LoginRequest {
  private final LoginId loginId;
  private final RawPassword rawPassword;

  @Value
  class LoginId {
    String id;

    public LoginId(String id) {
      requires(id != null);

      this.id = id;
    }
  }

  @Value
  class RawPassword {
    String password;

    public RawPassword(String password) {
      requires(password.length() > 8);
      this.password = password;
    }
  }

  public LoginRequest(String loginId, String rawPassword) {
    this.loginId = new LoginId(loginId);
    this.rawPassword = new RawPassword(rawPassword);
  }
}