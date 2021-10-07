package com.example.youtubedb.controller;

import com.example.youtubedb.dto.FailedReason;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.example.youtubedb.dto.FailedReason.*;
import static com.example.youtubedb.util.ContractUtil.requires;
import static java.util.stream.Collectors.joining;

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
    mapper.put(NOT_FOUND_MEMBER, "No Users...");
    mapper.put(NOT_MATCHED_OLD_PASSWORD, "Password is different...");
    mapper.put(UNEXPECTED_NEW_PASSWORD, "It's against the password rules...");

    return new FailedReasonTranslator(mapper);
  }
}
