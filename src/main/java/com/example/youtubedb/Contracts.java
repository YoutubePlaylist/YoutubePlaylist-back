package com.example.youtubedb;

public final class Contracts {
  public static void requires(boolean expectedToBeTrue, String message) {
    if (expectedToBeTrue) {
      return;
    }

    throw new ContractViolationException(message);
  }

  public static void requires(boolean expectedToBeTrue) {
    requires(expectedToBeTrue, "이 메시지를 많이 본다면, 상세한 메시지를 추가하세요");
  }

  private static class ContractViolationException extends RuntimeException {
    public ContractViolationException() {
      super();
    }

    public ContractViolationException(String message) {
      super(message);
    }
  }
}
