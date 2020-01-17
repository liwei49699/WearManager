package com.chengzhen.wearmanager;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;


import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.OkHttpClient;
import rxhttp.wrapper.param.RxHttp;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initBugly();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        RxHttp.init(getDefaultOkHttpClient());
        //初始化屏幕适配
        initAutoSize();
    }

    private void initBugly() {

        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "d9fd69021f", true, strategy);

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private static OkHttpClient getDefaultOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true) //忽略host验证
                .build();
    }

    private void initAutoSize() {

        //三方页面在其他页面时需初始化
        AutoSizeConfig.getInstance()
                .setBaseOnWidth(false)
                //屏蔽系统字体大小
                .setExcludeFontScale(true);
    }
}
