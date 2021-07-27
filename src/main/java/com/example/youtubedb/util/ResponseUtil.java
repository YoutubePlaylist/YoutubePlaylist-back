package com.example.youtubedb.util;

import com.example.youtubedb.dto.ErrorDto;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.exception.NotExistRequestValueException;

import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Object> getDeleteResponse(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("deleted", true);
        return result;
    }

    public static Map<String, Object> getEditResponse(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("edited", true);
        return result;
    }
}
