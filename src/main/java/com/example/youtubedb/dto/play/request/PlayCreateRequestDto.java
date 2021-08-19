package com.example.youtubedb.dto.play.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PlayCreateRequestDto {
    @Schema(description = "플레이 리스트 아이디" , example = "1")
    private final Long playlistId;
    @Schema(description = "비디오 아이디" , example = "video123")
    private final String videoId;
    @Schema(description = "시작 시간" , example = "100")
    private final Long start;
    @Schema(description = "끝 시간" , example = "1000")
    private final Long end;
    @Schema(description = "썸네일 이미지 주소" , example = "img1")
    private final String thumbnail;
    @Schema(description = "제목" , example = "제목1")
    private final String title;
    @Schema(description = "채널 아바타 이미지 주소" , example = "img2")
    private final String channelAvatar;
    @Schema(description = "채널 이름" , example = "채널이름1")
    private final String channelTitle;

    @Builder
    public PlayCreateRequestDto(
            Long playlistId,
            String videoId,
            Long start,
            Long end,
            String thumbnail,
            String title,
            String channelAvatar,
            String channelTitle) {
        this.playlistId = playlistId;
        this.videoId = videoId;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.title = title;
        this.channelAvatar = channelAvatar;
        this.channelTitle =channelTitle;
    }
}
