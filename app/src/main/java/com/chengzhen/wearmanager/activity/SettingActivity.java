package com.chengzhen.wearmanager.activity;

import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.AlarmSetBean;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_power_alarm)
    ImageView mIvPowerAlarm;
    @BindView(R.id.iv_location_alarm)
    ImageView mIvLocationAlarm;
    @BindView(R.id.iv_call_alarm)
    ImageView mIvCallAlarm;
    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
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
        setCenterTitle("设    置");
    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(mContext);
//
//        boolean alarmPower = mAppPreferences.getBoolean(Constant.ALARM_POWER, true);
//        boolean alarmLocation = mAppPreferences.getBoolean(Constant.ALARM_LOCATION, true);
//        boolean alarmCall = mAppPreferences.getBoolean(Constant.ALARM_CALL, true);

        setReceiveImg(mIvPowerAlarm,false);
        setReceiveImg(mIvLocationAlarm,false);
        setReceiveImg(mIvCallAlarm,false);

        getAlarmStatus();
    }

    private void getAlarmStatus() {

        showProDialogHint();
        String token = mAppPreferences.getString(Constant.Token,"");
        //http://61.155.106.23:8080/lpro-lgb/service/api/ApiDeviceInfo/delDevice
        String url = Constant.URL + "/ApiUserInfo/alarmSwitchInfo";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .asObject(AlarmSetBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(alarmSetBean -> {

                    boolean success = CodeUtils.judgeSuccessOther(alarmSetBean, mContext);
                    if(success) {
                        setAlarmStatusInfo(alarmSetBean.getData());
                    } else {
                        ToastUtils.showShort(alarmSetBean.getMsg());
                    }
                    hideProDialogHint();

                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    hideProDialogHint();
                });
        hideProDialogHint();
    }

    private void setAlarmStatusInfo(AlarmSetBean.DataBean data) {

        if(data != null) {

            int dlbj = data.getDlbj();
            int dwbj = data.getDwbj();
            int hjbj = data.getHjbj();

            setReceiveImg(mIvPowerAlarm,dlbj == 1);
            setReceiveImg(mIvLocationAlarm,dwbj == 1);
            setReceiveImg(mIvCallAlarm,hjbj == 1);
        }
    }

    private void setReceiveImg(ImageView ivAlarm, boolean alarm) {

        if(alarm) {
            ivAlarm.setBackgroundResource(R.drawable.ic_iv_message_receive);
            ivAlarm.setTag(true);

        } else  {
            ivAlarm.setBackgroundResource(R.drawable.ic_iv_message_refuse);
            ivAlarm.setTag(false);
        }
    }

    @OnClick({R.id.iv_power_alarm,R.id.iv_location_alarm,R.id.iv_call_alarm})
    public void click(View view){

        boolean powerStatus = (boolean) mIvPowerAlarm.getTag();
        boolean locationStatus = (boolean) mIvLocationAlarm.getTag();
        boolean callStatus = (boolean) mIvCallAlarm.getTag();
        switch (view.getId()) {
            case R.id.iv_power_alarm :
                powerStatus = !powerStatus;
                break;
            case R.id.iv_location_alarm :
                locationStatus = !locationStatus;
                break;
            case R.id.iv_call_alarm :
                callStatus = !callStatus;

                break;
        }
        setReceive(powerStatus,locationStatus,callStatus);
    }

    private void setReceive(boolean powerStatus,boolean locationStatus,boolean callStatus){

        showProDialogHint();
        String token = mAppPreferences.getString(Constant.Token,"");
        //http://61.155.106.23:8080/lpro-lgb/service/api/ApiDeviceInfo/delDevice
        //dlbj  电量报警 0关闭 1开启
        //dwbj  定位报警 0关闭 1开启
        //hjbj  呼叫报警 0关闭 1开启
        String url = Constant.URL + "/ApiUserInfo/alarmSwitch";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("dlbj",powerStatus ? 1 : 0)
                .add("dwbj",locationStatus ? 1 : 0)
                .add("hjbj",callStatus ? 1 : 0)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    boolean success = CodeUtils.judgeSuccessNormal(baseResponse, mContext);
                    if(success) {
                        setReceiveImg(mIvPowerAlarm,powerStatus);
                        setReceiveImg(mIvLocationAlarm,locationStatus);
                        setReceiveImg(mIvCallAlarm,callStatus);                    }
                    hideProDialogHint();

                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    hideProDialogHint();
                });
        hideProDialogHint();
    }

    private void setSingleAlarmStatus(ImageView ivMessage, int i) {
        if(i == 0) {
            ivMessage.setBackgroundResource(R.drawable.ic_iv_message_receive);
            ivMessage.setTag(1);
        } else  {
            ivMessage.setBackgroundResource(R.drawable.ic_iv_message_refuse);
            ivMessage.setTag(0);
        }
    }


}
