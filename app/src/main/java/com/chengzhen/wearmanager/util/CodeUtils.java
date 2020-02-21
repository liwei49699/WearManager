package com.chengzhen.wearmanager;

import android.app.Activity;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.bean.BaseResponse;

public class CodeUtils {

    public static boolean judgeContinue(BaseResponse baseResponse, Activity activity){

        int code = baseResponse.getCode();
        String msg = baseResponse.getMsg();
        if(code == 0) {
            //
            return true;
        } else if(code == -1) {
            ActivityManager.getInstance().finishAllActivitys();
            //token 失效 跳登录
            Intent intent = new Intent(activity,LoginActivity.class);
            activity.startActivity(intent);
            return false;
        } else {
            //失败 toast提示
            ToastUtils.showShort(msg);
            return false;
        }
    }
}
