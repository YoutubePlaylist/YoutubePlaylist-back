package com.example.youtubedb.mapper;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.vo.PlayVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PlayMapper {
    PlayMapper INSTANCE = Mappers.getMapper(PlayMapper.class);

    PlayVO toPlayVO(Play play);
}
