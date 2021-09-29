package com.example.youtubedb.mapper;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.vo.PlayVO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-29T15:12:35+0900",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.11 (AdoptOpenJDK)"
)
public class PlayMapperImpl implements PlayMapper {

    @Override
    public PlayVO toPlayVO(Play play) {
        if ( play == null ) {
            return null;
        }

        String videoId = null;
        long start = 0L;
        long end = 0L;
        String thumbnail = null;
        String title = null;
        int sequence = 0;
        String channelAvatar = null;
        String channelTitle = null;

        videoId = play.getVideoId();
        start = play.getStart();
        end = play.getEnd();
        thumbnail = play.getThumbnail();
        title = play.getTitle();
        sequence = play.getSequence();
        channelAvatar = play.getChannelAvatar();
        channelTitle = play.getChannelTitle();

        PlayVO playVO = new PlayVO( videoId, start, end, thumbnail, title, sequence, channelAvatar, channelTitle );

        return playVO;
    }
}
