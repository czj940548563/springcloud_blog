package com.czj.blog.blogauth.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: clownc
 * @Date: 2019-04-15 15:52
 */
public class DateUtil {
    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentTime(){
        Date getDate = Calendar.getInstance().getTime();
        String dateStr= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDate);
        return dateStr;
    }
}
