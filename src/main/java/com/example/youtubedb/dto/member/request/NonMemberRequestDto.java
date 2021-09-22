package com.example.youtubedb.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class NonMemberRequestDto {
    @Schema(description = "장치 ID", example = "device001")
    @NotBlank
    private final String deviceId;

    @Schema(description = "PC여부", example = "false")
    @NotNull
    private final Boolean isPC;

    @Builder
    public NonMemberRequestDto(String deviceId, Boolean isPC) {
        this.deviceId = deviceId;
        this.isPC = isPC;
    }
}
