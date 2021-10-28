package com.example.youtubedb.member.login.spring.request;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class JsonNonMemberLoginRequest {
  private final String deviceId;

  public JsonNonMemberLoginRequest(String deviceId) {
    this.deviceId = deviceId;
  }
}

