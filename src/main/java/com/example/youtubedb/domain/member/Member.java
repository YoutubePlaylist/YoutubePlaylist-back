package com.example.youtubedb.domain.member;

import com.example.youtubedb.domain.BaseEntity;
import com.example.youtubedb.domain.Playlist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    private String loginId;

    @JsonIgnore
    private String password;
    private boolean isMember;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @Transient
    private boolean isPc;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Playlist> playlists = new ArrayList<>();

    @Builder
    public Member(String loginId, String password, boolean isMember, Authority authority, boolean isPc){
        this.loginId = loginId;
        this.password = password;
        this.isMember = isMember;
        this.authority = authority;
        this.isPc = isPc;
    }
}
