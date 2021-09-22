package com.example.youtubedb.config.jwt;

import static com.example.youtubedb.config.jwt.TimeMatchers.isAfter30MinutesFrom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.youtubedb.domain.AccessToken;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.jupiter.api.Test;

class TokenService2Test {
  static String secretKey = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
  static JwtConfig jwtConfig = new JwtConfig(secretKey);

  @Test
  void test1() {
    // 구현을 먼저 생각하지 않는게 중요하다
    // 클라이언트 입장 interface를 먼저 생각한다

    // given
    final JwtFixture jwtFixture = fixtureAt(LocalDateTime.now());

    // when
    final AccessToken token = fixture().subject().create("id-123");

    final AccessToken expected = new AccessToken(secretKey, "id-123", fixture().expirationTime(), SignatureAlgorithm.HS512);
    assertThat(token, is(expected));
  }


  @Test
  void 토큰_만료_시간은_항상_동일하다() {
    final JwtFixture jwtFixture = fixtureAt(LocalDateTime.now());
    final AccessToken token = jwtFixture.subject().create(anyString());

    assertThat(token, is여러번_호출해도_항상_동일(jwtFixture.expirationTime()));
  }

















  private Matcher<AccessToken> is여러번_호출해도_항상_동일(LocalDateTime expiredTime) {
    return new TypeSafeDiagnosingMatcher<AccessToken>() {
      @Override
      protected boolean matchesSafely(AccessToken item, Description mismatchDescription) {
        for (int i = 0; i < 3; i++) {
          if (item.expirationAt().equals(expiredTime)) {
            continue;
          }

          mismatchDescription.appendText("2번째에는 실패햇어... 뭔가 이상한거야... ");
          return false;
        }
        return true;
      }

      @Override
      public void describeTo(Description description) {

      }
    }
  }


  @Test
  void 잘_파싱하니() {
    final JwtFixture jwtFixture = fixture();
    final AccessToken token = jwtFixture.subject().parseToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpZC00NTYiLCJleHAiOjE2MzIyOTI0OTR9.r79sTygbR_kGYIqglkD4A2i3nwVD5L1PoORKpq-bGTUPT5IPZZOo4dDP0fffaP7c93M48JWOAEP0VsXnF8Ze4g");

    assertThat(token.loginId(), is("id-456"));
    assertThat(token.expirationAt(), is(jwtFixture.expirationTime()));
    assertThat(token.signatureAlgorithm(), is(SignatureAlgorithm.HS512));
  }


  private static JwtFixture fixture() {
    final LocalDateTime expirationTime = LocalDateTime.of(2021, 9, 22, 15, 34, 54);
    final ConstantTime fakeTimeServer = new ConstantTime(expirationTime);
    return JwtFixture.builder()
      .subject(new TokenService2(fakeTimeServer, jwtConfig, secretKey))
      .expirationTime(expirationTime)
      .build();
  }

  private static JwtFixture fixtureAt(LocalDateTime expirationTime) {
    final ConstantTime fakeTimeServer = new ConstantTime(expirationTime);
    return JwtFixture.builder()
      .subject(new TokenService2(fakeTimeServer, jwtConfig, secretKey))
      .expirationTime(expirationTime)
      .build();
  }

  @Builder
  @Getter
  @Accessors(fluent = true)
  class JwtFixture {
    TokenService2 subject;
    LocalDateTime expirationTime;
  }

  @Test
  void 잘_파싱하니_2() {
    final LocalDateTime expirationTime = LocalDateTime.of(2021, 9, 22, 15, 34, 54);
    final ConstantTime fakeTimeServer = new ConstantTime(expirationTime);
    final TokenService2 tokenProvider = new TokenService2(fakeTimeServer, jwtConfig, secretKey);
    final AccessToken token = tokenProvider.parseToken(tokenProvider.create("id-456").asJwtToken());

    assertThat(token.loginId(), is("id-456"));
    assertThat(token.expirationAt(), isAfter30MinutesFrom(expirationTime));
    assertThat(token.signatureAlgorithm(), is(SignatureAlgorithm.HS512));
  }



  private static String anyString() {
    return "<any>";
  }
}