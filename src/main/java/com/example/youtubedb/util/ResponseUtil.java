package com.example.youtubedb.util;

import com.example.youtubedb.dto.ErrorDto;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.dto.play.PlaySeqDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseUtil {
    public static ResponseDto getSuccessResponse(Object data) {
        return ResponseDto.builder()
                .success(true)
                .response(data)
                .error(null)
                .build();
    }

    public static ResponseDto getFailResponse(String message, int status) {
        ErrorDto error = getErrorDto(message, status);

        return ResponseDto.builder()
                .success(false)
                .response(null)
                .error(error)
                .build();
    }

    private static ErrorDto getErrorDto(String message, int status) {
        return ErrorDto.builder()
                .status(status)
                .message(message)
                .build();
    }

    public static Map<String, Object> getDeleteResponse(Object id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("deleted", true);
        return result;
    }

    public static Map<String, Object> getEditResponse(Object id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("edited", true);
        return result;
    }

    public static List<Map<String, Object>> getEditPlaysResponse(List<PlaySeqDto> list) {
        return list.stream().map(m -> getEditResponse(m.getId())).collect(Collectors.toList());
    }
}
