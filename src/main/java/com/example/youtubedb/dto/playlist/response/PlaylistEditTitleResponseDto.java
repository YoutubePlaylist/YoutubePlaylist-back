package com.example.youtubedb.dto.playlist.response;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlaylistEditTitleResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "수정된 플레이리스트 아이디")
    private final Long id;
    @Schema(description = "수정 여부")
    private final boolean edited = true;

    public PlaylistEditTitleResponseDto(Long id) {
        this.id = id;
    }
}
