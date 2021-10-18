package com.example.youtubedb.dto.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberForTokenDto {
  private final boolean isPc;
  private final String loginId;
}
