package com.example.youtubedb.dto.member.request;

import com.example.youtubedb.repository.MemberRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
//@Component
//@Scope("request")
public class MemberChangePasswordRequestDto {

    private MemberRepository memberRepository;

    @Schema(description = "기존 PASSWORD", example = "password123")
    @NotBlank(message = "oldPassword 없음")
    private final String oldPassword;

    @Schema(description = "새로운 PASSWORD", example = "password123")
    @NotBlank(message = "newPassword 없음")
    @Pattern(regexp = "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + 8 + "," + 20 + "})$", message = "비밀번호 규칙을 지키지 못했습니다.")
    private final String newPassword;

    @AssertFalse(message = "새 비밀번호가 기존 비밀번호와 같습니다.")
    private boolean isSameTwoPassword() {
//        memberRepository.findByLoginId("member002");
        if (oldPassword != null && newPassword != null)
            return getOldPassword().equals(getNewPassword());
        return false;
    }


    public MemberChangePasswordRequestDto(String oldPassword, String newPassword) {
//        this.memberRepository = memberRepository;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        isSameTwoPassword();
    }

}
