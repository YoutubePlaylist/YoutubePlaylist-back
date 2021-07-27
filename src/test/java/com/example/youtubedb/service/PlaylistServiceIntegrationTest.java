package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.Category;
import com.example.youtubedb.exception.InvalidAccessException;
import com.example.youtubedb.exception.NotExistPlaylistException;
import com.example.youtubedb.exception.NotExistRequestValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaylistServiceIntegrationTest {
    @Autowired
    PlaylistService playlistService;
    @Autowired
    MemberService memberService;

    @Test
    void 플레이리스트_생성() {
        // given
        Member member = memberService.registerNon("device001");
        String title = "myList";
        String isPublic = "true";
        String category = "GAME";

        // when
        Playlist playlist = playlistService.createPlaylist(title, isPublic, category, member);

        // then
        assertAll(
                () -> assertThat(playlist.getTitle()).isEqualTo(title),
                () -> assertThat(playlist.getCategory()).isEqualTo(Category.GAME),
                () -> assertThat(playlist.isPublic()).isEqualTo(true),
                () -> assertThat(playlist.getMember()).isEqualTo(member),
                () -> assertThat(playlist.getLikeCnt()).isEqualTo(0)
        );
    }

    @Test
    void 플레이리스트_생성_Category이상() {
        // given
        Member member = memberService.registerNon("device001");
        String title = "myList";
        String isPublic = "true";
        String category = "???";

        // when
        Exception e = assertThrows(NotExistRequestValueException.class, () -> playlistService.createPlaylist(title, isPublic, category, member));

        // then
        assertThat(e.getMessage()).isEqualTo("필요값이 없습니다.");
    }

    @Test
    void 플레이리스트_조회() {
        // given
        Member member = memberService.registerNon("devide001");
        playlistService.createPlaylist("myList", "false", "GAME", member);
        playlistService.createPlaylist("myList2", "false", "OTHER", member);

        // when
        List<Playlist> playlists = member.getPlaylists();

        // then
        assertThat(playlists.size()).isEqualTo(2);
    }

    @Test
    void 플레이리스트_수정() {
        // given
        Member member = memberService.registerNon("devide001");
        Playlist playlist = playlistService.createPlaylist("myList", "false", "GAME", member);
        String newTitle = "newList";
        // when
        Playlist newList = playlistService.editPlaylistTitle(playlist.getId().toString(), newTitle, member);

        // then
        assertThat(newList.getTitle()).isEqualTo(newTitle);
    }

    @Test
    void 플레이리스트_존재X() {
        // given
        Member member = memberService.registerNon("devide001");
        Playlist playlist = playlistService.createPlaylist("myList", "false", "GAME", member);
        String newTitle = "newList";
        // when
        Exception e = assertThrows(NotExistPlaylistException.class, () -> playlistService.editPlaylistTitle("100", newTitle, member));

        // then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 플레이리스트입니다.");
    }

    @Test
    void 플레이리스트_삭제() {
        // given
        Member member = memberService.registerNon("devide001");
        Playlist playlist = playlistService.createPlaylist("myList", "false", "GAME", member);

        // when
        playlistService.deletePlaylistById(playlist.getId().toString(), member);
        Exception e = assertThrows(NotExistPlaylistException.class,
                () -> playlistService.editPlaylistTitle(playlist.getId().toString(), "newTitle", member)
        );

        // then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 플레이리스트입니다.");
    }

    @Test
    void 타유저_접근() {
        // given
        Member member1 = memberService.registerNon("device001");
        Member member2 = memberService.registerNon("device002");
        Playlist playlist = playlistService.createPlaylist("title", "false", "OTHER", member1);

        // when
        Exception e = assertThrows(InvalidAccessException.class, () -> playlistService.deletePlaylistById(playlist.getId().toString(), member2));

        // then
        assertThat(e.getMessage()).isEqualTo("올바르지 못한 접근입니다.");
    }
}