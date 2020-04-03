package com.chengzhen.wearmanager.util;

import android.util.Log;

import com.chengzhen.wearmanager.Constant;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class SignsTimeFormatter extends ValueFormatter {

    private List<Long> mLongList;

    @Override
    public String getFormattedValue(float value) {

        if(mLongList != null && mLongList.size() == 1) {
            return DateUtils.longToMonth(mLongList.get(0));
        }
        Log.d("--TAG3--", "getFormattedValue: " + value * Constant.BIG_NUM);
        return DateUtils.longToMonth(mLongList.get((int) value));
    }

    public void setLongList(List<Long> longList) {
        mLongList = longList;
    }
}
