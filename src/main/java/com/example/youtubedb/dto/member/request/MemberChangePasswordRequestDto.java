package com.example.youtubedb.dto.member.request;

import lombok.Value;

@Value
public class MemberChangePasswordRequestDto {
  String memberId;
  String oldPassword;
  String newPassword;
}


