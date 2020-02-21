package com.chengzhen.wearmanager;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.fragment.app.FragmentTransaction;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.DeviceListEvent;
import com.chengzhen.wearmanager.bean.DeviceListResponse;
import com.chengzhen.wearmanager.bean.MapSignBean;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.util.NumUtils;
import com.ruffian.library.widget.RRelativeLayout;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static com.chengzhen.wearmanager.Constants.SHANGHAI;

public class MapFragment extends BaseFragment {

    @BindView(R.id.fl_alarm)
    FrameLayout mFlAlarm;

    private AMap aMap;
    private SupportMapFragment aMapFragment;

    private static final CameraPosition LUJIAZUI = new CameraPosition.Builder()
            .target(SHANGHAI).zoom(18).bearing(0).tilt(30).build();
    private List<Marker> mMarkerList;
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

        setCenterTitle("地    图");
        initMap();
        mAppPreferences = new AppPreferences(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMapObject();
    }

    /**
     * 初始化AMap对象
     */
    private void initMapObject() {
        if (aMap == null) {
            aMap = aMapFragment.getMap();// amap对象初始化成功
//            showLocationPoint();

            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {

                    mLoadSuccess = true;
                    obtainDeviceList();
                }
            });
//            aMap.setOnMapLoadedListener(() -> operateMap());
            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    for (Marker marker1 : mMarkerList) {

                        if(marker.equals(marker1)) {
                        }
                    }

                    showDetails();

                    return true;
                }
            });
        }
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

                        int code = deviceListResponse.getCode();
                        if(code == 0) {
                            operateMap(deviceListResponse.getData());
                        }

                    }
                }, throwable -> ToastUtils.showShort("访问服务器异常"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMap(DeviceListEvent deviceListEvent){

        Log.d("--TAG--", "load success:" + mLoadSuccess);
        List<DeviceListResponse.DataBean> data = deviceListEvent.data;
        if(mLoadSuccess) {

            operateMap(data);
        }
    }



    private void showLocationPoint() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);

        //改为7
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER) ;//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。

        // 自定义定位蓝点图标
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
//                fromResource(R.drawable.gps_point));
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

    private void operateMap(List<DeviceListResponse.DataBean> data) {


        List<MapSignBean> signBeans = new ArrayList<>();
        for (DeviceListResponse.DataBean datum : data) {

            String deviceName = datum.getDeviceName();
            String powerLevel = datum.getPowerLevel();
            String latitude = datum.getLatitude();
            String longitude = datum.getLongitude();
            LatLng latLng = new LatLng(NumUtils.stringToDouble(latitude), NumUtils.stringToDouble(longitude));

            MapSignBean mapSignBean = new MapSignBean();
            mapSignBean.deviceName = deviceName;
            mapSignBean.powerLevel = powerLevel;
            mapSignBean.latLng = latLng;

            signBeans.add(mapSignBean);
        }

        addMarkersToMap(signBeans);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MapSignBean signBean : signBeans) {

            builder.include(signBean.latLng);
        }

        LatLngBounds latLngBounds = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,100));
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
            RRelativeLayout rlMarker = markerView.findViewById(R.id.rl_marker);
            rlMarker.getHelper().setBackgroundColorNormal(Color.parseColor("#f86734"));
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(markerView);

            MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor)
                    .position(signBeans.get(i).latLng)
                    .draggable(false);
            aMap.addMarker(markerOption);
        }

//        BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromView(markerView1);
//
//        List<LatLng> latLngList = new ArrayList<>();
//        List<MarkerOptions> markerOptionsList = new ArrayList<>();
//        mMarkerList = new ArrayList<>();
//        latLngList.add(SHANGHAI);
//        latLngList.add(BEIJING);
//        latLngList.add(XIAN);
//
//        for (int i = 0; i < latLngList.size(); i++) {
//
//            LatLng latLng = latLngList.get(i);
//            if(i == 1) {
//                MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor1)
//                        .position(latLng)
//                        .draggable(false);
//                markerOptionsList.add(markerOption);
//            } else {
//                MarkerOptions markerOption = new MarkerOptions().icon(bitmapDescriptor)
//                        .position(latLng)
//                        .draggable(false);
//                markerOptionsList.add(markerOption);
//            }
//        }
//
//        if(aMap != null) {
//
//            aMap.clear();
//
//            for (MarkerOptions markerOptions : markerOptionsList) {
//
//                Marker marker = aMap.addMarker(markerOptions);
//                mMarkerList.add(marker);
//            }
//        }
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

    private void showDetails() {

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_popup_details, null);
        PopupWindow popWnd = new PopupWindow(mContext);
        popWnd.setContentView(contentView);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                String id = marketMapDetal.getId();
//                Intent intent = new Intent(mContext, BuildNameDetailsActivity.class);
//                intent.putExtra(Keys.KEY_BUILD_ID,id);
//
//                mContext.startActivity(intent);
            }
        });

        popWnd.setBackgroundDrawable(null);
        //点击空白区域是否可以关闭popupWindow
        popWnd.setOutsideTouchable(true);
        popWnd.setBackgroundDrawable(new BitmapDrawable());
//        popWnd.setFocusable(true);
        // 设置此参数失去焦点，这样点击其他按钮才能响应相应的事件
        popWnd.setFocusable(false);

//        popWnd.showAsDropDown();
        popWnd.showAtLocation(mFlAlarm, Gravity.BOTTOM,
                0, ConvertUtils.dp2px(130));

    }
}
