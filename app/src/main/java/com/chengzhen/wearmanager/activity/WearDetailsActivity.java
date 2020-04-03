package com.chengzhen.wearmanager.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.bean.SosDetailsChangeBean;
import com.chengzhen.wearmanager.bean.WearDetailsBean;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.util.DateUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.ruffian.library.widget.RImageView;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class WearDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_photo)
    RImageView mIvPhoto;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_power)
    TextView mTvPower;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_wear_phone)
    TextView mTvWearPhone;
    @BindView(R.id.et_sos_number1)
    EditText mEtSosNumber1;
    @BindView(R.id.et_sos_number2)
    EditText mEtSosNumber2;
    @BindView(R.id.et_sos_number3)
    EditText mEtSosNumber3;
    @BindView(R.id.tv_address_sign)
    TextView mTvAddressSign;
    private AppPreferences mAppPreferences;
    private String mDeviceId;
    private String mDeviceNo;
    private String mPhone;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_wear_details;
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
        setCenterTitle("终端详情");
    }

    @Override
    protected void getData() {

        setCallClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mPhone)) {
                    ToastUtils.showShort("获取手机出错");
                } else {
                    PhoneUtils.dial(mPhone);
                }
            }
        });
        mAppPreferences = new AppPreferences(this);
        DevicePageListResponse.DataBeanX.DataBean dataBean = (DevicePageListResponse.DataBeanX.DataBean) getIntent().getSerializableExtra(Constant.WEAR_DETAILS);
        mDeviceId = dataBean.getDevice_id();
        mDeviceNo = dataBean.getDevice_no();
        getWearDetails();
        getWearSos();
    }

    private void getWearSos() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiDeviceInfo/sosInfo";
        RxHttp.postForm(url)
                .addHeader("access-token", token)
                .add("device_id", mDeviceId)
                .add("device_no", mDeviceNo)
                .asObject(SosDetailsChangeBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(sosDetailsBean -> {

                    boolean success = CodeUtils.judgeSuccessOther(sosDetailsBean, mContext);
                    if (success) {
                        setSosInfo(sosDetailsBean);
                    } else {
//                        ToastUtils.showShort(sosDetailsBean.getMsg());
                    }
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
//                    hideProDialogHint();
                });
    }

    private void setSosInfo(SosDetailsChangeBean sosDetailsBean) {

        SosDetailsChangeBean.DataBean dataBean = sosDetailsBean.getData();
        if(dataBean != null ) {
            mEtSosNumber1.setText(TextUtils.isEmpty(dataBean.getPhone()) ? "" : dataBean.getPhone());
            mEtSosNumber2.setText(TextUtils.isEmpty(dataBean.getPhone1()) ? "" : dataBean.getPhone1());
            mEtSosNumber3.setText(TextUtils.isEmpty(dataBean.getPhone2()) ? "" : dataBean.getPhone2());
        }
    }

    private void getWearDetails() {

        String token = mAppPreferences.getString(Constant.Token, "");
//        showProDialogHint();
        String url = Constant.URL + "/ApiDeviceInfo/deviceInfo";
        RxHttp.postForm(url)
                .addHeader("access-token", token)
                .add("device_id", mDeviceId)
                .add("device_no", mDeviceNo)
                .asObject(WearDetailsBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(wearDetailsBean -> {

                    boolean success = CodeUtils.judgeSuccessOther(wearDetailsBean, mContext);
                    if(success) {
                        setDetailsInfo(wearDetailsBean);
                    } else {
                        ToastUtils.showShort(wearDetailsBean.getMsg());
                    }
//                    hideProDialogHint();

                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
//                    hideProDialogHint();
                });
    }

    private void setDetailsInfo(WearDetailsBean wearDetailsBean) {

        WearDetailsBean.DataBean data = wearDetailsBean.getData();
        GlideApp.with(mContext).load(data.getImgUrl()).error(R.drawable.ic_default_head).into(mIvPhoto);
        mTvName.setText(data.getDeviceName());

        String powerLevel = data.getPowerLevel();
        if (TextUtils.isEmpty(powerLevel)) {
            powerLevel = "电量：未知";
        } else {
            powerLevel = "电量：" + powerLevel + "%";
        }
        mTvPower.setText(powerLevel);

        long lastActive = data.getLastActive();
        String activeShow;
        if (lastActive == 0L) {
            activeShow = "时间：未知";
        } else {
            activeShow = "时间：" + DateUtils.longToDate(lastActive);
        }
        mTvTime.setText(activeShow);

        String address = data.getAddress();
        if(TextUtils.isEmpty(address)) {
            mTvAddress.setText("未知");
        } else {
            mTvAddress.setText(data.getAddress());
        }
        mTvAddressSign.setText("地址：");

        String mobilePhone = data.getMobilePhone();
        if(TextUtils.isEmpty(mobilePhone)) {
            mTvWearPhone.setText("手表号码：" + "未知");
        } else {
            mTvWearPhone.setText("手表号码：" + mobilePhone);
            mPhone = mobilePhone;
        }
    }
}
