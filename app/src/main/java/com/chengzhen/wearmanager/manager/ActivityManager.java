package com.chengzhen.wearmanager.manager;

import android.app.Activity;
import android.os.Process;
import android.util.Log;

import com.chengzhen.wearmanager.base.BaseActivity;

import java.util.Stack;


public class ActivityManager {

    private Stack<Activity> activityStack = new Stack<>();
    private Activity mCurrentActivity;

    private ActivityManager() {
    }

    /**
     * 静态内部类的单例模式（最优）
     */
    private static class ActivityManagerHolder{

        private static ActivityManager INSTANCE = new ActivityManager();
    }

    public static ActivityManager getInstance(){

        return ActivityManagerHolder.INSTANCE;
    }


    /**
     * 将Activity加入Stack
     * @param activity
     */
    public void addActivity(Activity activity){

        if(!activityStack.contains(activity)) {

//            activityStack.push(activity);
            activityStack.add(activity);
        }
    }

    public void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }

    public Activity getCurrentActivity(){

        return mCurrentActivity;
    }

    /**
     * 结束Activity并移除Stack
     * @param activity
     */
    public void removeActivity(Activity activity){

        if(activityStack.contains(activity)) {

            activityStack.remove(activity);

            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭所有的Activity
     */
    public void finishAllActivitys(){

        for (Activity Activity : activityStack) {
            if(!Activity.isFinishing()){
                Activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用
     */
    public void exitApp(){

        for (Activity activity : activityStack) {
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activityStack.clear();
        //杀死进程    android.os.Process;
        Process.killProcess(Process.myPid());
        //其他数字非正常退出
        System.exit(0);
    }
}
