package com.example.youtubedb.dto.play.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class PlayCreateRequestDto {
    @Schema(description = "플레이 리스트 아이디" , example = "1")
    @NotNull
    private final Long playlistId;

    @Schema(description = "비디오 아이디" , example = "video123")
    @NotBlank
    private final String videoId;

    @Schema(description = "시작 시간" , example = "100")
    @NotNull
    private final Long start;

    @Schema(description = "끝 시간" , example = "1000")
    @NotNull
    private final Long end;

    @Schema(description = "썸네일 이미지 주소" , example = "img1")
    @NotBlank
    private final String thumbnail;

    @Schema(description = "제목" , example = "제목1")
    @NotBlank
    private final String title;

    @Schema(description = "채널 아바타 이미지 주소" , example = "img2")
    @NotBlank
    private final String channelAvatar;

    @NotBlank
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
