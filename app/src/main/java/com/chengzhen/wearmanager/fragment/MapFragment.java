package com.chengzhen.wearmanager.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.chengzhen.wearmanager.activity.WearDetailsActivity;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.LoginResponse;
import com.chengzhen.wearmanager.event.DeviceListEvent;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.bean.MapSignBean;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.util.NumUtils;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static com.chengzhen.wearmanager.Constants.BEIJING;
import static com.chengzhen.wearmanager.Constants.CHENGDU;
import static com.chengzhen.wearmanager.Constants.SHANGHAI;
import static com.chengzhen.wearmanager.Constants.XIAN;
import static com.chengzhen.wearmanager.Constants.ZHENGZHOU;

public class MapFragment extends BaseFragment {

    @BindView(R.id.fl_alarm)
    FrameLayout mFlAlarm;

    private AMap aMap;
    private SupportMapFragment aMapFragment;

    private static final CameraPosition LUJIAZUI = new CameraPosition.Builder()
            .target(SHANGHAI).zoom(18).bearing(0).tilt(30).build();
    private boolean mLoadSuccess;
    private AppPreferences mAppPreferences;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected void initView() {

//        setCenterTitle("地    图");
        setCenterTitle("");
        initMap();
        mAppPreferences = new AppPreferences(mContext);
    }

    @Override
    protected void setCenterTitle(String title) {

        mAppPreferences = new AppPreferences(mContext);
        String json = mAppPreferences.getString(Constant.JSON_USER, "");
        if(!TextUtils.isEmpty(json)) {

            LoginResponse.DataBean.UserInfoBean userInfo = GsonUtils.fromJson(json, LoginResponse.DataBean.UserInfoBean.class);
            if(userInfo != null) {

                String real_name = userInfo.getReal_name();
                if(TextUtils.isEmpty(real_name)) {
                    mTvFragmentTitle.setText("未知");
                } else {
                    mTvFragmentTitle.setText(real_name);
                    mTvFragmentTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    mTvFragmentTitle.setFocusable(true);
                    mTvFragmentTitle.setFocusableInTouchMode(true);
                    mTvFragmentTitle.requestFocus();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initMapObject();
    }

    @OnClick(R.id.rl_map_refresh)
    public void click(View view){

        if(mContext instanceof MainActivity) {

            showProDialogHint();
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.obtainDeviceList(true);
        }
    }

    /**
     * 初始化AMap对象
     */
    private void initMapObject() {
        if (aMap == null) {
            aMap = aMapFragment.getMap();// amap对象初始化成功
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setScaleControlsEnabled(true);
//            uiSettings.setCompassEnabled((true));
            uiSettings.setRotateGesturesEnabled(false);


            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {

                    mLoadSuccess = true;
                    showLocationPoint();
                    obtainDeviceList();
                }
            });
//            aMap.setOnMapLoadedListener(() -> operateMap());
            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    showDetails(marker.getPosition());
                    return true;
                }
            });
        }
    }

    public void obtainDeviceList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiDeviceInfo/devicePageList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("paged",1)
                .add("pageSize",100)
                .asObject(DevicePageListResponse.class)
