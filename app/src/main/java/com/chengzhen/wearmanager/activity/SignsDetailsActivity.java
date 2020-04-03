package com.chengzhen.wearmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ConvertUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.adapter.AlarmPagerAdapter;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.fragment.BloodGlucoseDetailsFragment;
import com.chengzhen.wearmanager.fragment.BloodOxygenDetailsFragment;
import com.chengzhen.wearmanager.fragment.BloodPressureDetailsFragment;
import com.chengzhen.wearmanager.fragment.HandleAlarmFragment;
import com.chengzhen.wearmanager.fragment.HeartRateDetailsFragment;
import com.chengzhen.wearmanager.fragment.OtherAlarmFragment;
import com.chengzhen.wearmanager.fragment.TemperatureDetailsFragment;
import com.gyf.immersionbar.ImmersionBar;
import com.ruffian.library.widget.RLinearLayout;
import com.ruffian.library.widget.helper.RBaseHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import butterknife.BindView;

public class SignsDetailsActivity extends BaseActivity {


    @BindView(R.id.mi_signs)
    MagicIndicator mMiSigns;
    @BindView(R.id.vp_signs)
    ViewPager mVpSigns;

    private String[] titles = {"体温测量","血压测量","血糖测量","血氧测量","心率测量"};
    private String[] topTitles = {"体温","血压","血糖","血氧","心率"};

    private int[] mSelectIcons = {R.drawable.ic_temperature_select,R.drawable.ic_blood_pressure_select
            ,R.drawable.ic_blood_glucose_select,R.drawable.ic_blood_oxygen_select,R.drawable.ic_heart_rate_select};
    private int[] mNormalIcons = {R.drawable.ic_temperature_normal,R.drawable.ic_blood_pressure_normal
            ,R.drawable.ic_blood_glucose_normal,R.drawable.ic_blood_oxygen_normal,R.drawable.ic_heart_rate_normal};
    private String mName;
    private String mDeviceId;
    private String mDeviceNo;

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_signs_details;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();
//        setCenterTitle("体征详情");

        Intent intent = getIntent();
        mName = intent.getStringExtra(Constant.SIGNS_NAME);
        mDeviceId = intent.getStringExtra(Constant.SIGNS_ID);
        mDeviceNo = intent.getStringExtra(Constant.SIGNS_NO);
    }

    @Override
    protected void getData() {

        initViewPager();
        initIndicator();
    }

    private void initViewPager() {
        AlarmPagerAdapter alarmPagerAdapter = new AlarmPagerAdapter(getSupportFragmentManager());

        TemperatureDetailsFragment temperatureDetailsFragment = TemperatureDetailsFragment.getInstance(mDeviceId,mDeviceNo);
        BloodPressureDetailsFragment bloodPressureDetailsFragment = BloodPressureDetailsFragment.getInstance(mDeviceId,mDeviceNo);
        BloodGlucoseDetailsFragment bloodGlucoseDetailsFragment = BloodGlucoseDetailsFragment.getInstance(mDeviceId,mDeviceNo);
        BloodOxygenDetailsFragment bloodOxygenDetailsFragment = BloodOxygenDetailsFragment.getInstance(mDeviceId,mDeviceNo);
        HeartRateDetailsFragment heartRateDetailsFragment = HeartRateDetailsFragment.getInstance(mDeviceId,mDeviceNo);
        alarmPagerAdapter.setFragmentList(temperatureDetailsFragment,bloodPressureDetailsFragment,
                bloodGlucoseDetailsFragment,bloodOxygenDetailsFragment,heartRateDetailsFragment);
        mVpSigns.setOffscreenPageLimit(5);
        mVpSigns.setAdapter(alarmPagerAdapter);
    }

    private void initIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                RelativeLayout customLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_indicator_signs_top, null);
                TextView titleText = customLayout.findViewById(R.id.title_text);
                ImageView titleImg = customLayout.findViewById(R.id.title_img);
                commonPagerTitleView.setContentView(customLayout);

                titleText.setText(titles[index]);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
//                        titleText.setTextColor(getResources().getColor(R.color.white));
//                        titleText.setTypeface(Typeface.DEFAULT_BOLD);
//                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
//                        customLayout.setBackgroundColor(Color.parseColor(""));
//                        layoutHelper.setBackgroundColorNormal(Color.parseColor("#4BBF93"));
                        titleImg.setImageResource(mSelectIcons[index]);
                        setCenterTitle(mName + "的" + topTitles[index]);

                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
//                        titleText.setTextColor(Color.parseColor("#4BBF93"));
//                        titleText.setTypeface(Typeface.DEFAULT);
//                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
//                        layoutHelper.setBackgroundColorNormal(Color.parseColor("#FFFFFF"));
//                        customLayout.setBackgroundColor(Color.parseColor(""));
                        titleImg.setImageResource(mNormalIcons[index]);

                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

//                        titleText.setTextScaleX();
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    }
                });

                commonPagerTitleView.setOnClickListener(v -> mVpSigns.setCurrentItem(index));

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mMiSigns.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMiSigns, mVpSigns);
    }

}
