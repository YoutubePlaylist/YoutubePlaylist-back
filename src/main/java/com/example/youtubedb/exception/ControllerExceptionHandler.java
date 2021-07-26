package com.example.youtubedb.exception;

import com.example.youtubedb.dto.ErrorDto;
import com.example.youtubedb.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({
            NotExistRequestValueException.class,
            DuplicateMemberException.class
    })
    public ResponseEntity<?> badRequest(Exception e) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        ResponseDto responseDto = ResponseDto.builder()
                .success(false)
                .response(null)
                .error(errorDto)
                .build();

        return ResponseEntity.badRequest().body(responseDto);
    }

    @ExceptionHandler({
            RuntimeException.class
    })
    public ResponseEntity<?> serverError(Exception e) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        ResponseDto responseDto = ResponseDto.builder()
                .success(false)
                .response(null)
                .error(errorDto)
                .build();

        return ResponseEntity.internalServerError().body(responseDto);
    }
}
