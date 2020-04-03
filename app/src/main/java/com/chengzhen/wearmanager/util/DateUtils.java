package com.chengzhen.wearmanager.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    public static String longToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm",Locale.CHINA);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String longToString(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss", Locale.CHINA);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String longToWholeDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String longToDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.CHINA);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String longToMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd", Locale.CHINA);
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static List<Long> getTimeList(){

        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-(29-i));
            long millis = calendar.getTimeInMillis();
            longList.add(millis);
        }
        return longList;
    }
}
