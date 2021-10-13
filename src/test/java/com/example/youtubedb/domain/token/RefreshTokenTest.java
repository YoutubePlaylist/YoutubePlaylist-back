package com.example.youtubedb.domain.token;

import com.example.youtubedb.exception.ContractViolationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.Period;

import static com.example.youtubedb.Fixture.curTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
RefreshToken은 자료구조로 쓰이기에 테스트할 필요가 없다고 생각할 수 있다.
하지만 테스트 코드는 문서화의 기능도 가지고 있기에 테스트 코드를 작성해주는 것이 좋다.

그렇다면
RefreshToken은 무엇을 테스트해야하는가?
-> RefreshToken의 만료시간은 현재시간보다 전 일 수 없다.

ConstantTime이 굳이 필요한가..? -> 어차피 Instant쓰니까 plus, minus 메서드를 사용하면 되지않을까..?(특정 시점이 아닌 현재, 과거, 미래가 목적이니까..?)
* */

class RefreshTokenTest {
  @Test
  void RefreshToken_생성() {
    Instant expirationTime = curTime().now().plus(Period.ofDays(7));
    RefreshToken refreshToken = new RefreshToken(expirationTime);
    assertThat(refreshToken.expirationAt(), is(expirationTime));
  }

  @Test
  void RefreshToken의_만료시간은_과거X() {
    assertThrows(ContractViolationException.class, () -> new RefreshToken(curTime().now().minus(Period.ofDays(1))));
  }
}
