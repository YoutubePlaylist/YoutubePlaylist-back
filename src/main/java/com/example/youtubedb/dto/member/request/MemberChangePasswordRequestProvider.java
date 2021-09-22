package com.example.youtubedb.dto.member.request;

import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.NOT_FOUND_MEMBER;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.NOT_MATCHED_OLD_PASSWORD;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.UNEXPECTED_NEW_PASSWORD;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.repository.MemberRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class MemberChangePasswordRequestProvider {
  private final MemberRepository memberRepository;
  private final SecuredPassword.Provider securedPasswordProvider;

  public Result create(MemberChangePasswordRequestDto dto) {
    final String memberId = dto.getMemberId();
    final String oldFromUser = dto.getOldPassword();
    final String newPassword = dto.getNewPassword();

    final boolean 존재하는_멤버니 = memberRepository.findByLoginId(memberId).isPresent();
    final boolean 이전_패스워드랑_같니 = memberRepository
      .findByLoginId(memberId)
      .map(Member::getPassword)
      .map(oldFromDb -> oldFromDb.equals(oldFromUser))
      .orElse(false);
    final boolean 새로운_패스워드는_새롭니 = !newPassword.equals(oldFromUser);


    if (존재하는_멤버니 && 이전_패스워드랑_같니 && 새로운_패스워드는_새롭니) {
      return new Success(new ChangingPasswordRequest(memberId, securedPasswordProvider.create(newPassword)));
    }

    Set<FailedReason> reasons = new HashSet<>();
    if (!존재하는_멤버니) {
      reasons.add(NOT_FOUND_MEMBER);
    }
    if (!이전_패스워드랑_같니) {
      reasons.add(NOT_MATCHED_OLD_PASSWORD);
    }
    if (!새로운_패스워드는_새롭니) {
      reasons.add(UNEXPECTED_NEW_PASSWORD);
    }
    return new Failed(reasons);
  }


  public enum FailedReason {
    NOT_FOUND_MEMBER,
    NOT_MATCHED_OLD_PASSWORD,
    UNEXPECTED_NEW_PASSWORD,

    REASON_1,
    REASON_2,
  }


  public interface Result {
    boolean isSuccess();

    Set<FailedReason> failedReason();

    ChangingPasswordRequest request();
  }


  @RequiredArgsConstructor
  class Success implements Result {
    private final ChangingPasswordRequest request;

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public Set<FailedReason> failedReason() {
      throw new IllegalStateException("아니 성공했는데 왜 실패한 이유를 물어보세요;;");
    }

    @Override
    public ChangingPasswordRequest request() {
      return request;
    }
  }

  @RequiredArgsConstructor
  class Failed implements Result {
    private final Set<FailedReason> failedReason;

    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public Set<FailedReason> failedReason() {
      return failedReason;
    }

    @Override
    public ChangingPasswordRequest request() {
      throw new IllegalStateException("아니 실패했다니까 왜 값을 가져가려고 하세요;;;");
    }
  }
}


