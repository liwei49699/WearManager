package com.chengzhen.wearmanager.activity;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;


import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_password_set)
    EditText mEtPasswordSet;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    private String accountStr;
    private String passwordStr;
    private InputMethodManager manager;
    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAppPreferences = new AppPreferences(this);

        String account = mAppPreferences.getString(Constant.ACCOUNT, "");
        if(!TextUtils.isEmpty(account)) {
            mEtAccount.setText(account);
            mEtAccount.setSelection(account.length());
        }
    }

    @Override
    protected void getData() {

//        mIvPasswordShow.setOnClickListener(v -> controlPasswordShow());

        mTvLogin.setOnClickListener(v -> startLogin());
    }

    private void startLogin() {

        boolean judge = judge();
        if(judge) {

            login();
            KeyboardUtils.hideSoftInput(this);
         }
    }

    private void login() {

        showProDialogHint();
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiUserInfo/login?name=lisi&password=123456
        String usr = Constant.URL + "/ApiUserInfo/login";

        RxHttp.postForm(usr)
                .add("name",accountStr)
                .add("password",passwordStr)
                .asObject(LoginResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(loginResponse -> {

                    int error = loginResponse.getCode();
                    if(error == 0) {
                        LoginResponse.DataBean data = loginResponse.getData();
                        LoginResponse.DataBean.UserInfoBean userInfo = data.getUserInfo();
                        mAppPreferences.put(Constant.ACCOUNT,userInfo.getName());
                        mAppPreferences.put(Constant.LOGIN_SIGN,true);
                        mAppPreferences.put(Constant.Token,data.getToken());
                        mAppPreferences.put(Constant.USER_ID,userInfo.getId() + "");
                        startActivity(MainActivity.class);
                        finish();

                        String userJson = GsonUtils.toJson(userInfo);
                        mAppPreferences.put(Constant.JSON_USER,userJson);

                        TagAliasOperatorHelper.onTagAliasAction(mContext,3,userInfo.getName() + "");

                    } else {
                        ToastUtils.showShort(loginResponse.getMsg());
                        hideProDialogHint();
                    }
                    hideProDialogHint();
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
                    hideProDialogHint();
                });
    }

    private boolean judge() {

        obtainInputData();

        if (TextUtils.isEmpty(accountStr)) {

            ToastUtils.showShort("账号不能为空");
            return false;
        }

        if (TextUtils.isEmpty(passwordStr)) {

            ToastUtils.showShort("密码不能为空");
            return false;
        }
        return true;
    }

    private void controlPasswordShow() {

        boolean isSelectedSet = false;
        if (isSelectedSet) {

            mEtPasswordSet.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {

            mEtPasswordSet.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        obtainInputData();
        if (!TextUtils.isEmpty(passwordStr)) {

            mEtPasswordSet.setSelection(passwordStr.length());
        }
    }

    private void obtainInputData() {

        if (mEtAccount.getText() != null) {
            accountStr = mEtAccount.getText().toString();
        }

        if(mEtPasswordSet.getText() != null) {
            passwordStr = mEtPasswordSet.getText().toString();
        }
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
