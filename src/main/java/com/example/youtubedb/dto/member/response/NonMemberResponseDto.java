package com.example.youtubedb.dto.member.response;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class NonMemberResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "생성된 비회원")
    private final Member response;
    @Schema(description = "JWT 토큰")
    private final String token;

    public NonMemberResponseDto(Member response, String token) {
        this.response = response;
        this. token = token;
    }
}
