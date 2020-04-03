package com.chengzhen.wearmanager.util;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
import com.chengzhen.wearmanager.manager.ActivityManager;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.activity.LoginActivity;
import com.chengzhen.wearmanager.bean.BaseResponse;

import net.grandcentrix.tray.AppPreferences;

public class CodeUtils {

    public static boolean judgeContinue(BaseResponse baseResponse, Activity activity){

        int code = baseResponse.getCode();
        String msg = baseResponse.getMsg();
        if(code == 0) {
            //
            return true;
        } else if(code == -1) {

            Activity currentActivity = ActivityManager.getInstance().getCurrentActivity();
            if(currentActivity instanceof LoginActivity) {

            } else {
                AppPreferences appPreferences = new AppPreferences(activity);
                appPreferences.remove(Constant.LOGIN_SIGN);
                String userName = appPreferences.getString(Constant.ACCOUNT, "");
                //删除别名
                TagAliasOperatorHelper.onTagAliasAction(activity,4,userName);
                ToastUtils.showShort(msg);
                ActivityManager.getInstance().finishAllActivitys();
                //token 失效 跳登录
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            }
            return false;
        } else {
            //失败 toast提示
            ToastUtils.showShort(msg);
            return true;
        }
    }

    public static boolean judgeSuccessNormal(BaseResponse baseResponse, Activity activity){

        int code = baseResponse.getCode();
        String msg = baseResponse.getMsg();
        ToastUtils.showShort(msg);

        if(code == 0) {

            return true;
        } else if(code == -1) {

            Activity currentActivity = ActivityManager.getInstance().getCurrentActivity();
            if(currentActivity instanceof LoginActivity) {

            } else {

                AppPreferences appPreferences = new AppPreferences(activity);
                appPreferences.remove(Constant.LOGIN_SIGN);
                String userName = appPreferences.getString(Constant.ACCOUNT, "");
                //删除别名
                TagAliasOperatorHelper.onTagAliasAction(activity,4,userName);
                ActivityManager.getInstance().finishAllActivitys();
                //token 失效 跳登录
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            }

            return false;
        } else {
            return false;
        }

    }

    public static boolean judgeSuccessOther(BaseResponse baseResponse, Activity activity){

        int code = baseResponse.getCode();

        if(code == 0) {

            return true;
        } else if(code == -1) {

            Activity currentActivity = ActivityManager.getInstance().getCurrentActivity();
            if(currentActivity instanceof LoginActivity) {

            } else {
                AppPreferences appPreferences = new AppPreferences(activity);
                appPreferences.remove(Constant.LOGIN_SIGN);
                String userName = appPreferences.getString(Constant.ACCOUNT, "");
                //删除别名
                TagAliasOperatorHelper.onTagAliasAction(activity,4,userName);
                ActivityManager.getInstance().finishAllActivitys();
                //token 失效 跳登录
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
            }

            return false;
        } else {
            return false;
        }

    }
}
