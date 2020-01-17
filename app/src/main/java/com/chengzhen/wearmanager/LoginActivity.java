package com.chengzhen.wearmanager;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
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
    @BindView(R.id.iv_password_show)
    ImageView mIvPasswordShow;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
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

        mIvPasswordShow.setOnClickListener(v -> controlPasswordShow());

        mBtnLogin.setOnClickListener(v -> startLogin());
    }

    private void startLogin() {

        boolean judge = judge();
        if(judge) {

            login();
            KeyboardUtils.hideSoftInput(this);
         }
    }

    private void login() {

        showProDialogHint("登录中。。。");
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiUserInfo/login?name=lisi&password=123456
        String usr = "http://192.168.18.30:8080/lpro_lgb/service/api/ApiUserInfo/login";
        RxHttp.get(usr)
                .add("username",accountStr)
                .add("passsword",passwordStr)
                .asObject(LoginResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(loginResponse -> {

                    int error = loginResponse.error;
                    if(error == 0) {
                        ToastUtils.showShort(loginResponse.errorcode);
                        hideProDialogHint();
                    } else {
                        LoginResponse.Data data = loginResponse.data;
                        mAppPreferences.put(Constant.ACCOUNT,data.Account);
                        mAppPreferences.put(Constant.LOGIN_SIGN,true);
                        mAppPreferences.put(Constant.Token,data.token);
                        startActivity(MainActivity.class);

                        TagAliasOperatorHelper.onTagAliasAction(mContext,3,data.Account);
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

        boolean isSelectedSet = mIvPasswordShow.isSelected();
        if (isSelectedSet) {

            mIvPasswordShow.setSelected(false);
            mEtPasswordSet.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {

            mIvPasswordShow.setSelected(true);
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
