package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberChangingUserDto {
    @Schema(description = "장치 ID", example = "device001")
    @NotBlank
    private String loginId;

    @Schema(description = "장치 비밀번호", example = "password01!")
    @NotBlank
    private String password;

    @Builder
    public MemberChangingUserDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
