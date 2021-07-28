package com.example.youtubedb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Play extends BaseEntity {
    private String videoId;
    private long start;
    private long end;
    private String thumbnail;
    private String title;
    private int sequence;
    private String channelAvatar;
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
                String channelAvatar) {
        this.videoId = videoId;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.title = title;
        this.sequence = sequence;
        this.channelAvatar = channelAvatar;
    }

    public void setPlaylist(Playlist playlist) {
        if (this.playlist != null) {
            this.playlist.getPlays().remove(this);
        }
        this.playlist = playlist;
        playlist.getPlays().add(this);
    }
}
