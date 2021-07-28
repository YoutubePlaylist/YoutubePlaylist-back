package com.example.youtubedb.exception;

import com.example.youtubedb.dto.ErrorDto;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({
            NotExistRequestValueException.class,
            DuplicateMemberException.class,
            NotExistMemberException.class,
            NotExistPlaylistException.class,
            StartAndEndTimeException.class
    })
    public ResponseEntity<?> badRequest(Exception e) {
        ResponseDto responseBody = ResponseUtil.getFailResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler({
            InvalidAccessException.class
    })
    public ResponseEntity<?> notAcceptable(Exception e) {
        ResponseDto responseBody = ResponseUtil.getFailResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseBody);
    }

    @ExceptionHandler({
            RuntimeException.class
    })
    public ResponseEntity<?> serverError(Exception e) {
        ResponseDto responseBody = ResponseUtil.getFailResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.internalServerError().body(responseBody);
    }
}
