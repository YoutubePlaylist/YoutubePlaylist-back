package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.exception.NotExistMemberException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {
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
        assertThat(e.getMessage()).isEqualTo(DuplicateMemberException.getErrorMessage());
    }

    @Test
    void loginId_조회() {
        // given
        String deviceId = "device001";
        Member member = memberService.registerNon(deviceId);

        // when
        Member result = memberService.findMemberByLoginId(member.getLoginId());

        // then
        assertAll(
                () -> assertThat(result.isMember()).isEqualTo(false),
                () -> assertThat(result.getLoginId()).isEqualTo(deviceId),
                () -> assertThat(result.getPassword()).isEqualTo(null)
        );
    }

    @Test
    void loginId_조회_존재X() {
        // given
        String deviceId = "device001";

        // when
        Exception e = assertThrows(NotExistMemberException.class, () -> memberService.findMemberByLoginId(deviceId));

        // then
        assertThat(e.getMessage()).isEqualTo(NotExistMemberException.getErrorMessage());
    }
}