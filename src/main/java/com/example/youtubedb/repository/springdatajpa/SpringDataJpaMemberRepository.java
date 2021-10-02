package com.example.youtubedb.repository.springdatajpa;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.repository.interfaces.MemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
}
