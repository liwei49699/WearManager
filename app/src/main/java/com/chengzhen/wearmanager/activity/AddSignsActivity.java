package com.chengzhen.wearmanager.activity;


import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.PeopleListResponse;
import com.chengzhen.wearmanager.event.AddDeviceEvent;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class AddSignsActivity extends BaseActivity {

    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.et_device_number)
    EditText mEtDeviceNumber;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    private String mDeviceNumber;
//    private String mFactoryNumber;
    private String mUserName;
//    private String mDeviceType;
    private String mPhoneNumber;
    private AppPreferences mAppPreferences;
//    private String mDeviceName;
    private int mPeopleInfoId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_signs;
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

        setCenterTitle("添加体征仪器");

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
        String url = Constant.URL + "/ApiDeviceInfo/addTzDevice";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",mDeviceNumber)
                .add("node_user_id",mPeopleInfoId)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    hideProDialogHint();

                    boolean success = CodeUtils.judgeSuccessOther(baseResponse, mContext);
                    if(success) {

                        ToastUtils.showShort("设备添加成功");
                        AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
                        addDeviceEvent.success = true;
                        addDeviceEvent.type = 3;
                        EventBus.getDefault().post(addDeviceEvent);
                        finish();

                    } else {
                        ToastUtils.showShort(baseResponse.getMsg());
                    }
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
                    hideProDialogHint();
                });
    }

    private boolean judgeEmpty() {

        obtainInputData();

        if (TextUtils.isEmpty(mDeviceNumber)) {

            ToastUtils.showShort("设备编号不能为空");
            return false;
        }

        return true;
    }


    private void obtainInputData() {

        if (mEtDeviceNumber.getText() != null) {
            mDeviceNumber = mEtDeviceNumber.getText().toString();
        }

        if(mEtUserName.getText() != null) {
            mUserName = mEtUserName.getText().toString();
        }

        if(mEtPhoneNumber.getText() != null) {
            mPhoneNumber = mEtPhoneNumber.getText().toString();
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
