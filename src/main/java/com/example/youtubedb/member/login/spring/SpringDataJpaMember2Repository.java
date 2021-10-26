package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.member.login.core.MemberRepository2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMember2Repository extends JpaRepository<Member, Long>, MemberRepository2 {
}
