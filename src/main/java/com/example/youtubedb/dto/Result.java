package com.example.youtubedb.dto;

import java.util.Set;

public interface Result<T> {
  boolean isSuccess();

  Set<FailedReason> failedReason();

  T request();
}
