package com.example.youtubedb.dto.play.request;

import com.example.youtubedb.dto.play.PlaySeqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
public class PlayEditSeqRequestDto {
    @Schema(description = "로그인 아이디", example = "tester")
    private final Long playlistId;
    @Schema(description = "영상 순서 목록")
    private final List<PlaySeqDto> seqList;

    @Builder
    public PlayEditSeqRequestDto(Long playlistId, List<PlaySeqDto> seqList) {
        this.playlistId = playlistId;
        this.seqList = seqList;
    }
}
