package com.example.youtubedb.dto.play.response;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.play.PlaySeqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class PlayEditSeqResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "수정 목록")
    private final List<PlaySeqDto> response;
    public PlayEditSeqResponseDto(List<PlaySeqDto> response) {
        this.response = response;
    }
}
