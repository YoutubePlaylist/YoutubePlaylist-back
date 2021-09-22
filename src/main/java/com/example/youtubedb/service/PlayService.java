package com.example.youtubedb.service;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.play.PlaySeqDto;
import com.example.youtubedb.repository.PlayRepository;
import com.example.youtubedb.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.youtubedb.exception.*;

import java.util.Collections;
import java.util.Comparator;
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
            String loginId,
            String videoId,
            Long start,
            Long end,
            String thumbnail,
            String title,
            String channelAvatar,
            String channelTitle) {
        checkTime(start, end);
        RequestUtil.checkOwn(playlist.getMember().getLoginId(), loginId);

        int sequence = playlist.getPlays().size() + 1;

        Play play = Play.builder()
                .title(title)
                .videoId(videoId)
                .thumbnail(thumbnail)
                .sequence(sequence)
                .start(start)
                .end(end)
                .channelAvatar(channelAvatar)
                .channelTitle(channelTitle)
                .build();
        play.setPlaylist(playlist);
        playRepository.save(play);

        return play;
    }

    private void checkTime(Long start, Long end) {
        if ((start > end) || start < 0) {
            throw new StartAndEndTimeException();
        }
    }

    public List<Play> getPlaysInPlaylist(Playlist playlist, String loginId) {
        validateWatch(playlist, loginId);

        return playlist.getPlays();
    }

    private void validateWatch(Playlist playlist, String loginId) {
        if (!playlist.getMember().getLoginId().equals(loginId) && !playlist.isPublic()) {
            throw new InvalidAccessException();
        }
    }

    public void deletePlayById(Play play, String loginId) {
        RequestUtil.checkOwn(play.getPlaylist().getMember().getLoginId(), loginId);
        playRepository.delete(play);
    }

    public Play getPlayById(Long id) {
        return playRepository.findById(id).orElseThrow(NotExistPlayException::new);
    }

    public void editTime(Play play, String loginId, long start, long end) {
        RequestUtil.checkOwn(play.getPlaylist().getMember().getLoginId(), loginId);
        checkTime(start, end);
        play.setTime(start, end);
        playRepository.save(play);
    }

    public void editSeq(String loginId, Long playlistId, List<PlaySeqDto> seqList) {
        checkSeqList(seqList);
        seqList.forEach(p -> {
            Play play = getPlayById(p.getId());
            RequestUtil.checkOwn(playlistId, play.getPlaylist().getId());
            RequestUtil.checkOwn(loginId, play.getPlaylist().getMember().getLoginId());
            play.setSequence(p.getSequence());
        });
    }

    private void checkSeqList(List<PlaySeqDto> seqList) {
        int size = seqList.size();
        for (int i = 0; i < size; i++) {
            checkDuplicateSeq(seqList, seqList.get(i));
            checkSeqNum(seqList.get(i).getSequence(), size);
        }
    }

    private void checkDuplicateSeq(List<PlaySeqDto> seqList, PlaySeqDto playSeqDto) {
        if (Collections.frequency(seqList, playSeqDto) > 1) {
            throw new DuplicateSeqException();
        }
    }

    private void checkSeqNum(int sequence, int size) {
        if((sequence < 1) || (sequence > size)) {
            throw new InvalidSeqException();
        }
    }

    public void sortPlaysInPlaylist(Playlist playlist) {
        List<Play> plays = playlist.getPlays();
        plays.sort(new Comparator<Play>() {
            @Override
            public int compare(Play p1, Play p2) {
                return p1.getSequence() - p2.getSequence();
            }
        });
        int seq = 1;
        for (Play play : plays) {
            play.setSequence(seq++);
        }
    }
}
