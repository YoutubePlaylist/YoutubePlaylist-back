package com.example.youtubedb.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class RealMemberCreateRequestDto {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotNull
    private Boolean isPc;

    @Builder
    public RealMemberCreateRequestDto(String loginId, String password, Boolean isPc){
        this.loginId = loginId;
        this.password = password;
        this.isPc = isPc;
    }
}
