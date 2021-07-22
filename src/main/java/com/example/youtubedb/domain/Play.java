package com.example.youtubedb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String videoId;
    private long start;
    private long end;
    private String thumbnail;
    private String title;
    private int sequence;
    private LocalDate publishedAt;
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Builder
    public Play(String videoId,
                long start,
                long end,
                String thumbnail,
                String title,
                int sequence,
                Playlist playlist) {
        this.videoId = videoId;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.title = title;
        this.sequence = sequence;
        this.playlist = playlist;
    }

    public void setPlaylist(Playlist playlist) {
        if (this.playlist != null) {
            this.playlist.getPlays().remove(this);
        }
        this.playlist = playlist;
        playlist.getPlays().add(this);
    }
}
