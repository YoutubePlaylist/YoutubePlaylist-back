package com.example.youtubedb.repository;

import com.example.youtubedb.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
}
