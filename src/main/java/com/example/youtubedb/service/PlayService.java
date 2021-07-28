package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.exception.InvalidAccessException;
import com.example.youtubedb.exception.StartAndEndTimeException;
import com.example.youtubedb.repository.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlayService {
    private final PlayRepository playRepository;

    @Autowired
    public PlayService(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    public Play addPlayToPlaylist(
            Playlist playlist,
            String videoId,
            Long start,
            Long end,
            String thumbnail,
            String title,
            String channelAvatar) {
        checkTime(start, end);

        int sequence = playlist.getPlays().size() + 1;

        Play play = Play.builder()
                .title(title)
                .videoId(videoId)
                .thumbnail(thumbnail)
                .sequence(sequence)
                .start(start)
                .end(end)
                .channelAvatar(channelAvatar)
                .build();
        play.setPlaylist(playlist);

        return play;
    }

    private void checkTime(Long start, Long end) {
        if ((start > end) || start < 0) {
            throw new StartAndEndTimeException();
        }
    }

    public List<Play> getPlaysInPlaylist(Playlist playlist, Member member) {
        validateWatch(playlist, member);

        return playlist.getPlays();
    }

    private void validateWatch(Playlist playlist, Member member) {
        if (!playlist.getMember().getId().equals(member.getId()) && !playlist.isPublic()) {
            throw new InvalidAccessException();
        }
    }
}
