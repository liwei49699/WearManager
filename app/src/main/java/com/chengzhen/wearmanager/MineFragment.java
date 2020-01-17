package com.chengzhen.wearmanager;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;

import net.grandcentrix.tray.AppPreferences;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;

import static com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper.ACTION_SET;
import static com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper.sequence;

public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_unit_name)
    TextView mTvUnitName;
    @BindView(R.id.tv_version_code)
    TextView mTvVersionCode;
    @BindView(R.id.rl_change_password)
    RelativeLayout mRlChangePassword;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;
    private String pushtoken;
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

        setCenterTitle("我的");
    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(mContext);
        String appVersionName = AppUtils.getAppVersionName();
//        mTvVersionCode.setText(appVersionName);

//        mAppPreferences.getString()
//        mTvUnitName.setText("南京承臻医疗");

        mRlChangePassword.setOnClickListener(v -> {

            startActivity(ChangePasswordActivity.class);
        });

        mRlSetting.setOnClickListener(v -> {

            startActivity(SettingActivity.class);
        });

        mTvLogout.setOnClickListener(v -> logout());
    }

    private void logout() {

//        ToastUtils.showShort("退出登录");
//        getToken();


        //删除别名
        TagAliasOperatorHelper.onTagAliasAction(mContext,4,"1111");
    }

    private void showLog(final String log) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                View tvView = findViewById(R.id.tv_log);
//                if (tvView instanceof TextView) {
//                    ((TextView) tvView).setText(log);
                    Toast.makeText(mContext, pushtoken, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext,log,Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
