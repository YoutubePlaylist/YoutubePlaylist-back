package com.example.youtubedb.dto.playlist.request;

import com.example.youtubedb.util.RequestUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaylistCreateRequestDto {
    @Schema(description = "로그인 아이디", example = "tester")
    private String loginId;
    @Schema(description = "제목", example = "영상 목록 1")
    private String title;
    @Schema(description = "공개 여부", example = "true")
    private Boolean isPublic;
    @Schema(description = "카테고리", example = "GAME")
    private String category;

    @Builder
    public PlaylistCreateRequestDto(String loginId, String title, Boolean isPublic, String category) {
        this.loginId = loginId;
        this.title = title;
        this.isPublic = isPublic;
        this.category = category;
    }
}
