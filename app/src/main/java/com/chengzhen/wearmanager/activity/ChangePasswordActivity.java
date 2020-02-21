package com.chengzhen.wearmanager;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ruffian.library.widget.RTextView;

import butterknife.BindView;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.et_confirm_password)
    EditText mEtConfirmPassword;
    @BindView(R.id.tv_confirm)
    RTextView mBtnConfirm;
    private String mOldPasswordStr;
    private String mNewPasswordStr;
    private String mConfirmPasswordStr;

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

        setCenterTitle("修改密码");
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmChange();
            }
        });
    }

    private void confirmChange() {

        boolean judge = judge();
        if(judge) {

            changePassword();
            KeyboardUtils.hideSoftInput(this);
        }
    }

    private void changePassword() {

        finish();
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
        return true;
    }

    @Override
    protected void getData() {

    }

}
