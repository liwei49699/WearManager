package com.chengzhen.wearmanager.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.bean.DeviceListResponse;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.chengzhen.wearmanager.util.DateUtils;
import com.chengzhen.wearmanager.util.NumUtils;
import com.ruffian.library.widget.RTextView;


public class WearListAdapter extends BaseQuickAdapter<DevicePageListResponse.DataBeanX.DataBean, BaseViewHolder> {

    private Activity mAcntext;

    public WearListAdapter(Activity context) {
        super(R.layout.item_wear_list);
        mAcntext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DevicePageListResponse.DataBeanX.DataBean item) {

        ImageView ivPhoto = helper.getView(R.id.iv_photo);
        GlideApp.with(mAcntext).load(item.getImgUrl()).error(R.drawable.ic_default_head).into(ivPhoto);

        String powerLevel = item.getPowerLevel();
        String powerLevelShow;

        if(TextUtils.isEmpty(powerLevel)) {
            powerLevelShow = "电量：未知";
        } else {
            powerLevelShow = "电量：" + powerLevel + "%";
        }

        long lastActive = item.getLastActive();
        String activeShow;
        if(lastActive == 0L) {
            activeShow = "时间：未知";
        } else {
            activeShow = "时间：" + DateUtils.longToDate(lastActive);
        }

        String device_id = item.getDevice_id();
        String idShow;
        if(TextUtils.isEmpty(device_id)) {
            idShow = "设备编号：" + "未知";
        } else {
            idShow = "设备编号：" + device_id;
        }

        helper.setText(R.id.tv_name,item.getDeviceName())
                .setText(R.id.tv_power,powerLevelShow)
                .setText(R.id.tv_time,activeShow)
                .setText(R.id.tv_address,idShow);

//        String latitude = item.getLatitude();
//        String longitude = item.getLongitude();
//        int position = helper.getLayoutPosition();
//        LatLonPoint latLng = new LatLonPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
//
        TextView tvAddress = helper.getView(R.id.tv_address);
        String address = item.getAddress();
        if(TextUtils.isEmpty(address)) {
            tvAddress.setText("地址：" + "未知");
        } else {
            tvAddress.setText("地址：" + item.getAddress());
        }
//        getAddress(latLng,tvAddress);

        //是否报警 0 报警
        int alarmFlag = item.getAlertflag();
        //在线状态 16在线 17离线
        int iot_node_status = item.getIot_node_status();
        //围栏状态 1 围栏外
        int posAlert = item.getPosAlert();
        //电量状态 30低电量
        int powerStatus = NumUtils.stringToInteger(powerLevel);

        RTextView tvAlarm = helper.getView(R.id.tv_status_alarm);
        RTextView tvOffine = helper.getView(R.id.tv_status_offline);
        RTextView tvOutside = helper.getView(R.id.tv_status_outside);
        RTextView tvPower = helper.getView(R.id.tv_status_power);

        if(alarmFlag != 0) {
//            mapSignBean.alarmFlag = 0; //报警
            tvAlarm.setText("报警");
            tvAlarm.getHelper().setBackgroundColorNormal(Color.parseColor("#C1272D"));
            tvAlarm.setVisibility(View.VISIBLE);
        } else {
            tvAlarm.setVisibility(View.GONE);
        }

        if(iot_node_status == 16) {
//            mapSignBean.alarmFlag = 1; //离线
            tvOffine.setText("在线");
            tvOffine.getHelper().setBackgroundColorNormal(Color.parseColor("#0071BC"));

            if(powerStatus < 30) {
//                mapSignBean.alarmFlag = 3; //低电量
                tvPower.setText("电量低");
                tvPower.getHelper().setBackgroundColorNormal(Color.parseColor("#ADC18E"));
            } else {
//                mapSignBean.alarmFlag = 4; //正常
                tvPower.setText("电量高");
                tvPower.getHelper().setBackgroundColorNormal(Color.parseColor("#38B549"));
            }

            tvPower.setVisibility(View.VISIBLE);

        } else {
            tvOffine.setText("离线");
            tvOffine.getHelper().setBackgroundColorNormal(Color.parseColor("#B3B3B3"));

            tvPower.setVisibility(View.GONE);
        }

        if(posAlert == 1) {
//            mapSignBean.alarmFlag = 2; //围栏外
            tvOutside.setText("围栏外");
            tvOutside.getHelper().setBackgroundColorNormal(Color.parseColor("#F15924"));
        } else {
            tvOutside.setText("围栏里");
            tvOutside.getHelper().setBackgroundColorNormal(Color.parseColor("#F15924"));
            tvOutside.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.rrl_change,R.id.rrl_delete,R.id.rl_content);
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint, TextView tvLocation) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系

        GeocodeSearch geocodeSearch = new GeocodeSearch(mAcntext);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

                String location = "暂无";
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {

                        location = result.getRegeocodeAddress().getFormatAddress() + "附近";
                    } else {
                        location = "暂无";
                    }
                } else {
                    location = "暂无";
                }

//                result.

//                String = tvLocation.getTag();
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        geocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    public void updateItem(int position, String userName) {

        DevicePageListResponse.DataBeanX.DataBean dataBean = mData.get(position);
        dataBean.setDeviceName(userName);
        notifyItemChanged(position);
    }
}
