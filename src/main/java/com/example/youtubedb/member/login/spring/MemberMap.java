package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.Member2;
import com.example.youtubedb.member.login.core.MemberRepository2;

import java.util.HashMap;
import java.util.Map;

public class MemberMap implements MemberRepository2 {
  Map<String, Member2> testRepository = new HashMap<>();

  public void save(Member2 member2) {
    testRepository.put(member2.loginId(), member2);
  }

  public Member2 find(String loginId) {
    return testRepository.get(loginId);
  }
}
