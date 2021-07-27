package com.example.youtubedb.util;

import com.example.youtubedb.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ResponseUtilTest {
    @Test
    void 성공_응답() {
        // given
        String data = "DATA";

        // when
        ResponseDto responseBody = ResponseUtil.getSuccessResponse(data);

        // then
        assertAll(
                () -> assertThat(responseBody.isSuccess()).isEqualTo(true),
                () -> assertThat(responseBody.getResponse()).isEqualTo(data),
                () -> assertThat(responseBody.getError()).isEqualTo(null)
        );
    }

    @Test
    void 실패_응답() {
        // given
        String message = "오류입니다.";
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        // when
        ResponseDto responseBody = ResponseUtil.getFailResponse(message, status);

        // then
        assertAll(
                () -> assertThat(responseBody.isSuccess()).isEqualTo(false),
                () -> assertThat(responseBody.getResponse()).isEqualTo(null),
                () -> assertThat(responseBody.getError().getMessage()).isEqualTo(message),
                () -> assertThat(responseBody.getError().getStatus()).isEqualTo(status)
        );
    }

    @Test
    void 수정시_응답객체() {
        // given
        String id = "1";

        // then
        Map<String, Object> result = ResponseUtil.getEditResponse(id);

        // when
        assertAll(
                () -> assertThat(result.get("id")).isEqualTo(id),
                () -> assertThat(result.get("edited")).isEqualTo(true)
        );
    }

    @Test
    void 삭제시_응답객체() {
        // given
        String id = "1";

        // then
        Map<String, Object> result = ResponseUtil.getDeleteResponse(id);

        // when
        assertAll(
                () -> assertThat(result.get("id")).isEqualTo(id),
                () -> assertThat(result.get("deleted")).isEqualTo(true)
        );
    }
}