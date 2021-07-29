package com.example.youtubedb.util;

import com.example.youtubedb.exception.InvalidAccessException;
import com.example.youtubedb.exception.NotExistRequestValueException;

import java.util.List;

public class RequestUtil {
    public static void checkNeedValue(Object ...args) {
        for(Object arg : args) {
            if(arg == null) throw new NotExistRequestValueException();
        }
    }

    public static void checkOwn(Object target, Object origin) {
        if (!target.equals(origin)) {
            throw new InvalidAccessException();
        }
    }
}
