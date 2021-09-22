package com.example.youtubedb.vo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PlayVO {
    private final String videoId;
    private final long start;
    private final long end;
    private final String thumbnail;
    private final String title;
    private final int sequence;
    private final String channelAvatar;
    private final String channelTitle;

    @Override
    public String toString() {
        return "PlayVO{" +
                "videoId='" + videoId + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", sequence=" + sequence +
                ", channelAvatar='" + channelAvatar + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                '}';
    }

    public boolean sameAs(PlayVO anotherPlayVO) {
        return videoId.equals(anotherPlayVO.videoId) &&
                start == anotherPlayVO.start &&
                end == anotherPlayVO.end &&
                thumbnail.equals(anotherPlayVO.thumbnail) &&
                title.equals(anotherPlayVO.title) &&
                sequence == anotherPlayVO.sequence &&
                channelAvatar.equals(anotherPlayVO.channelAvatar) &&
                channelTitle.equals(anotherPlayVO.channelTitle);
    }
}
