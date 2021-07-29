package com.example.youtubedb.dto.play;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class PlaySeqDto {
    private Long id;
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
