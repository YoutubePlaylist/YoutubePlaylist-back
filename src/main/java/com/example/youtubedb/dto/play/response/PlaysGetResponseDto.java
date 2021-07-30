package com.example.youtubedb.dto.play.response;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaysGetResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "리스트 내 영상 목록")
    private final List<Play> response;

    public PlaysGetResponseDto(List<Play> response) {
        this.response = response;
    }
}
