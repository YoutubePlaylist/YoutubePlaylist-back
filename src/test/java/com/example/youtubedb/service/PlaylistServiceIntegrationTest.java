package com.example.youtubedb.service;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.Category;
import com.example.youtubedb.exception.NotExistPlaylistException;
import com.example.youtubedb.exception.NotExistRequestValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PlaylistServiceIntegrationTest {
    @Autowired
    PlaylistService playlistService;
    @Autowired
    MemberService memberService;
    @Autowired
    PlayService playService;

    @Test
    void 플레이리스트_생성() {
        // given
        Member member = memberService.registerNon("device001", true);
        String title = "myList";
        Boolean isPublic = true;
        String category = "GAME";

        // when
        Playlist playlist = playlistService.createPlaylist(title, isPublic, category, member);

        // then
        assertAll(
                () -> assertThat(playlist.getTitle()).isEqualTo(title),
                () -> assertThat(playlist.getCategory()).isEqualTo(Category.GAME),
                () -> assertThat(playlist.isPublic()).isEqualTo(true),
                () -> assertThat(playlist.getMember().getId()).isEqualTo(member.getId()),
                () -> assertThat(playlist.getLikeCnt()).isEqualTo(0)
        );
    }

    @Test
    void 플레이리스트_생성_Category이상() {
        // given
        Member member = memberService.registerNon("device001", true);
        String title = "myList";
        Boolean isPublic = true;
        String category = "???";

        // when
        Exception e = assertThrows(NotExistRequestValueException.class, () -> playlistService.createPlaylist(title, isPublic, category, member));

        // then
        assertThat(e.getMessage()).isEqualTo(NotExistRequestValueException.getErrorMessage());
    }

    @Test
    void 플레이리스트_조회() {
        // given
        Member member = memberService.registerNon("devide001",true);
        String title = "myList";
        Boolean isPublic = false;
        String category = "GAME";
        Playlist playlist = playlistService.createPlaylist(title, isPublic, category, member);

        // when
        Playlist result = playlistService.getPlaylistById(playlist.getId());

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(playlist.getId()),
                () -> assertThat(result.getTitle()).isEqualTo(title),
                () -> assertThat(result.isPublic()).isEqualTo(isPublic),
                () -> assertThat(result.getCategory()).isEqualTo(Category.valueOf(category)),
                () -> assertThat(result.getMember().getId()).isEqualTo(member.getId())
        );
    }

    @Test
    void 플레이리스트_수정() {
        // given
        Member member = memberService.registerNon("devide001",true);
        Playlist playlist = playlistService.createPlaylist("myList", false, "GAME", member);
        String newTitle = "newList";
        // when
        Playlist newList = playlistService.editPlaylistTitle(playlist.getId(), newTitle, member.getLoginId());

        // then
        assertThat(newList.getTitle()).isEqualTo(newTitle);
    }

    @Test
    void 플레이리스트_존재X() {
        // given
        Member member = memberService.registerNon("devide001",true);
        Playlist playlist = playlistService.createPlaylist("myList", false, "GAME", member);
        // when
        Exception e = assertThrows(NotExistPlaylistException.class, () -> playlistService.getPlaylistById(100L));

        // then
        assertThat(e.getMessage()).isEqualTo(NotExistPlaylistException.getErrorMessage());
    }

    @Test
    void 플레이리스트_삭제() {
        // given
        Member member = memberService.registerNon("device001",true);
        Playlist playlist = playlistService.createPlaylist("myList", false, "GAME", member);

        // when
        playlistService.deletePlaylistById(playlist.getId(), member.getLoginId());
        Exception e = assertThrows(NotExistPlaylistException.class,
                () -> playlistService.getPlaylistById(playlist.getId())
        );

        // then
        assertThat(e.getMessage()).isEqualTo(NotExistPlaylistException.getErrorMessage());
    }

    @Test
    void 썸네일_설정() {
        // given
        Member member = memberService.registerNon("device001",true);
        Playlist playlist = playlistService.createPlaylist("myList", false, "GAME", member);
        String thumbnail = "thumbnail";

        // when
        Play play = Play.builder()
                .end(100L)
                .start(20L)
                .sequence(1)
                .title("title")
                .videoId("video001")
                .thumbnail(thumbnail)
                .channelAvatar("avatar")
                .build();

        play.setPlaylist(playlist);
        playlistService.addThumbnail(member.getPlaylists());

        // then
        assertThat(playlist.getThumbnail()).isEqualTo(thumbnail);
    }
}