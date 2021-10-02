package com.example.youtubedb.repository.interfaces;

import com.example.youtubedb.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findByLoginId(String loginId);
    void delete(Member member);
//    boolean existsByLoginId(String loginId);
}
