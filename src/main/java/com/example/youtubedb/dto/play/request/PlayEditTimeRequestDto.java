package com.example.youtubedb.dto.play.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayEditTimeRequestDto {
    @Schema(description = "플레이 아이디", example = "1")
    private Long id;
    @Schema(description = "시작 시간", example = "100")
    private Long start;
    @Schema(description = "끝 시간", example = "1000")
    private Long end;

    @Builder
    public PlayEditTimeRequestDto(Long id, Long start, Long end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }
}
