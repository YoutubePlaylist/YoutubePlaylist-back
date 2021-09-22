package com.example.youtubedb.config.jwt;

import java.time.Duration;
import java.time.LocalDateTime;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class TimeMatchers {
  public static Matcher<LocalDateTime> isAfter30MinutesFrom(LocalDateTime baseTime) {
    return new TypeSafeDiagnosingMatcher<LocalDateTime>() {
      @Override
      protected boolean matchesSafely(LocalDateTime item, Description mismatchDescription) {
        final LocalDateTime expected = baseTime.plus(Duration.ofMinutes(30));
        if (item.equals(expected)) {
          return true;
        }

        mismatchDescription
          .appendValue(item)
          .appendText("이가 기대하는 값")
          .appendValue(expected)
          .appendText("와 다릅니다. 시간이 맞지 않아요....")
          .appendText(String.format("[%s] 정도 시간이 차이가 납니다.", Duration.between(expected, item)));
        return false;
      }

      @Override
      public void describeTo(Description description) {
      }
    };
  }
}
