package com.example.youtubedb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class BaseResponseSuccessDto {
    private final boolean success = true;
    @Schema(defaultValue = "null")
    private final ErrorDto error = null;
}
