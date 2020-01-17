package com.chengzhen.wearmanager;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import net.grandcentrix.tray.AppPreferences;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.mi_bottom_nav)
    MagicIndicator mMiBottomNav;
    @BindView(R.id.vp_main)
    ScrollViewPager mVpMain;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private String[] mTitles = {"地图", "终端", "我的"};
    private int[] mSelectIcons = {R.drawable.ic_home_selected
            ,R.drawable.ic_wechat_selected,R.drawable.ic_mine_selected};
    private int[] mNormalIcons = {R.drawable.ic_home_normal
            ,R.drawable.ic_wechat_normal,R.drawable.ic_mine_normal};

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

        checkPermissions();
        initViewpager();
        initIndicator();
    }

    private void checkPermissions() {

        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions.request(needPermissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
            }
        });
    }

    private void initViewpager() {

        MainBottomPagerAdapter mainBottomPagerAdapter = new MainBottomPagerAdapter(getSupportFragmentManager());
        MapFragment mapFragment = new MapFragment();
        WearListFragment wearListFragment = new WearListFragment();
        MineFragment mineFragment = new MineFragment();
        mainBottomPagerAdapter.setFragmentList(mapFragment,wearListFragment,mineFragment);
        mVpMain.setOffscreenPageLimit(3);
        mVpMain.setAdapter(mainBottomPagerAdapter);
    }

    private void initIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                View customLayout = LayoutInflater.from(context).inflate(R.layout.item_indicator_bottom, null);
                ImageView titleImg = customLayout.findViewById(R.id.title_img);
                TextView titleText = customLayout.findViewById(R.id.title_text);
                titleText.setText(mTitles[index]);
                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        titleImg.setImageResource(mSelectIcons[index]);

                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.color_333333));
                        titleImg.setImageResource(mNormalIcons[index]);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        titleImg.setScaleX(1.3f + (1.0f - 1.3f) * leavePercent);
                        titleImg.setScaleY(1.3f + (1.0f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        titleImg.setScaleX(1.0f + (1.3f - 1.0f) * enterPercent);
                        titleImg.setScaleY(1.0f + (1.3f - 1.0f) * enterPercent);
                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mVpMain.setCurrentItem(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mMiBottomNav.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMiBottomNav, mVpMain);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void getData() {

    }
}
