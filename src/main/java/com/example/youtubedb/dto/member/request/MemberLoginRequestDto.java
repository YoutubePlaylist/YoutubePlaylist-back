package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
public class MemberLoginRequestDto {

    @Schema(description = "로그인 ID", example = "user001")
    @NotBlank
    private final String loginId;

    @Schema(description = "로그인 PASSWORD", example = "password123")
    @NotBlank
    private final String password;

    @NotNull
    @Schema(description = "PC여부", example = "false")
    private final Boolean isPC;

    @Builder
    public MemberLoginRequestDto(String loginId, String password, Boolean isPC) {
        this.loginId = loginId;
        this.password = password;
        this.isPC = isPC;
    }
}


