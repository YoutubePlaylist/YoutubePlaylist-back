package com.example.youtubedb.service.member;

//import static org.junit.jupiter.api.Assertions.*;
//import org.hamcrest.


import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.Token;
import com.example.youtubedb.domain.member.Authority;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.repository.interfaces.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberLoginTest {

  static MemberRepository memberRepository;
  static AuthenticationManagerBuilder authenticationManagerBuilder;
  static TokenProvider tokenProvider;

  @Test
  void jwt를_잘_만드니() {
    //given
    given(memberRepository.findByLoginId("test1")).willReturn(null);

    MemberLogin jwtTest = new MemberLogin.JwtMaker(memberRepository, authenticationManagerBuilder, tokenProvider);
    Token token = jwtTest.login(new MemberLoginRequestDto("test1", "test12", true));
    Token toekn1 = jwtTest.login(new MemberLoginRequestDto("test1", "test12", true));

    assertThat(token, is(token));
  }

  private Optional<Member> memberFixture() {
    Member member = new Member("test1", "password1", true, Authority.ROLE_USER, true);
    return Optional.ofNullable(member);
  }

  class MemberFixture {

  }
}