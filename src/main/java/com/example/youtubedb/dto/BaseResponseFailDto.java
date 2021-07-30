package com.example.youtubedb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class BaseResponseFailDto {
    private final boolean success = false;
    @Schema(defaultValue = "null")
    private final Object response = null;
}
