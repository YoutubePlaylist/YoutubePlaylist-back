package com.example.youtubedb.member.login.core;

import com.example.youtubedb.member.Member2;

public interface MemberRepository2 {
  void save(Member2 member2);
  Member2 find(String loginId);
}
