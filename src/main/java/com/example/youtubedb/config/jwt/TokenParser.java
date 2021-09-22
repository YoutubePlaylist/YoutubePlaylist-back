package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.AccessToken;
import java.util.Optional;

// 예외를 통해서 control flow를 조작하지 않는다
// if (~~) {  ... } // control flow
// 순차 - 분기 - 반복


public interface TokenParser {
  AccessToken parse1(String tokenString) throws CannotParseException;

  Optional<AccessToken> parse2(String tokenString);

  ParseResult parse3(String tokenString);









  interface ParseResult {
    boolean isSucce예ss();

    String failedReason();

    AccessToken accessToken();
  }

  class CannotParseException extends RuntimeException {
    public CannotParseException(String failedReason) {
      super(failedReason);
    }
  }



  class TT {
    static int i = 0;

    public static void main(String[] args) {
      // 500은 랜덤 숫자
      for (int j = 0; j < 500; j++) {
        if (doB()) {
          break;
        }
      }
      System.out.println("끝났습니다");






      for (int i = 0; i < 500; i++) {
        try {
          doA();
        } catch (EndException e) {
          break;
        }
      }
      System.out.println("끝났습니다");
    }

    private static void doA() {
      if (i == 100) {
        throw new EndException();
      }
      i++;
    }

    private static boolean doB() {
      if (i == 100) {
        return true;
      }
      i++
      return false;
    }

    static class EndException extends RuntimeException {

    }
  }

}
