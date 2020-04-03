package com.chengzhen.wearmanager.activity;

import android.Manifest;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.adapter.MainBottomPagerAdapter;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.fragment.AlarmListFragment;
import com.chengzhen.wearmanager.fragment.MapFragment;
import com.chengzhen.wearmanager.fragment.MineFragment;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.fragment.SignsFragment;
import com.chengzhen.wearmanager.manager.ActivityManager;
import com.chengzhen.wearmanager.view.ScrollViewPager;
import com.chengzhen.wearmanager.fragment.WearListFragment;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.event.DeviceListEvent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.AutoSizeCompat;
import rxhttp.wrapper.param.RxHttp;

public class MainActivity extends BaseActivity {

    @BindView(R.id.mi_bottom_nav)
    MagicIndicator mMiBottomNav;
    @BindView(R.id.vp_main)
    ScrollViewPager mVpMain;
    @BindView(R.id.rl_bottom_center)
    RelativeLayout mRlBottomCenter;
    @BindView(R.id.title_img)
    ImageView mTitleImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private String[] mTitles = {"地图", "终端","报警", "体征","我的"};
    private int[] mSelectIcons = {R.drawable.ic_map_select,R.drawable.ic_device_select
            ,R.drawable.ic_alarm_select,R.drawable.ic_signs_select,R.drawable.ic_mine_select};
    private int[] mNormalIcons = {R.drawable.ic_map_normal,R.drawable.ic_device_normal
            ,R.drawable.ic_alarm_normal,R.drawable.ic_signs_normal,R.drawable.ic_mine_normal};
    private AppPreferences mAppPreferences;
    private Disposable mDisposable;

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

        setTimerTask();
    }

    private void setTimerTask() {

        mDisposable = Observable.interval(0, 1, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> obtainDeviceList(false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public void obtainDeviceList(boolean refresh) {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiDeviceInfo/devicePageList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("paged",1)
                .add("pageSize",100)
                .asObject(DevicePageListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(deviceListResponse -> {

                    boolean judgeContinue = CodeUtils.judgeContinue(deviceListResponse, mContext);
                    if(judgeContinue) {

                        DeviceListEvent deviceListEvent = new DeviceListEvent();
                        int code = deviceListResponse.getCode();
                        deviceListEvent.success = code == 0;
                        deviceListEvent.refresh = refresh;
                        DevicePageListResponse.DataBeanX data = deviceListResponse.getData();
                        List<DevicePageListResponse.DataBeanX.DataBean> dataList = null;
                        if(data != null) {
                            List<DevicePageListResponse.DataBeanX.DataBean> dataBeanList = data.getData();
                            if(dataBeanList != null) {
                                dataList = dataBeanList;
                            } else {
                                dataList = new ArrayList<>();
                            }
                        } else {
                            dataList = new ArrayList<>();
                        }
                        deviceListEvent.data = dataList;
                        EventBus.getDefault().post(deviceListEvent);
                    }
                }, throwable -> {

                    DeviceListEvent deviceListEvent = new DeviceListEvent();
                    deviceListEvent.success = false;
                    EventBus.getDefault().post(deviceListEvent);
                    ToastUtils.showShort("访问服务器异常");
                });
    }

    private void checkPermissions() {

        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions.request(needPermissions).subscribe(aBoolean -> {
        });
    }

    private void initViewpager() {

        MainBottomPagerAdapter mainBottomPagerAdapter = new MainBottomPagerAdapter(getSupportFragmentManager());
        MapFragment mapFragment = new MapFragment();
        WearListFragment wearListFragment = new WearListFragment();
        AlarmListFragment alarmListFragment = new AlarmListFragment();
        SignsFragment signsFragment = new SignsFragment();
        MineFragment mineFragment = new MineFragment();
        mainBottomPagerAdapter.setFragmentList(mapFragment,wearListFragment,alarmListFragment,signsFragment,mineFragment);
        mVpMain.setOffscreenPageLimit(4);
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

                if(index == 2) {
                    titleImg.setVisibility(View.INVISIBLE);
                }

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        titleImg.setImageResource(mSelectIcons[index]);

                        if(index == 2) {
                            mTitleText.setTextColor(getResources().getColor(R.color.colorPrimary));
                            mTitleImg.setImageResource(mSelectIcons[index]);
                        }

                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.color_B1B1B1));
                        titleImg.setImageResource(mNormalIcons[index]);
                        if(index == 2) {
                            mTitleText.setTextColor(getResources().getColor(R.color.color_B1B1B1));
                            mTitleImg.setImageResource(mNormalIcons[index]);
                        }
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

        mRlBottomCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mVpMain.setCurrentItem(2);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exit();
                return true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showShort("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            // 结束Activity从堆栈中移除
            ActivityManager.getInstance().exitApp();
        }
    }

    @Override
    protected void getData() {

    }
}
