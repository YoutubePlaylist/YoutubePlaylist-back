package com.example.youtubedb.util;

import com.example.youtubedb.exception.NotExistRequestValueException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RequestUtilTest {

    @Test
    void 요청값_존재X() {
        // given
        String param1 = "params";
        Integer param2 = 3;
        String params3 = null;
        List<Object> requestList = new ArrayList<>();
        requestList.add(param1);
        requestList.add(param2);
        requestList.add(params3);

        // when
        Exception e = assertThrows(NotExistRequestValueException.class, () -> RequestUtil.checkNeedValue(requestList));

        // then
        assertThat(e.getMessage()).isEqualTo("필요값이 없습니다.");
    }
}