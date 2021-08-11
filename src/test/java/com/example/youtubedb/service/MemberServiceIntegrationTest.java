/*
package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.exception.DoNotMatchPasswordException;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.exception.NotExistMemberException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

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
    void 가입시_중복() {
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
        Member member = memberService.registerNon(deviceId, false);

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

    @Test
    void 회원_비회원_삭제() {
        // given
        String deviceId = "device001";
        Member nonMember = memberService.registerNon(deviceId);

        // when
        memberService.deleteUserByLoginId(deviceId);
        Exception e = assertThrows(NotExistMemberException.class, () -> memberService.findMemberByLoginId(deviceId));

        // then
        assertThat(e.getMessage()).isEqualTo(NotExistMemberException.getErrorMessage());
    }

    @Test
    void 회원가입() {
        // given
        String loginId = "helloMan";
        String password = "hello123";

        // when
        Member member = memberService.registerReal(loginId, password, false);
        Member finded = memberService.findMemberByLoginId(loginId);

        // then
        assertAll(
                () -> assertThat(finded.getId()).isEqualTo(member.getId()),
                () -> assertThat(finded.getLoginId()).isEqualTo(member.getLoginId()),
                () -> assertThat(finded.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(finded.isMember()).isEqualTo(member.isMember())
        );
    }

    @Test
    void 로그인() {
        // given
        String loginId = "helloMan";
        String password = "hello123";

        // when
        Member member = memberService.registerReal(loginId, password, false);
        Member loginMember = memberService.login(loginId, password, false);

        // then
        assertAll(
                () -> assertThat(loginMember.getId()).isEqualTo(member.getId()),
                () -> assertThat(loginMember.getLoginId()).isEqualTo(member.getLoginId()),
                () -> assertThat(loginMember.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(loginMember.isMember()).isEqualTo(member.isMember())
        );
    }

    @Test
    void 로그인_비밀번호_불일치() {
        // given
        String loginId = "helloMan";
        String password = "hello123";
        String otherPassword = "bye123";
        Member member = memberService.register(loginId, password);

        // when
       Exception e = assertThrows(DoNotMatchPasswordException.class, () -> memberService.login(loginId, otherPassword));

        // then
        assertThat(e.getMessage()).isEqualTo(DoNotMatchPasswordException.getErrorMessage());
    }
}*/
