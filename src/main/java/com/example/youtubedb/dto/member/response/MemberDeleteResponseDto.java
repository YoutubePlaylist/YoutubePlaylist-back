package com.example.youtubedb.dto.member.response;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberDeleteResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "삭제된 사용자 LoginId")
    private final String response;

    public MemberDeleteResponseDto(String response) {
        this.response = response;
    }
}
