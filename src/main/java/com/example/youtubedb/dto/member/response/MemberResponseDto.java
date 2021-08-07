package com.example.youtubedb.dto.member.response;

import com.example.youtubedb.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String loginId;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getLoginId());
    }
}