package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @Test
    void 비회원_등록() {
        // given
        String deviceId = "device001";

        // when
        Member nonMember = memberService.registerNon(deviceId);

        // then
        assertAll(
                () -> assertThat(nonMember.isMember()).isEqualTo(false),
                () -> assertThat(nonMember.getLoginId()).isEqualTo(deviceId),
                () -> assertThat(nonMember.getPassword()).isEqualTo(null)
        );
    }

    @Test
    void 등록시_중복() {
        // given
        String deviceId = "device001";

        // when
        memberService.registerNon(deviceId);
        Exception e = assertThrows(DuplicateMemberException.class, () -> memberService.registerNon(deviceId));

        // then
        assertThat(e.getMessage()).isEqualTo("ID가 중복된 회원입니다.");
    }
}