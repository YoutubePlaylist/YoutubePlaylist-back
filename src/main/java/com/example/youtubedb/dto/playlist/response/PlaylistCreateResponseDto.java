package com.example.youtubedb.dto.playlist.response;

import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlaylistCreateResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "생성된 플레이리스트")
    private final Playlist response;

    public PlaylistCreateResponseDto(Playlist response) {
        this.response = response;
    }
}
