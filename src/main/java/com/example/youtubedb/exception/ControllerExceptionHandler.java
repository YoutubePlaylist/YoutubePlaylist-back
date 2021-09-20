package com.example.youtubedb.exception;

import com.example.youtubedb.dto.BaseResponseFailDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.NotAcceptableFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {
            NotExistRequestValueException.class,
            DuplicateMemberException.class,
            NotExistMemberException.class,
            NotExistPlaylistException.class,
            StartAndEndTimeException.class,
            NotExistPlayException.class,
            DuplicateSeqException.class,
            InvalidSeqException.class,
            DoNotMatchPasswordException.class,
            RefreshTokenException.class,
            InvalidBlankPasswordException.class,
            InvalidRegexPasswordException.class,
            OverNomMemberMaxListException.class,
            NotMemberException.class,
            DoNotChangePasswordException.class
    })
    public ResponseEntity<?> badRequest(Exception e) {
        BaseResponseFailDto responseBody = BadRequestFailResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseBody);
    }

    @ExceptionHandler({
            InvalidAccessException.class
    })
    public ResponseEntity<?> notAcceptable(Exception e) {
        BaseResponseFailDto responseBody = NotAcceptableFailResponseDto.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseBody);
    }

    @ExceptionHandler({
            RuntimeException.class
    })
    public ResponseEntity<?> serverError(Exception e) {
        BaseResponseFailDto responseBody = ServerErrorFailResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationException(MethodArgumentNotValidException e) {
        BaseResponseFailDto responseBody = BadRequestFailResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
        System.out.println(e.getMessage().getClass());

        return ResponseEntity.badRequest().body(responseBody);
    }
}
