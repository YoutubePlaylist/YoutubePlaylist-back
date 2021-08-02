package com.example.youtubedb.dto.playlist.response;

import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlaylistDeleteResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "삭제된 플레이리스트 아이디")
    private final Long id;
    @Schema(description = "삭제 여부")
    private final boolean deleted = true;

    public PlaylistDeleteResponseDto(Long id) {
        this.id = id;
    }
}
