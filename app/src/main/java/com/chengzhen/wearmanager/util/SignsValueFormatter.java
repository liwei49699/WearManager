package com.chengzhen.wearmanager.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class SignsValueFormatter extends ValueFormatter {

    public static final int TYPE_VALUE = 1;
    public static final int TYPE_TIME = 2;
    private int mType;

    public SignsValueFormatter(int type) {
        mType = type;
    }

    @Override
    public String getFormattedValue(float value) {

//        if(34 == value) {
////            if(mType == TYPE_VALUE) {
////                return "数值";
////            } else {
////                return "时间";
////            }
//            return "数值";
//        } else if(10 == value) {
//            return "时间";
//
//        } else {
//            if(mType == TYPE_VALUE) {
//                return (int)value + "";
//            } else {
//                return (int)value + "";
//            }
//        }
        return (int)value + "";
    }
}
