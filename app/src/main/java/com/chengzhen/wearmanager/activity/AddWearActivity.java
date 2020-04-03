package com.chengzhen.wearmanager.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.bean.PeopleListResponse;
import com.chengzhen.wearmanager.event.AddDeviceEvent;
import com.chengzhen.wearmanager.jpush.TagAliasOperatorHelper;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class AddWearActivity extends BaseActivity {

    @BindView(R.id.tv_add)
    TextView mTvAdd;
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
    private String mDeviceNumber;
//    private String mFactoryNumber;
    private String mUserName;
//    private String mDeviceType;
    private String mPhoneNumber;
    private AppPreferences mAppPreferences;
//    private String mDeviceName;
    private String mSosNumber1;
    private String mSosNumber2;
    private String mSosNumber3;
    private int mPeopleInfoId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_wear;
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
    }

    @Override
    protected void getData() {

        setCenterTitle("添加终端");

        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //添加
                addDevice();
            }
        });

        PeopleListResponse.DataBean peopleInfo = getIntent().getParcelableExtra(Constant.PEOPLE_INFO);
        Log.d("--TAG--", "getData: " + peopleInfo);

        if(peopleInfo != null) {
            String deviceName = peopleInfo.getDeviceName();
            String mobilePhone = peopleInfo.getMobilePhone();
            mPeopleInfoId = peopleInfo.getId();
            if(!TextUtils.isEmpty(deviceName)) {
                mEtUserName.setText(deviceName);
            }
            if(!TextUtils.isEmpty(mobilePhone)) {
                mEtPhoneNumber.setText(mobilePhone);
            }
        }

        mEtUserName.setEnabled(false);
        mEtPhoneNumber.setEnabled(false);

        mAppPreferences = new AppPreferences(this);

    }

    private void addDevice() {

        boolean pass = judgeEmpty();
        if(pass) {
            add();
            KeyboardUtils.hideSoftInput(this);
        }
    }

    private void add() {

        String token = mAppPreferences.getString(Constant.Token, "");
        showProDialogHint();
        String url = Constant.URL + "/ApiDeviceInfo/addDevice";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
//                .add("name",mDeviceName)
                .add("device_id",mDeviceNumber)
//                .add("device_no",mFactoryNumber)
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

                        if(TextUtils.isEmpty(mSosNumber1) && TextUtils.isEmpty(mSosNumber2) && TextUtils.isEmpty(mSosNumber3)) {
                            ToastUtils.showShort("设备添加成功");
                            AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
                            addDeviceEvent.success = true;
                            addDeviceEvent.type = 1;
                            EventBus.getDefault().post(addDeviceEvent);
                            finish();
                        } else {
                            setSosInfo();
                        }
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

    private void setSosInfo(){
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiDeviceInfo/sosSet?phone=432123&device_id=1703328887&device_no=3G
        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiDeviceInfo/sosSet";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("phone",mSosNumber1)
                .add("phone1",mSosNumber2)
                .add("phone2",mSosNumber3)
                .add("device_id",mDeviceNumber)
//                .add("device_no",mFactoryNumber)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    boolean success = CodeUtils.judgeSuccessOther(baseResponse, mContext);
                    if(success) {
                        //设置sos成功
                        ToastUtils.showShort("设备添加成功");
                    } else {
                        //设置sos失败
                        ToastUtils.showShort(baseResponse.getMsg());
                    }
                    hideProDialogHint();
                    AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
                    addDeviceEvent.success = true;
                    addDeviceEvent.type = 1;
                    EventBus.getDefault().post(addDeviceEvent);
                    finish();
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("设备添加成功，sos设置错误");
                    hideProDialogHint();
                    AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
                    addDeviceEvent.success = true;
                    addDeviceEvent.type = 1;
                    EventBus.getDefault().post(addDeviceEvent);
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

        if (TextUtils.isEmpty(mDeviceNumber)) {

            ToastUtils.showShort("设备编号不能为空");
            return false;
        }

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
//             mDeviceName = mEtDeviceName.getText().toString();
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
