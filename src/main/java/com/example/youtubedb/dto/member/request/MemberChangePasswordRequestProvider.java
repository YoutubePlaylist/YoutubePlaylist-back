package com.example.youtubedb.dto.member.request;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.member.SecuredPasssword;
import com.example.youtubedb.dto.Failed;
import com.example.youtubedb.dto.FailedReason;
import com.example.youtubedb.dto.Result;
import com.example.youtubedb.dto.Success;
import com.example.youtubedb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.example.youtubedb.dto.FailedReason.*;

@Component
public class MemberChangePasswordRequestProvider {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecuredPasssword.Provider securedPasswordProvider;

  public MemberChangePasswordRequestProvider(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.securedPasswordProvider = new SecuredPasssword.Provider(passwordEncoder);
  }

  public Result<ChangingPasswordRequest> create(String loginId, MemberChangePasswordRequestDto dto) {
    final String oldPassword = dto.getOldPassword();
    final String newPassword = dto.getNewPassword();

    final boolean isPresentedMember = memberRepository.findByLoginId(loginId).isPresent();
    final boolean isEqualOldPassword = memberRepository
      .findByLoginId(loginId)
      .map(Member::getPassword)
      .map(realPassword -> passwordEncoder.matches(oldPassword, realPassword))
      .orElse(false);
    final boolean isNew = !newPassword.equals(oldPassword);

    if(isPresentedMember && isEqualOldPassword && isNew) {
      return new Success<ChangingPasswordRequest>(new ChangingPasswordRequest(loginId, securedPasswordProvider.create(newPassword)));
    }

    Set<FailedReason> reasons = new HashSet<>();
    if(!isPresentedMember) {
      reasons.add(NOT_FOUND_MEMBER);
    }
    if(!isEqualOldPassword) {
      reasons.add(NOT_MATCHED_OLD_PASSWORD);
    }
    if(!isNew) {
      reasons.add(UNEXPECTED_NEW_PASSWORD);
    }
    return new Failed<ChangingPasswordRequest>(reasons);
  }
}
