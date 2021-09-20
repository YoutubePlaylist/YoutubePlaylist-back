package com.example.youtubedb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class BaseResponseSuccessDto {
    @NotNull
    private final boolean success = true;
    @Schema(defaultValue = "null")
    private final ErrorDto error = null;
}
