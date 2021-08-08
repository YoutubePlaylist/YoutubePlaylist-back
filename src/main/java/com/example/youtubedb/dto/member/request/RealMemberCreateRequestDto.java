package com.example.youtubedb.dto.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor
public class RealMemberCreateRequestDto {

    private String loginId;
    private String password;
    private Boolean isPc;

    @Builder
    public RealMemberCreateRequestDto(String loginId, String password, Boolean isPc){
        this.loginId = loginId;
        this.password = password;
        this.isPc = isPc;
    }
}
