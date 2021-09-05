package com.example.youtubedb.domain.member;

import com.example.youtubedb.domain.BaseEntity;
import com.example.youtubedb.domain.Playlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    private String loginId;

    @JsonIgnore
    private String password;
    private boolean isMember;
    @Setter
    private String profileImg = null;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @JsonIgnore
    @Transient
    private boolean isPC;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Playlist> playlists = new ArrayList<>();

    @Builder
    public Member(String loginId, String password, boolean isMember, Authority authority, boolean isPC){
        this.loginId = loginId;
        this.password = password;
        this.isMember = isMember;
        this.authority = authority;
        this.isPC = isPC;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public void changeToMember(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        this.isMember = true;
    }
}
