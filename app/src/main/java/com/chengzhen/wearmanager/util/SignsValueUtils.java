package com.chengzhen.wearmanager.util;

import com.blankj.utilcode.util.StringUtils;

public class SignsValueUtils {

    public static String judgeEmpty(Object value){

//        if(TextUtils.isEmpty(value)) {
        if(value == null) {
            return "----";
        } else {
            if(value instanceof String && StringUtils.isTrimEmpty((String) value)) {
                return "----";
            }
            return value + "";
        }
    }

    public static String judgeTime(long time){

        if(time == 0) {
            return "----";
        } else {
            return DateUtils.longToString(time);
        }
    }
}
