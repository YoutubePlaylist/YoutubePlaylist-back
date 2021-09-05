package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NonMemberRequestDto {
    @Schema(description = "장치 ID", example = "device001")
    private final String deviceId;
    @Schema(description = "PC여부", example = "false")
    private final Boolean isPC;

    @Builder
    public NonMemberRequestDto(String deviceId, Boolean isPC) {
        this.deviceId = deviceId;
        this.isPC = isPC;
    }
}
