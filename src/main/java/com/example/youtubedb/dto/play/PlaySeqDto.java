package com.example.youtubedb.dto.play;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlaySeqDto {
    @Schema(description = "영상 아이디", example = "1")
    private Long id;

    @EqualsAndHashCode.Include
    @Schema(description = "영상 순서", example = "1")
    private int sequence;

    @Builder
    public PlaySeqDto(Long id, int sequence) {
        this.id = id;
        this.sequence = sequence;
    }
}
