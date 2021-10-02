package com.example.youtubedb.service;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.Category;
import com.example.youtubedb.exception.NotExistPlaylistException;
import com.example.youtubedb.exception.NotExistRequestValueException;
import com.example.youtubedb.exception.OverNomMemberMaxListException;
import com.example.youtubedb.repository.interfaces.PlaylistRepository;
import com.example.youtubedb.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Transactional
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    private final int NON_MEMBER_MAX_PLAYLISTS = 5;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist createPlaylist(String title, Boolean isPublic, String category, Member member) {
        checkCategory(category);
        checkNomMemberCount(member);
        Playlist playlist = Playlist.builder()
                .title(title)
                .isPublic(isPublic)
                .category(Category.valueOf(category))
                .build();
        playlist.setMember(member);

        return playlistRepository.save(playlist);
    }

    private void checkNomMemberCount(Member member) {
        if(!member.isMember() && member.getPlaylists().size() >= NON_MEMBER_MAX_PLAYLISTS){
            log.info("isMember, listSize = {} {}",member.isMember(), member.getPlaylists().size());
            throw new OverNomMemberMaxListException();
        }

    }

    private void checkCategory(String category) {
        Category[] categories = Category.values();
        int count = 0;
        if (Arrays.stream(categories).noneMatch(c -> c.toString().equals(category))) {
            System.out.println(category);
            throw new NotExistRequestValueException();
        }
    }

    public Playlist editPlaylistTitle(Long id, String title, String loginId) {
        Playlist playlist = getPlaylistById(id);
//        RequestUtil.checkOwn(playlist.getMember().getLoginId(), loginId);
        playlist.setTitle(title);

        return playlist;
    }

    public void deletePlaylistById(Long id, String loginId) {
        Playlist playlist = getPlaylistById(id);
        RequestUtil.checkOwn(playlist.getMember().getLoginId(), loginId);

        playlistRepository.delete(playlist);
    }

    public Playlist getPlaylistById(Long lId) {
        return playlistRepository.findById(lId).orElseThrow(NotExistPlaylistException::new);
    }

    public void addThumbnail(List<Playlist> playlists) {
        playlists.forEach(playlist -> {
            String thumbnail = getThumbnailInPlaylist(playlist.getPlays());
            playlist.setThumbnail(thumbnail);
        });
    }

    private String getThumbnailInPlaylist(List<Play> plays) {
        for (Play play : plays) {
            if (play.getSequence() == 1) {
                return play.getThumbnail();
            }
        }
        return null;
    }
}
