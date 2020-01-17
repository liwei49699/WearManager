package com.chengzhen.wearmanager;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import net.grandcentrix.tray.AppPreferences;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    private Handler mHandler;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {

        mHandler = new Handler();
        mHandler.postDelayed(this::checkPermissions,1500);
    }

    private void checkPermissions() {

        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions.request(needPermissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

                AppPreferences appPreferences = new AppPreferences(mContext);
                boolean login = appPreferences.getBoolean(Constant.LOGIN_SIGN, false);
                if(login) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(MainActivity.class);
                }
            }
        });
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler != null) {

            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
