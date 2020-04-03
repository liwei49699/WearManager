package com.chengzhen.wearmanager.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.bean.SosDetailsChangeBean;
import com.chengzhen.wearmanager.bean.WearDetailsBean;
import com.chengzhen.wearmanager.event.AddDeviceEvent;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.util.DateUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.ruffian.library.widget.RTextView;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class WearUpdateActivity extends BaseActivity {

    @BindView(R.id.et_device_name)
    EditText mEtDeviceName;
    @BindView(R.id.et_device_number)
    EditText mEtDeviceNumber;
    @BindView(R.id.et_factory_number)
    EditText mEtFactoryNumber;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.et_device_type)
    EditText mEtDeviceType;
    @BindView(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @BindView(R.id.et_sos_number1)
    EditText mEtSosNumber1;
    @BindView(R.id.et_sos_number2)
    EditText mEtSosNumber2;
    @BindView(R.id.et_sos_number3)
    EditText mEtSosNumber3;
    @BindView(R.id.tv_save)
    RTextView mTvSave;
    @BindView(R.id.tv_cancel)
    RTextView mTvCancel;

    private AppPreferences mAppPreferences;
    //设备编号
    private String mDeviceId;
    //厂家编号
    private String mDeviceNo;
//    private String mDeviceName;
    private String mDeviceNumber;
    private String mFactoryNumber;
    private String mUserName;
//    private String mDeviceType;
    private String mPhoneNumber;
    private String mSosNumber1;
    private String mSosNumber2;
    private String mSosNumber3;
    private int itemPosition;
    private int mPeopleInfoId;

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_wear_update;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
        setCenterTitle("终端编辑");
    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(this);
        DevicePageListResponse.DataBeanX.DataBean dataBean = (DevicePageListResponse.DataBeanX.DataBean) getIntent().getSerializableExtra(Constant.WEAR_DETAILS);
        itemPosition = getIntent().getIntExtra(Constant.POSITION, 0);

        if(dataBean != null) {
            mDeviceId = dataBean.getDevice_id();
            mDeviceNo = dataBean.getDevice_no();
            String deviceName = dataBean.getDeviceName();
            String mobilePhone = dataBean.getMobilePhone();
            mPeopleInfoId = dataBean.getUser_id();
            if(!TextUtils.isEmpty(deviceName)) {
                mEtUserName.setText(deviceName);
            }
            if(!TextUtils.isEmpty(mobilePhone)) {
                mEtPhoneNumber.setText(mobilePhone);
            }
        }

        mEtUserName.setEnabled(false);
        mEtPhoneNumber.setEnabled(false);
        getWearDetails();
        getWearSos();

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWear();
            }
        });
    }

    private void updateWear() {

        boolean pass = judgeEmpty();
        if(pass) {
//            update();
            changeSosInfo();

            KeyboardUtils.hideSoftInput(this);
        }
    }

    private void update() {

        String token = mAppPreferences.getString(Constant.Token, "");
        showProDialogHint();
        String url = Constant.URL + "/ApiDeviceInfo/modDevice";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
//                .add("name",mDeviceName)
                .add("device_id",mDeviceNumber)
                .add("device_no",mDeviceNo)
//                .add("deviceName",mUserName)
//                .add("deviceType",mDeviceType)
//                .add("mobilePhone",mPhoneNumber)
                .add("node_user_id",mPeopleInfoId)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    boolean success = CodeUtils.judgeSuccessOther(baseResponse, mContext);
                    if(success) {
                        changeSosInfo();
                    } else {
                        ToastUtils.showShort(baseResponse.getMsg());
                        hideProDialogHint();
                    }
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
                    hideProDialogHint();
                });
    }

    private void changeSosInfo() {

        showProDialogHint();
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiDeviceInfo/sosSet?phone=432123&device_id=1703328887&device_no=3G
        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiDeviceInfo/sosSet";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("phone",mSosNumber1)
                .add("phone1",mSosNumber2)
                .add("phone2",mSosNumber3)
                .add("device_id",mDeviceNumber)
                .add("device_no",mDeviceNo)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    boolean success = CodeUtils.judgeSuccessOther(baseResponse, mContext);
                    if(success) {
                        ToastUtils.showShort("信息修改成功");
                    } else {
                        ToastUtils.showShort(baseResponse.getMsg());
                    }

                    hideProDialogHint();
//                    AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
//                    addDeviceEvent.success = true;
//                    addDeviceEvent.type = 2;
//                    addDeviceEvent.name = mUserName;
//                    addDeviceEvent.position = itemPosition;
//                    EventBus.getDefault().post(addDeviceEvent);
                    finish();
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("设备添加成功，sos设置错误");
                    hideProDialogHint();
//                    AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
//                    addDeviceEvent.success = true;
//                    addDeviceEvent.type = 2;
//                    addDeviceEvent.name = mUserName;
//                    addDeviceEvent.position = itemPosition;
//                    EventBus.getDefault().post(addDeviceEvent);
                    finish();
                });
    }

    private boolean judgeEmpty() {

        obtainInputData();

//        if (TextUtils.isEmpty(mDeviceName)) {
//
//            ToastUtils.showShort("设备名称不能为空");
//            return false;
//        }

//        if (TextUtils.isEmpty(mDeviceNumber)) {
//
//            ToastUtils.showShort("设备编号不能为空");
//            return false;
//        }

//        if (TextUtils.isEmpty(mFactoryNumber)) {
//
//            ToastUtils.showShort("厂家编号不能为空");
//            return false;
//        }


//        if (TextUtils.isEmpty(mUserName)) {
//
//            ToastUtils.showShort("使用者姓名不能为空");
//            return false;
//        }


//        if (TextUtils.isEmpty(mDeviceType)) {
//
//            ToastUtils.showShort("设备类型不能为空");
//            return false;
//        }


//        if (TextUtils.isEmpty(mPhoneNumber)) {
//
//            ToastUtils.showShort("手机号码不能为空");
//            return false;
//        }
        return true;
    }

    private void obtainInputData() {

//        if (mEtDeviceName.getText() != null) {
//            mDeviceName = mEtDeviceName.getText().toString();
//        }

        if (mEtDeviceNumber.getText() != null) {
            mDeviceNumber = mEtDeviceNumber.getText().toString();
        }

//        if(mEtFactoryNumber.getText() != null) {
//            mFactoryNumber = mEtFactoryNumber.getText().toString();
//        }

        if(mEtUserName.getText() != null) {
            mUserName = mEtUserName.getText().toString();
        }

//        if(mEtDeviceType.getText() != null) {
//            mDeviceType = mEtDeviceType.getText().toString();
//        }

        if(mEtPhoneNumber.getText() != null) {
            mPhoneNumber = mEtPhoneNumber.getText().toString();
        }

        if(mEtSosNumber1.getText() != null) {
            mSosNumber1 = mEtSosNumber1.getText().toString();
        }

        if(mEtSosNumber2.getText() != null) {
            mSosNumber2 = mEtSosNumber2.getText().toString();
        }

        if(mEtSosNumber3.getText() != null) {
            mSosNumber3 = mEtSosNumber3.getText().toString();
        }

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
//                    ToastUtils.showShort("访问服务器错误");
//                    hideProDialogHint();
                });
    }

    private void setSosInfo(SosDetailsChangeBean sosDetailsBean) {

        SosDetailsChangeBean.DataBean dataBean = sosDetailsBean.getData();
        if (dataBean != null) {
            mEtSosNumber1.setText(TextUtils.isEmpty(dataBean.getPhone()) ? "" : dataBean.getPhone());
            mEtSosNumber2.setText(TextUtils.isEmpty(dataBean.getPhone1()) ? "" : dataBean.getPhone1());
            mEtSosNumber3.setText(TextUtils.isEmpty(dataBean.getPhone2()) ? "" : dataBean.getPhone2());
        }
    }

    private void getWearDetails() {
        String token = mAppPreferences.getString(Constant.Token, "");
        showProDialogHint();
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
                    hideProDialogHint();

                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
                    hideProDialogHint();
                });
    }

    private void setDetailsInfo(WearDetailsBean wearDetailsBean) {

        WearDetailsBean.DataBean dataBean = wearDetailsBean.getData();
        mEtDeviceName.setText(TextUtils.isEmpty(dataBean.getName()) ? "" : dataBean.getName());
        mEtDeviceNumber.setText(TextUtils.isEmpty(dataBean.getDevice_id()) ? "" : dataBean.getDevice_id());
        mEtFactoryNumber.setText(TextUtils.isEmpty(dataBean.getDevice_no()) ? "" : dataBean.getDevice_no());
        mEtUserName.setText(TextUtils.isEmpty(dataBean.getDeviceName()) ? "" : dataBean.getDeviceName());
        mEtDeviceType.setText(TextUtils.isEmpty(dataBean.getDeviceType()) ? "" : dataBean.getDeviceType());
        mEtPhoneNumber.setText(TextUtils.isEmpty(dataBean.getMobilePhone()) ? "" : dataBean.getMobilePhone());
    }

}
