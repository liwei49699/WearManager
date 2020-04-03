package com.chengzhen.wearmanager.util;

public class NumUtils {

    public static double stringToDouble(String str){

        double value = 0;
        try {
            value = Double.parseDouble(str);

        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return value;
    }

    public static float stringToFloat(String str){

        float value = 0;
        try {
            value = Float.parseFloat(str);

        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return value;
    }

    public static int stringToInteger(String str){

        int value = 0;
        try {
            value = Integer.parseInt(str);

        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return value;
    }
}
