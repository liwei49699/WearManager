package com.chengzhen.wearmanager.activity;

import android.Manifest;
import android.os.Handler;

import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.LoginActivity;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
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
    private Disposable mSubscribe;
    private RxPermissions mRxPermissions;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
//        ImmersionBar.with(this)
//                .transparentBar()
//                .fitsSystemWindows(true)
//                .statusBarDarkFont(true)
//                .init();
    }

    @Override
    protected void getData() {

        mRxPermissions = new RxPermissions(this);

        mHandler = new Handler();
        mHandler.postDelayed(this::checkPermissions,1500);
    }

    private void checkPermissions() {

        mSubscribe = mRxPermissions.request(needPermissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

                AppPreferences appPreferences = new AppPreferences(mContext);
                boolean login = appPreferences.getBoolean(Constant.LOGIN_SIGN, false);
                if(login) {
                    startActivity(MainActivity.class);
//                    startActivity(SignsDetailsActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if(mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }
}
