package com.chengzhen.wearmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.ResetPwdResponse;
import com.chengzhen.wearmanager.bean.WorkPhoneBean;
import com.chengzhen.wearmanager.event.WorkPhoneEvent;
import com.chengzhen.wearmanager.manager.ActivityManager;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.param.RxHttp;

public class SetWorkPhoneActivity extends BaseActivity {

    @BindView(R.id.et_set_work_phone)
    EditText mEtSetWorkPhone;
    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_set_work_phone;
    }

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();

        setCenterTitle("值班电话");
        mAppPreferences = new AppPreferences(mContext);

        String workPhone = getIntent().getStringExtra(Constant.WORK_PHONE);
        if(!TextUtils.isEmpty(workPhone)) {
            mEtSetWorkPhone.setText(workPhone);
            mEtSetWorkPhone.setSelection(workPhone.length());
        }
    }

    @Override
    protected void getData() {

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                judgePhone();
            }
        });

//        getCurrentPhone();
    }

    private void judgePhone() {

        String phone = mEtSetWorkPhone.getText().toString();
        if(TextUtils.isEmpty(phone)) {

            ToastUtils.showShort("请输入值班电话");
        } else {
            if(RegexUtils.isMobileExact(phone)) {
                setWorkPhone(phone);
                KeyboardUtils.hideSoftInput(this);
            } else {
                ToastUtils.showShort("值班电话格式不正确");
            }
        }
    }

    private void setWorkPhone(String phone) {

        String token = mAppPreferences.getString(Constant.Token, "");

        showProDialogHint();
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiDeviceInfo/sosSetBatchZb?phone=432123   值班sos号码设置
        String url = Constant.URL + "/ApiDeviceInfo/sosSetBatchZb";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("phone",phone)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(response -> {

                    boolean success = CodeUtils.judgeSuccessNormal(response, mContext);
                    if(success) {

                        WorkPhoneEvent workPhoneEvent = new WorkPhoneEvent();
                        workPhoneEvent.success = true;
                        workPhoneEvent.workPhone = phone;
                        EventBus.getDefault().post(workPhoneEvent);
                        finish();
                    }
                    hideProDialogHint();
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
                    hideProDialogHint();
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                KeyboardUtils.hideSoftInput(this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationOnScreen(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom);
        }
        return false;
    }
}
