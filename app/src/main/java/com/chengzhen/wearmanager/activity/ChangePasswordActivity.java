package com.chengzhen.wearmanager.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.bean.ResetPwdResponse;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
import com.chengzhen.wearmanager.manager.ActivityManager;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.ruffian.library.widget.RTextView;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.param.RxHttp;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText mEtConfirmPassword;
    @BindView(R.id.tv_confirm)
    TextView mBtnConfirm;
    private String mOldPasswordStr;
    private String mNewPasswordStr;
    private String mConfirmPasswordStr;
    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_password;
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

        setCenterTitle("修改密码");
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmChange();
            }
        });

        mAppPreferences = new AppPreferences(this);
    }

    private void confirmChange() {

        boolean judge = judge();
        if(judge) {

            changePassword();
            KeyboardUtils.hideSoftInput(this);
        }
    }

    private void changePassword() {

        String token = mAppPreferences.getString(Constant.Token, "");

        showProDialogHint();
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiUserInfo/resetPwd?oldPwd=123456&newPwd=1234567
        String url = Constant.URL + "/ApiUserInfo/resetPwd";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("oldPwd",mOldPasswordStr)
                .add("newPwd",mNewPasswordStr)
                .asObject(ResetPwdResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(resetPwdResponse -> {

                    boolean success = CodeUtils.judgeSuccessNormal(resetPwdResponse, mContext);
                    if(success) {
                        finish();
                    }
                    hideProDialogHint();
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
                    hideProDialogHint();
                });
    }

    private void obtainInputData() {

        if (mEtOldPassword.getText() != null) {

            mOldPasswordStr = mEtOldPassword.getText().toString();
        }

        if(mEtNewPassword.getText() != null) {

            mNewPasswordStr = mEtNewPassword.getText().toString();
        }

        if(mEtConfirmPassword.getText() != null) {

            mConfirmPasswordStr = mEtConfirmPassword.getText().toString();
        }
    }

    private boolean judge() {

        obtainInputData();

        if (TextUtils.isEmpty(mOldPasswordStr)) {
            ToastUtils.showShort("就密码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(mNewPasswordStr)) {
            ToastUtils.showShort("新密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(mConfirmPasswordStr)) {
            ToastUtils.showShort("新密码不能为空");
            return false;
        }
        if(!TextUtils.equals(mConfirmPasswordStr,mNewPasswordStr)) {
            ToastUtils.showShort("输入的两次密码不一致");
            return false;
        }
        return true;
    }

    @Override
    protected void getData() {

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
