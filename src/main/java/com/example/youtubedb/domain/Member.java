package com.example.youtubedb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String displayName;
    @OneToMany(mappedBy = "member")
    private List<Playlist> playlists = new ArrayList<>();

    @Builder
    public Member(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
    }
}
