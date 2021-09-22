package com.example.youtubedb.controller;

import static com.example.youtubedb.Contracts.requires;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.NOT_FOUND_MEMBER;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.NOT_MATCHED_OLD_PASSWORD;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.UNEXPECTED_NEW_PASSWORD;
import static java.util.stream.Collectors.joining;

import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestDto;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class FailedReasonTranslator {
  private final Map<FailedReason, String> mapper;

  public FailedReasonTranslator(Map<FailedReason, String> mapper) {
    requires(mapper.size() == FailedReason.values().length);

    this.mapper = mapper;
  }

  public String translate(FailedReason failedReason) {
    return mapper.get(failedReason);
  }

  public String translate(Collection<FailedReason> failedReasons) {
    return failedReasons.stream()
      .map(this::translate)
      .collect(joining("\n"));
  }

  public static FailedReasonTranslator create(MemberChangePasswordRequestDto dto) {
    Map<FailedReason, String> mapper = new HashMap<>();
    mapper.put(NOT_FOUND_MEMBER, "멤버가 없어요~ 이전에 생성했는지 보세요~ 잘못된 id: [%s]" + dto.getMemberId());
    mapper.put(NOT_MATCHED_OLD_PASSWORD, "비밀번호가 달라요~");
    mapper.put(UNEXPECTED_NEW_PASSWORD, "새로운 비밀번호가 규칙에 어긋나요~ 뭐 5글자 이상으로 해주세요~ (기타 조건 블라블라)");

    return new FailedReasonTranslator(mapper);
  }
}
