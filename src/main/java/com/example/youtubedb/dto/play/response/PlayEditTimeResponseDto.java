package com.example.youtubedb.dto.play.response;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlayEditTimeResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "수정된 영상 아이디")
    private final Long id;
    @Schema(description = "수정 여부")
    private final boolean edited = true;

    public PlayEditTimeResponseDto(Long id) {
        this.id = id;
    }
}
