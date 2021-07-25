package com.example.youtubedb.domain;

import com.example.youtubedb.dto.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean isPublic;
    private long likeCnt = 0;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "playlist")
    private List<Play> plays = new ArrayList<>();

    @Builder
    public Playlist(boolean isPublic, Category category, Member member) {
        this.isPublic = isPublic;
        this.category = category;
        this.member = member;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getPlaylists().remove(this);
        }
        this.member = member;
        member.getPlaylists().add(this);
    }
}
