package com.example.youtubedb.dto.member.request;

import com.example.youtubedb.domain.member.SecuredPassword;
import lombok.Getter;

import static com.example.youtubedb.util.ContractUtil.requires;

@Getter
public class ChangingPasswordRequest {
  private final String memberId;
  private final SecuredPassword newPassword;

  ChangingPasswordRequest(String memberId, SecuredPassword newPassword) {
    requires(memberId != null);
    requires(memberId.length() > 0);

    this.memberId = memberId;
    this.newPassword = newPassword;
  }
}
