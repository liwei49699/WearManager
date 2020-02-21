package com.chengzhen.wearmanager;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.bean.DeviceListEvent;
import com.chengzhen.wearmanager.bean.DeviceListResponse;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.grandcentrix.tray.AppPreferences;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import rxhttp.wrapper.param.RxHttp;

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
    private int[] mSelectIcons = {R.drawable.ic_map_select
            ,R.drawable.ic_device_select,R.drawable.ic_mine_select};
    private int[] mNormalIcons = {R.drawable.ic_map_normal
            ,R.drawable.ic_device_normal,R.drawable.ic_mine_normal};
    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();

        mAppPreferences = new AppPreferences(this);

        checkPermissions();
        initViewpager();
        initIndicator();
//        obtainDeviceList();
    }

    public void obtainDeviceList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = "http://61.155.106.23:8080/lpro-lgb/service/api/ApiDeviceInfo/deviceList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .asObject(DeviceListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(deviceListResponse -> {

                    boolean judgeContinue = CodeUtils.judgeContinue(deviceListResponse, mContext);
                    if(judgeContinue) {

                        DeviceListEvent deviceListEvent = new DeviceListEvent();
                        int code = deviceListResponse.getCode();
                        if(code == 0) {
                            deviceListEvent.success = true;
                        } else {
                            deviceListEvent.success = false;
                        }
                        List<DeviceListResponse.DataBean> data = deviceListResponse.getData();
                        deviceListEvent.data = data;
                        EventBus.getDefault().post(deviceListEvent);
                    }
                }, throwable -> ToastUtils.showShort("访问服务器异常"));
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
                        titleText.setTextColor(getResources().getColor(R.color.color_B1B1B1));
                        titleImg.setImageResource(mNormalIcons[index]);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//                        titleImg.setScaleX(1.3f + (1.0f - 1.3f) * leavePercent);
//                        titleImg.setScaleY(1.3f + (1.0f - 1.3f) * leavePercent);
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//                        titleImg.setScaleX(1.0f + (1.3f - 1.0f) * enterPercent);
//                        titleImg.setScaleY(1.0f + (1.3f - 1.0f) * enterPercent);
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
