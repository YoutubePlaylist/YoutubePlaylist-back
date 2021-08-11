package com.example.youtubedb.domain;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Playlist extends BaseEntity {
    @Setter
    private String title;
    private boolean isPublic;
    private long likeCnt = 0;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;
    @JsonIgnore
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<Play> plays = new ArrayList<>();

    @Transient
    @Setter
    private String thumbnail;

    @Builder
    public Playlist(String title, boolean isPublic, Category category) {
        this.title = title;
        this.isPublic = isPublic;
        this.category = category;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getPlaylists().remove(this);
        }
        this.member = member;
        member.getPlaylists().add(this);
    }
}
