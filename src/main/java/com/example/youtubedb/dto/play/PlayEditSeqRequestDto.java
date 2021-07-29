package com.example.youtubedb.dto.play;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
public class PlayEditSeqRequestDto {
    private String loginId;
    private Long playlistId;
    private List<PlaySeqDto> seqList;

    @Builder
    public PlayEditSeqRequestDto(String loginId, Long playlistId, List<PlaySeqDto> seqList) {
        this.loginId = loginId;
        this.playlistId = playlistId;
        this.seqList = seqList;
    }
}
