package com.example.youtubedb.dto.member.response;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberCreateResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "생성된 회원")
    private final Member response;

    public MemberCreateResponseDto(Member response) {
        this.response = response;
    }
}
