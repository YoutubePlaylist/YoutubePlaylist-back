package com.example.youtubedb.util;

public final class Contracts {
  private static void requires(boolean expectedToBeTrue, String message) {
    if (expectedToBeTrue) {
      return;
    }

    throw new ContractViolationException(message);
  }

  public static void requires(boolean expectedToBeTrue) {
    requires(expectedToBeTrue, "입력 값이 유효하지 않습니다");
  }

  protected static class ContractViolationException extends RuntimeException {
    public ContractViolationException() {
      super();
    }

    public ContractViolationException(String message) {
      super(message);
    }
  }
}
