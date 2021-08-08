package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDto {
    @Schema(description = "장치 ID", example = "device001")
    private String loginId;
    private String password;

    @Builder
    public MemberLoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
