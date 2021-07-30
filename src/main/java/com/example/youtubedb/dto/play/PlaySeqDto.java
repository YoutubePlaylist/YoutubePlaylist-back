package com.example.youtubedb.dto.play;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class PlaySeqDto {
    @Schema(description = "영상 아이디", example = "1")
    private Long id;
    @Schema(description = "영상 순서", example = "1")
    private int sequence;

    @Builder
    public PlaySeqDto(Long id, int sequence) {
        this.id = id;
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaySeqDto that = (PlaySeqDto) o;
        return sequence == that.sequence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence);
    }
}
