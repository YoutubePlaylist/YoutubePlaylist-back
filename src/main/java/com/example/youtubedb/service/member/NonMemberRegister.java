package com.example.youtubedb.service.member;

import com.example.youtubedb.domain.member.Authority;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.exception.NotExistMemberException;
import com.example.youtubedb.repository.interfaces.MemberRepository;
import com.example.youtubedb.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface NonMemberRegister {
  Member register(NonMemberRequestDto dto);


  @RequiredArgsConstructor
  class UpdateDatabase implements NonMemberRegister {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(NonMemberRequestDto dto) {
      checkDuplicateMember(dto.getDeviceId());

      return memberRepository.save(
        Member.builder()
          .isMember(false)
          .loginId(dto.getDeviceId())
          .authority(Authority.ROLE_USER)
          .password(passwordEncoder.encode(dto.getDeviceId()))
          .isPC(dto.getIsPC())
          .build()
      );
    }

    private void checkDuplicateMember(String loginId) {
      memberRepository.findByLoginId(loginId).ifPresent(m -> {
        throw new DuplicateMemberException();
      });
    }
  }

  @RequiredArgsConstructor
  class CreatePlaylist implements NonMemberRegister {

    private final NonMemberRegister nonMemberRegister;
    private final PlaylistService playlistService;

    @Override
    public Member register(NonMemberRequestDto dto) {
      Member registerMember = nonMemberRegister.register(dto);
      playlistService.createPlaylist("default", false, "OTHER", registerMember);
      return registerMember;
    }
  }
}
