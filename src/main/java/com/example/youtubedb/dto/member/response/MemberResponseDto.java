package com.example.youtubedb.dto.member.response;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "생성된 회원")
    private final Member response;
    @Schema(description = "JWT 토큰")
    private final String token;

    public MemberResponseDto(Member response, String token) {
        this.response = response;
        this. token = token;
    }
}
