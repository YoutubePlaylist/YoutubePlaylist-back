package com.example.youtubedb.dto.play.response;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlayDeleteResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "삭제된 영상 아이디")
    private final Long id;
    @Schema(description = "삭제 여부")
    private final boolean deleted = true;

    public PlayDeleteResponseDto(Long id) {
        this.id = id;
    }
}
