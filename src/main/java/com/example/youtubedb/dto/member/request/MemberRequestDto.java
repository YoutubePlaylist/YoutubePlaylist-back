package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequestDto {
    @Schema(description = "로그인 ID", example = "user001")
    private final String loginId;
    @Schema(description = "로그인 PASSWORD", example = "password123")
    private final String password;
    @Schema(description = "PC여부", example = "false")
    private final Boolean isPC;

    @Builder
    public MemberRequestDto(String loginId, String password, Boolean isPC) {
        this.loginId = loginId;
        this.password = password;
        this.isPC = isPC;
    }
}


