package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.ConstantTime;
import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.exception.ContractViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static com.example.youtubedb.Fixture.constTime;
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

ConstantTime이 굳이 필요한가..? -> 어차피 Instant쓰니까 plus, minus 메서드를 사용하면 되지않을까..?(특정 시점이 아닌 현재, 과거, 미래가 목적이니까..?) -> 아아 아니다, 테스트시 특정 시점에 대한 고정이 필요할 수 있다. 내부적으로 Instant.now()를 해버리면 테스트하기 까다롭다.9

-> 흠... 근데 해당 테스트를 봐서 RefreshToken의 정보를 알려주는게 목적 -> 여기서 주는 정보는 만료시간을 과거로 설정해서 만들면 안된다...
-> 애초에 그러면 해당 생성자를 package-private으로 만들고 안에 static public Provider 클래스를 만들어서 거기서 create해준다면..? 거기서 isPC의 값에 따라 적절한 만료시간만큼 뒤인 RefreshToken을 만들어주면 되지않을까...?
-> 그리고 해당 값에 따라 적절한 만료시간을 가진 RefreshToken이 만들어짐을 보여주는 테스트만 작성하면 해당 자료구조 클래스를 보고 사용자가 테스트코드를 봤을 때 어떻게 해당 자료구조가 사용되는지 알 수 있지 않을까..?
* */

class RefreshTokenTest {
  final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
  final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofDays(30);
  RefreshToken.Provider provider;
  CurrentTimeServer time;

  @BeforeEach
  void setUp() {
    time = new ConstantTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    provider = new RefreshToken.Provider(time);
  }

  @Test
  void RefreshToken_PC() {
    boolean isPC = true;
    RefreshToken refreshToken = provider.create(isPC);
    assertThat(refreshToken.expirationAt(), is(time.now().plus(REFRESH_TOKEN_EXPIRE_DATE_PC)));
  }

  @Test
  void RefreshToken_APP() {
    boolean isPC = false;
    RefreshToken refreshToken = provider.create(isPC);
    assertThat(refreshToken.expirationAt(), is(time.now().plus(REFRESH_TOKEN_EXPIRE_DATE_APP)));
  }

  @Test
  void RefreshToken의_만료시간은_과거X() {
    assertThrows(ContractViolationException.class, () -> provider.create(curTime().now().minus(Period.ofDays(1))));
  }
}
