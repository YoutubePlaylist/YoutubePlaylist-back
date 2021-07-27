package com.example.youtubedb.util;

import com.example.youtubedb.exception.NotExistRequestValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestUtilTest {

    @Test
    void 요청값_존재X() {
        // given
        String param1 = "params";
        Integer param2 = 3;
        String param3 = null;

        // when
        Exception e = assertThrows(NotExistRequestValueException.class, () -> RequestUtil.checkNeedValue(param1, param2, param3));

        // then
        assertThat(e.getMessage()).isEqualTo("필요값이 없습니다.");
    }
}