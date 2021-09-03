package com.example.youtubedb.dto.playlist.response;

import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaylistGetResponseDto extends BaseResponseSuccessDto {
    @Schema(description = "플레이리스트 목록")
    private final List<Playlist> response;

    public PlaylistGetResponseDto(List<Playlist> response) {
        this.response = response;
    }
}
