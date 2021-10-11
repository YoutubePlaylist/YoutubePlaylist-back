package com.example.youtubedb.dto.member.request;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.member.SecuredPassword;
import com.example.youtubedb.dto.Failed;
import com.example.youtubedb.dto.FailedReason;
import com.example.youtubedb.dto.Result;
import com.example.youtubedb.dto.Success;
import com.example.youtubedb.repository.MemberRepository;
import com.example.youtubedb.service.PasswordValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.example.youtubedb.dto.FailedReason.*;

@Component
@RequiredArgsConstructor
public class MemberChangePasswordRequestProvider {
  private final MemberRepository memberRepository;
  private final SecuredPassword.Provider securedPasswordProvider;
  private final PasswordValidationService passwordValidationService;

  public Result<ChangingPasswordRequest> create(String loginId, MemberChangePasswordRequestDto dto) {
    final String oldPassword = dto.getOldPassword();
    final String newPassword = dto.getNewPassword();

    final boolean isPresentedMember = memberRepository.findByLoginId(loginId).isPresent();
    final boolean isEqualOldPassword = memberRepository
      .findByLoginId(loginId)
      .map(Member::getPassword)
      .map(realPassword -> passwordValidationService.checkCorrectPassword(oldPassword, realPassword.getPassword()))
      .orElse(false);
    final boolean isNew = !newPassword.equals(oldPassword);

    if(isPresentedMember && isEqualOldPassword && isNew) {
      return new Success<>(new ChangingPasswordRequest(loginId, securedPasswordProvider.create(newPassword)));
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
    return new Failed<>(reasons);
  }
}
