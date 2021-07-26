package com.example.youtubedb.util;

import com.example.youtubedb.exception.NotExistRequestValueException;

import java.util.List;

public class RequestUtil {
    public static void checkNeedValue(List<Object> requestList) {
        requestList.forEach(r -> {
            if(r == null) throw new NotExistRequestValueException();
        });
    }
}
