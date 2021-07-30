package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NonMemberCreateRequestDto {
    @Schema(description = "장치 ID", example = "device001")
    private String deviceId;

    @Builder
    public NonMemberCreateRequestDto(String deviceId) {
        this.deviceId = deviceId;
    }
}
