package com.chengzhen.wearmanager.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.adapter.AlarmPagerAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
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

public class AlarmListFragment extends BaseFragment {


    @BindView(R.id.mi_alarm)
    MagicIndicator mIndicatorAlarm;
    @BindView(R.id.vp_alarm)
    ViewPager mVpAlarm;
    String[] titles = {"主动报警","其他报警"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alarm_list;
    }

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected void initView() {

        setCenterTitle("报警列表");
        initViewPager();
        initIndicator();
    }

    private void initViewPager() {
        AlarmPagerAdapter alarmPagerAdapter = new AlarmPagerAdapter(getChildFragmentManager());
        alarmPagerAdapter.setFragmentList(new HandleAlarmFragment(),new OtherAlarmFragment());
        mVpAlarm.setAdapter(alarmPagerAdapter);
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

                RLinearLayout customLayout = (RLinearLayout) LayoutInflater.from(context).inflate(R.layout.item_indicator_top, null);
                TextView titleText = customLayout.findViewById(R.id.title_text);
                titleText.setText(titles[index]);
                commonPagerTitleView.setContentView(customLayout);
                RBaseHelper layoutHelper = customLayout.getHelper();

                int radius = ConvertUtils.dp2px(3);
                if(index == 0) {
                    layoutHelper.setCornerRadius(radius,0,0,radius);
                } else if(index == 1) {
                    layoutHelper.setCornerRadius(0,radius,radius,0);
                }

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.white));
//                        titleText.setTypeface(Typeface.DEFAULT_BOLD);
//                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
//                        customLayout.setBackgroundColor(Color.parseColor(""));
                        layoutHelper.setBackgroundColorNormal(Color.parseColor("#4BBF93"));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(Color.parseColor("#4BBF93"));
                        titleText.setTypeface(Typeface.DEFAULT);
//                        titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                        layoutHelper.setBackgroundColorNormal(Color.parseColor("#FFFFFF"));
//                        customLayout.setBackgroundColor(Color.parseColor(""));

                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

//                        titleText.setTextScaleX();
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                    }
                });

                commonPagerTitleView.setOnClickListener(v -> mVpAlarm.setCurrentItem(index));

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mIndicatorAlarm.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mIndicatorAlarm, mVpAlarm);
    }

    @Override
    protected void getData() {



    }



}
