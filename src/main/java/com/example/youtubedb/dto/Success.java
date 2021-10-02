package com.example.youtubedb.dto;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class Success<T> implements Result<T> {
  private final T request;

  @Override
  public boolean isSuccess() {
    return true;
  }

  @Override
  public Set<FailedReason> failedReason() {
    throw new IllegalStateException("I succeded It;;");
  }

  @Override
  public T request() {
    return request;
  }
}
