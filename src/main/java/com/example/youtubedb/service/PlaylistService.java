package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.Category;
import com.example.youtubedb.exception.InvalidAccessException;
import com.example.youtubedb.exception.NotExistPlaylistException;
import com.example.youtubedb.exception.NotExistRequestValueException;
import com.example.youtubedb.repository.PlaylistRepository;
import com.example.youtubedb.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist createPlaylist(String title, String isPublic, String category, Member member) {
        checkCategory(category);
        Playlist playlist = Playlist.builder()
                .title(title)
                .isPublic(Boolean.parseBoolean(isPublic))
                .category(Category.valueOf(category))
                .build();
        playlist.setMember(member);

        return playlistRepository.save(playlist);
    }

    private void checkCategory(String category) {
        Category[] categories = Category.values();
        int count = 0;
        if (Arrays.stream(categories).noneMatch(c -> c.toString().equals(category))) {
            throw new NotExistRequestValueException();
        }
    }

    public Playlist editPlaylistTitle(String id, String title, String loginId) {
        Long lId = Long.parseLong(id);
        Playlist playlist = getPlaylistById(lId);
        RequestUtil.checkOwn(playlist.getMember().getLoginId(), loginId);

        playlist.setTitle(title);

        return playlist;
    }

    public void deletePlaylistById(String id, String loginId) {
        Long lId = Long.parseLong(id);
        Playlist playlist = getPlaylistById(lId);
        RequestUtil.checkOwn(playlist.getMember().getLoginId(), loginId);

        playlistRepository.deleteById(lId);
    }

    public Playlist getPlaylistById(Long lId) {
        return playlistRepository.findById(lId).orElseThrow(NotExistPlaylistException::new);
    }
}
