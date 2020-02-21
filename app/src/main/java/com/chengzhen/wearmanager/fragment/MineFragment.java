package com.chengzhen.wearmanager;

import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.chengzhen.wearmanager.activity.ChangePasswordActivity;
import com.chengzhen.wearmanager.activity.LoginActivity;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
import com.chengzhen.wearmanager.manager.ActivityManager;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.ruffian.library.widget.RImageView;

import net.grandcentrix.tray.AppPreferences;


import butterknife.BindView;

public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_name_top)
    TextView mTvNameTop;
    @BindView(R.id.iv_head)
    RImageView mIvHead;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_unit_name)
    TextView mTvUnitName;
    @BindView(R.id.tv_location)
    TextView mTvLocation;

    @BindView(R.id.rl_change_password)
    RelativeLayout mRlChangePassword;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;
    @BindView(R.id.rl_logout)
    RelativeLayout mRlLogout;
    private String TAG = "--TAG--";
    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected void initView() {

        setCenterTitle("我    的");
    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(mContext);
        String json = mAppPreferences.getString(Constant.JSON_USER, "");
        if(!TextUtils.isEmpty(json)) {

            LoginResponse.DataBean.UserInfoBean userInfo = GsonUtils.fromJson(json, LoginResponse.DataBean.UserInfoBean.class);
            if(userInfo != null) {

                mTvNameTop.setText(userInfo.getName());
                mTvName.setText(userInfo.getName());
                mTvUnitName.setText(userInfo.getReal_name());
                mTvLocation.setText(userInfo.getAddress());

                GlideApp.with(mContext).load(userInfo.getAvatar()).error(R.drawable.ic_default_head).into(mIvHead);
            }

        }
        mRlChangePassword.setOnClickListener(v -> {

            startActivity(ChangePasswordActivity.class);
        });

        mRlSetting.setOnClickListener(v -> {

            startActivity(SettingActivity.class);
        });

        mRlLogout.setOnClickListener(v -> logout());
    }

    private void logout() {

        String userId = mAppPreferences.getString(Constant.USER_ID, "");

        mAppPreferences.remove(Constant.Token);
        mAppPreferences.put(Constant.LOGIN_SIGN,false);
        //删除别名
        TagAliasOperatorHelper.onTagAliasAction(mContext,4,userId);

        ActivityManager.getInstance().finishAllActivitys();
        startActivity(LoginActivity.class);
    }

}
