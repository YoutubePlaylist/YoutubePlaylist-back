package com.example.youtubedb.adapter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.sql.Timestamp.valueOf;

public class DateAdapter{

    public static LocalDateTime toLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime localDateTime){
        return valueOf(localDateTime);
    }
}
