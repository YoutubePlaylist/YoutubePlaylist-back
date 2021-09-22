package com.example.youtubedb.dto.playlist.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class PlaylistCreateRequestDto {
    @Schema(description = "제목", example = "영상 목록 1")
    @NotBlank
    private final String title;

    @Schema(description = "공개 여부", example = "true")
    @NotNull
    private final Boolean isPublic;

    @Schema(description = "카테고리", example = "GAME")
    @NotBlank
    private final String category;

    @Builder
    public PlaylistCreateRequestDto(String title, Boolean isPublic, String category) {
        this.title = title;
        this.isPublic = isPublic;
        this.category = category;
    }
}
