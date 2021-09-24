package com.example.youtubedb.integrationTest.service;

import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.exception.*;
import com.example.youtubedb.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "spring.config.location="
        + "classpath:application.yaml,"
        + "classpath:aws.yaml")
@Transactional
class MemberServiceIntegrationTest {
    @Autowired
    MemberService memberService;

    @Test
    void 비회원_등록() {
        // given
        String deviceId = "device001";

        // when
        Member nonMember = memberService.registerNon(deviceId, false);

        // then
        assertAll(
                () -> assertThat(nonMember.isMember()).isEqualTo(false),
                () -> assertThat(nonMember.getLoginId()).isEqualTo(deviceId)
        );
    }

    @Test
    void 가입시_중복() {
        // given
        String deviceId = "device001";
        memberService.registerNon(deviceId, false);

        // when & then
        assertThrows(DuplicateMemberException.class, () -> memberService.registerNon(deviceId, false));
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
                () -> assertThat(result.getLoginId()).isEqualTo(deviceId)
        );
    }

    @Test
    void loginId_조회_존재X() {
        // given
        String deviceId = "device001";

        // when & then
        assertThrows(NotExistMemberException.class, () -> memberService.findMemberByLoginId(deviceId));
    }

    @Test
    void 회원_비회원_삭제() {
        // given
        String deviceId = "device001";
        Member nonMember = memberService.registerNon(deviceId, false);
        memberService.deleteUserByLoginId(deviceId);

        // when & then
        assertThrows(NotExistMemberException.class, () -> memberService.findMemberByLoginId(deviceId));
    }

    @Test
    void 회원가입() {
        // given
        String loginId = "helloMan";
        String password = "hello123*";

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
    void 회원가입_비밀번호_공백() {
        // given
        String loginId = "helloMan";
        String password = "he llo";

        // when & then
        assertThrows(InvalidBlankPasswordException.class, () -> memberService.registerReal(loginId, password, false));
    }

    @Test
    void 회원가입_비밀번호규칙X() {
        // given
        String loginId = "helloMan";
        String password = "hello";

        // when & then
        assertThrows(InvalidRegexPasswordException.class, () -> memberService.registerReal(loginId, password, false));
    }

    @Test
    void 로그인() {
        // given
        String loginId = "helloMan";
        String password = "hello123";
        String BEARER_TYPE = "bearer";

        // when
        Member member = memberService.registerReal(loginId, password, false);
        Token token = memberService.login(loginId, password, false);

        // then
        assertThat(token.getGrantType()).isEqualTo(BEARER_TYPE);
    }

    @Test
    void 로그인_비밀번호_불일치() {
        // given
        String loginId = "helloMan";
        String password = "hello123";
        String otherPassword = "bye123";
        Member member = memberService.registerReal(loginId, password, false);

        // when & then
       assertThrows(DoNotMatchPasswordException.class, () -> memberService.login(loginId, otherPassword, false));
    }

    @Test
    void 프로필_이미지() {
        // given
        String loginId = "helloMan";
        String password = "hello123**";
        String profileImg = "profile123";
        Member member = memberService.registerReal(loginId, password, false);

        // when
        memberService.setProfileImg(member, profileImg);
        // then
        assertThat(member.getProfileImg()).isEqualTo(profileImg);
    }

    @Test
    void 회원X_비회원() {
        // given
        String loginId = "helloMan";
        Member member = memberService.registerNon(loginId, false);

        // when & then
        Exception e = assertThrows(NotMemberException.class, () -> memberService.checkMember(member));
    }
}
