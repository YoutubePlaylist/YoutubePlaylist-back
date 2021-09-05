package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberChangePasswordRequestDto {

    @Schema(description = "기존 PASSWORD", example = "password123")
    private final String oldPassword;
    @Schema(description = "새로운 PASSWORD", example = "password123")
    private final String newPassword;

    @Builder
    public MemberChangePasswordRequestDto(String oldPassword, String newPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

}
