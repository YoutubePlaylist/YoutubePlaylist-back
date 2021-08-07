package com.example.youtubedb.repository;

import com.example.youtubedb.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