//                .asString()
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//
//                        Log.d("--TAG--", ": " + s);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
                .subscribe(deviceListResponse -> {

                    boolean judgeContinue = CodeUtils.judgeContinue(deviceListResponse, mContext);
                    if(judgeContinue) {
                        int code = deviceListResponse.getCode();
                        if(code == 0) {

                            List<DevicePageListResponse.DataBeanX.DataBean> dataList;
                            DevicePageListResponse.DataBeanX data = deviceListResponse.getData();
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
                            operateMap(dataList,true);
                        }
                    }
                }, throwable -> ToastUtils.showShort("访问服务器异常"));
    }

    private List<DevicePageListResponse.DataBeanX.DataBean> mDataBeanList;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMap(DeviceListEvent deviceListEvent){

//        Log.d("--TAG--", "load success:" + mLoadSuccess);
        List<DevicePageListResponse.DataBeanX.DataBean> data = deviceListEvent.data;
        hideProDialogHint();
        if(mLoadSuccess) {
            operateMap(data,deviceListEvent.refresh);
        }
    }



    private void showLocationPoint() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);

        //改为7
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) ;//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.ic_map_location));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private List<MapSignBean> getTestMapData(){

        List<MapSignBean> signBeans = new ArrayList<>();

        MapSignBean mapSignBean = new MapSignBean();
        mapSignBean.deviceName = "上海";
        mapSignBean.powerLevel = "50";
        mapSignBean.latLng = SHANGHAI;
        mapSignBean.alarmFlag = 0;

        MapSignBean mapSignBean1 = new MapSignBean();
        mapSignBean1.deviceName = "北京";
        mapSignBean1.powerLevel = "30";
        mapSignBean1.latLng = BEIJING;
        mapSignBean1.alarmFlag = 1;

        MapSignBean mapSignBean2 = new MapSignBean();
        mapSignBean2.deviceName = "西安";
        mapSignBean2.powerLevel = "40";
        mapSignBean2.latLng = XIAN;
        mapSignBean2.alarmFlag = 2;

        MapSignBean mapSignBean3 = new MapSignBean();
        mapSignBean3.deviceName = "郑州";
        mapSignBean3.powerLevel = "70";
        mapSignBean3.latLng = ZHENGZHOU;
        mapSignBean3.alarmFlag = 3;

        MapSignBean mapSignBean4 = new MapSignBean();
        mapSignBean4.deviceName = "成都";
        mapSignBean4.powerLevel = "100";
        mapSignBean4.latLng = CHENGDU;
        mapSignBean4.alarmFlag = 4;

        signBeans.add(mapSignBean);
        signBeans.add(mapSignBean1);
        signBeans.add(mapSignBean2);
        signBeans.add(mapSignBean3);
        signBeans.add(mapSignBean4);

        return signBeans;
    }

    private void operateMap(List<DevicePageListResponse.DataBeanX.DataBean> data, boolean refresh) {

        mDataBeanList = data;
        aMap.clear(true);
        List<MapSignBean> signBeans = new ArrayList<>();
        for (DevicePageListResponse.DataBeanX.DataBean datum : data) {

            String deviceName = datum.getDeviceName();
            String powerLevel = datum.getPowerLevel();
            String latitude = datum.getLatitude();
            String longitude = datum.getLongitude();
            if(!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                LatLng latLng = new LatLng(NumUtils.stringToDouble(latitude), NumUtils.stringToDouble(longitude));



                MapSignBean mapSignBean = new MapSignBean();
                mapSignBean.deviceName = deviceName;
                mapSignBean.powerLevel = powerLevel;
                mapSignBean.latLng = latLng;
//                mapSignBean.latLng = convert(latLng);

//                float powerStatus = Float.parseFloat(powerLevel);

                //是否报警 0 报警
                int alarmFlag = datum.getAlertflag();
                //在线状态 16在线 17离线
                int iot_node_status = datum.getIot_node_status();
                //围栏状态 1 围栏外
                int posAlert = datum.getPosAlert();
                //电量状态 30低电量
                float powerStatus = NumUtils.stringToFloat(powerLevel);

                if(alarmFlag != 0) {
                    mapSignBean.alarmFlag = 0; //报警
                } else if(iot_node_status == 17) {
                    mapSignBean.alarmFlag = 1; //离线
                } else if(posAlert == 1) {
                    mapSignBean.alarmFlag = 2; //围栏外
                } else if(powerStatus < 30) {
                    mapSignBean.alarmFlag = 3; //低电量
                } else {
                    mapSignBean.alarmFlag = 4; //正常
                }

                signBeans.add(mapSignBean);
            }
        }

        addMarkersToMap(signBeans);

        if(refresh) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (MapSignBean signBean : signBeans) {

                builder.include(signBean.latLng);
            }

            LatLngBounds latLngBounds = builder.build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,100));
        }
    }

    /**
     * 根据类型 转换 坐标
     */
    private LatLng convert(LatLng sourceLatLng) {

        CoordinateConverter.CoordType coordType = CoordinateConverter.CoordType.valueOf("GPS");
        CoordinateConverter converter  = new CoordinateConverter(mContext);
        // CoordType.GPS 待转换坐标类型
        converter.from(coordType);
        // sourceLatLng待转换坐标点
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

//    private void addMySign(double var1, double var2) {
//
//        //定义Maker坐标点
//
//        LatLng point = new LatLng(var1, var2);
//
//        //构建Marker图标
//
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.ic_my_location);
//
//        //构建MarkerOption，用于在地图上添加Marker
//
//        TextView textView = new TextView(getContext());
//        textView.setText("自定义的覆盖物" + i++);
//        textView.setTextSize(16);
//        //将View转换为BitmapDescriptor
//        BitmapDescriptor descriptor =  BitmapDescriptorFactory.fromView(textView);
//
//
//        OverlayOptions option = new MarkerOptions()
//                .position(point)
//                .icon(bitmap);
//
//        //在地图上添加Marker，并显示
//        baiduMap.addOverlay(option);
//    }

    @Override
    protected void getData() {

    }

    private void addMarkersToMap(List<MapSignBean> signBeans) {

        for (int i = 0; i < signBeans.size(); i++) {

            View markerView = LayoutInflater.from(mContext).inflate(R.layout.layout_map_market, null);
            MapSignBean mapSignBean = signBeans.get(i);
            ImageView ivStatus = markerView.findViewById(R.id.iv_status);
            TextView tvName = markerView.findViewById(R.id.tv_name);
            TextView tvPower = markerView.findViewById(R.id.tv_power);

            setStatusMap(ivStatus,mapSignBean.alarmFlag);
            tvName.setText(mapSignBean.deviceName);
            tvPower.setText("电量：" + mapSignBean.powerLevel + "%");
//            rlMarker.getHelper().setBackgroundColorNormal(Color.parseColor("#f86734"));
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(markerView);
            MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor)
                    .position(signBeans.get(i).latLng)
                    .anchor(0.5F,0.54F)
                    .draggable(false);
            aMap.addMarker(markerOption);
        }
    }

    private void setStatusMap(ImageView ivStatus, int alarmFlag) {

        switch (alarmFlag) {
            case 0 :
                ivStatus.setImageResource(R.drawable.ic_map_alarm);
                break;
            case 1 :
                ivStatus.setImageResource(R.drawable.ic_map_offline);
                break;
            case 2 :
                ivStatus.setImageResource(R.drawable.ic_map_outside);

                break;
            case 3 :
                ivStatus.setImageResource(R.drawable.ic_map_status_power_low);

                break;
            case 4 :
                ivStatus.setImageResource(R.drawable.ic_map_status_normal);

                break;
        }
    }

    /**
     * 初始化AMap对象
     */
    private void initMap(){

        AMapOptions aOptions = new AMapOptions();
        aOptions.zoomGesturesEnabled(true);// 禁止通过手势缩放地图
        aOptions.scrollGesturesEnabled(true);// 禁止通过手势移动地图
        aOptions.zoomControlsEnabled(false);

        aOptions.camera(LUJIAZUI);
        if (aMapFragment == null) {
            aMapFragment = SupportMapFragment.newInstance(aOptions);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_alarm, aMapFragment, "map_fragment");
            fragmentTransaction.commit();
        }
    }

    private void showDetails(LatLng latLng) {

        for (DevicePageListResponse.DataBeanX.DataBean dataBean : mDataBeanList) {

            String latitudeMarker = latLng.latitude+ "";
            String longitudeMarker = latLng.longitude + "";

            String latitude = dataBean.getLatitude();
            String longitude = dataBean.getLongitude();
            if(TextUtils.equals(latitudeMarker,latitude) && TextUtils.equals(longitudeMarker,longitude)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.WEAR_DETAILS,dataBean);
                startActivity(WearDetailsActivity.class,bundle);
                break;
            }
        }

//        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_details, null);
//        PopupWindow popWnd = new PopupWindow(mContext);
//        popWnd.setContentView(contentView);
//
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////
////                String id = marketMapDetal.getId();
////                Intent intent = new Intent(mContext, BuildNameDetailsActivity.class);
////                intent.putExtra(Keys.KEY_BUILD_ID,id);
////
////                mContext.startActivity(intent);
//            }
//        });
//
//        popWnd.setBackgroundDrawable(null);
//        //点击空白区域是否可以关闭popupWindow
//        popWnd.setOutsideTouchable(true);
//        popWnd.setBackgroundDrawable(new BitmapDrawable());
////        popWnd.setFocusable(true);
//        // 设置此参数失去焦点，这样点击其他按钮才能响应相应的事件
//        popWnd.setFocusable(false);
//
////        popWnd.showAsDropDown();
//        popWnd.showAtLocation(mFlAlarm, Gravity.BOTTOM,
//                0, ConvertUtils.dp2px(130));



    }
}
