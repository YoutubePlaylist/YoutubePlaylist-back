package com.example.youtubedb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Play extends BaseEntity {
    private String videoId;
    private long start;
    private long end;
    private String thumbnail;
    private String title;
    @Setter
    private int sequence;
    private String channelAvatar;
    private String channelTitle;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Builder
    public Play(String videoId,
                long start,
                long end,
                String thumbnail,
                String title,
                int sequence,
                String channelAvatar,
                String channelTitle) {
        this.videoId = videoId;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.title = title;
        this.sequence = sequence;
        this.channelAvatar = channelAvatar;
        this.channelTitle = channelTitle;
    }

    public void setPlaylist(Playlist playlist) {
        if (this.playlist != null) {
            this.playlist.getPlays().remove(this);
        }
        this.playlist = playlist;
        playlist.getPlays().add(this);
    }

    public void setTime(long start, long end) {
        this.start = start;
        this.end = end;
    }
}
