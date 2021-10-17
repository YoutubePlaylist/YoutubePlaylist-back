package com.example.youtubedb.test;

public interface MemberRepository2 {
  void save(Member2 member2);
  Member2 find(String loginId);
}
