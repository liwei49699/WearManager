package com.chengzhen.wearmanager.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.kongzue.dialog.util.DialogSettings;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;
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

        RxHttp.init(getDefaultOkHttpClient(),true);
        //初始化屏幕适配
        initAutoSize();
    }

    private void initBugly() {

        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块; false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 60 * 1000;
        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);

        Beta.upgradeDialogLayoutId = R.layout.dialog_update;


        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//        // 初始化Bugly
//        CrashReport.initCrashReport(context, "d9fd69021f", true, strategy);

        Bugly.init(getApplicationContext(), "d9fd69021f", false,strategy);
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
                .setBaseOnWidth(true)
                //屏蔽系统字体大小
                .setExcludeFontScale(true);
    }
}
