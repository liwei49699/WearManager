package com.chengzhen.wearmanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.chengzhen.wearmanager.activity.SetWorkPhoneActivity;
import com.chengzhen.wearmanager.activity.SettingActivity;
import com.chengzhen.wearmanager.activity.ChangePasswordActivity;
import com.chengzhen.wearmanager.activity.LoginActivity;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.AlarmSetBean;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.bean.WorkPhoneBean;
import com.chengzhen.wearmanager.event.WorkPhoneEvent;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
import com.chengzhen.wearmanager.manager.ActivityManager;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.ruffian.library.widget.RImageView;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.param.RxHttp;

public class MineFragment extends BaseFragment {

    @BindView(R.id.refresh_mine)
    SwipeRefreshLayout mRefreshMine;
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
    @BindView(R.id.rl_set_work_phone)
    RelativeLayout mRlSetWorkPhone;

    @BindView(R.id.rl_change_password)
    RelativeLayout mRlChangePassword;
    @BindView(R.id.rl_setting)
    RelativeLayout mRlSetting;
    @BindView(R.id.rl_logout)
    RelativeLayout mRlLogout;
    @BindView(R.id.tv_work_phone)
    TextView mTvWorkPhone;
    private String TAG = "--TAG--";
    private AppPreferences mAppPreferences;
    @BindView(R.id.iv_logo)
    RImageView mIvLogo;

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

                GlideApp.with(mContext).load(userInfo.getAvatar()).error(R.drawable.ic_mine_photo_circle).into(mIvLogo);
            }

        }
        mRlChangePassword.setOnClickListener(v -> startActivity(ChangePasswordActivity.class));

        mRlSetting.setOnClickListener(v -> startActivity(SettingActivity.class));

        mRlLogout.setOnClickListener(v -> showDialog());

        mRlSetWorkPhone.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString(Constant.WORK_PHONE,mTvWorkPhone.getText().toString());
            startActivity(SetWorkPhoneActivity.class,bundle);
        });

        getWorkPhone();
        mRefreshMine.setOnRefreshListener(this::getWorkPhone);
    }

    private void getWorkPhone() {

        String token = mAppPreferences.getString(Constant.Token,"");
        //http://127.0.0.1:8080/lpro_lgb/service/api/ApiDeviceInfo/sosZbInfo  不需要参数，只需要access-token
        //返回 {
        //    "msg": "获取成功",
        //    "code": 0,
        //    "data": "18751996112"
        //}
        String url = Constant.URL + "/ApiDeviceInfo/sosZbInfo";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .asObject(WorkPhoneBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(workPhoneBean -> {

                    boolean success = CodeUtils.judgeSuccessOther(workPhoneBean, mContext);
                    if (success) {
                        String phone = workPhoneBean.getData();
                        if (!TextUtils.isEmpty(phone)) {
                            mTvWorkPhone.setText(phone);
                        }
                    }
                    if(mRefreshMine.isRefreshing()) {
                        mRefreshMine.setRefreshing(false);
                    }

                }, throwable -> {
                    if(mRefreshMine.isRefreshing()) {
                        mRefreshMine.setRefreshing(false);
                    }
                });
    }

    private void showDialog(){

//        MessageDialog messageDialog = MessageDialog.build((AppCompatActivity) mContext)
//                .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
//
//                .setTheme(DialogSettings.THEME.LIGHT)
//                .setTitle("提示消息")
//                .setMessage("确定要退出当前账户吗？")
//                .setOkButton("确定", new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//
//                        logout();
//                        return false;
//                    }
//                })
//                .setCancelButton("取消");
//
//        if(mContext instanceof MainActivity) {
//            mContext.getResources();
//        }
//        messageDialog.show();

        new AlertDialog.Builder(mContext, R.style.BDAlertDialog)
                .setTitle("提示消息")
                .setMessage("确定要退出当前账户吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void logout() {

        String userId = mAppPreferences.getString(Constant.USER_ID, "");
        String userName = mAppPreferences.getString(Constant.ACCOUNT, "");

        mAppPreferences.remove(Constant.Token);
        mAppPreferences.put(Constant.LOGIN_SIGN,false);
        //删除别名
        TagAliasOperatorHelper.onTagAliasAction(mContext,4,userName);

        ActivityManager.getInstance().finishAllActivitys();
        startActivity(LoginActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWorkPhone(WorkPhoneEvent workPhoneEvent){

        if(workPhoneEvent.success) {
            mTvWorkPhone.setText(workPhoneEvent.workPhone);
        }
    }


}
