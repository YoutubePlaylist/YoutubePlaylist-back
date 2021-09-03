package com.example.youtubedb.dto.play.response;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlayCreateResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "생성된 영상")
    private final Play response;

    public PlayCreateResponseDto(Play response) {
        this.response = response;
    }
}
