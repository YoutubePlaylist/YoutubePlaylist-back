package com.example.youtubedb.dto;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class Failed<T> implements Result<T>{
  private final Set<FailedReason> failedReasons;

  @Override
  public boolean isSuccess() {
    return false;
  }

  @Override
  public Set<FailedReason> failedReason() {
    return failedReasons;
  }

  @Override
  public T request() {
    throw new IllegalStateException("I have no request;;");
  }
}
