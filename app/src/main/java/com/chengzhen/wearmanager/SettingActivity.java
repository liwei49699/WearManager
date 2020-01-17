package com.chengzhen.wearmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        setCenterTitle("设置");
    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(mContext);

        boolean alarmPower = mAppPreferences.getBoolean(Constant.ALARM_POWER, true);
        boolean alarmLocation = mAppPreferences.getBoolean(Constant.ALARM_LOCATION, true);
        boolean alarmCall = mAppPreferences.getBoolean(Constant.ALARM_CALL, true);

        setReceiveImg(mIvPowerAlarm,alarmPower);
        setReceiveImg(mIvLocationAlarm,alarmLocation);
        setReceiveImg(mIvCallAlarm,alarmCall);
    }

    private void setReceiveImg(ImageView ivAlarm, boolean alarm) {

        if(alarm) {
            ivAlarm.setBackgroundResource(R.drawable.ic_iv_message_receive);
        } else  {
            ivAlarm.setBackgroundResource(R.drawable.ic_iv_message_refuse);
        }
    }

    @OnClick({R.id.iv_power_alarm,R.id.iv_location_alarm,R.id.iv_call_alarm})
    public void click(View view){

        ImageView imageView = null;
        String key = "";
        switch (view.getId()) {
            case R.id.iv_power_alarm :
                imageView = mIvPowerAlarm;
                key = Constant.ALARM_POWER;
                break;
            case R.id.iv_location_alarm :
                imageView = mIvLocationAlarm;
                key = Constant.ALARM_LOCATION;
                break;
            case R.id.iv_call_alarm :
                imageView = mIvCallAlarm;
                key = Constant.ALARM_CALL;
                break;
        }
        setReceive(imageView,key);
    }

    private void setReceive(ImageView ivMessage,String key){

        boolean isPush = mAppPreferences.getBoolean(key, true);
        if(!isPush) {

            ivMessage.setBackgroundResource(R.drawable.ic_iv_message_receive);
        } else  {
            ivMessage.setBackgroundResource(R.drawable.ic_iv_message_refuse);
        }
        mAppPreferences.put(key,!isPush);
    }
}
