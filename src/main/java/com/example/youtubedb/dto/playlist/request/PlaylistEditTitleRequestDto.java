package com.example.youtubedb.dto.playlist.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PlaylistEditTitleRequestDto {
    @Schema(description = "플레이 리스트 아이디", example = "1")
    private final Long id;
    @Schema(description = "제목", example = "영상 목록 1")
    private final String title;

    @Builder
    public PlaylistEditTitleRequestDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
