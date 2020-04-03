package com.chengzhen.wearmanager.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.bean.AlarmListBean;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.manager.GlideApp;
import com.chengzhen.wearmanager.util.DateUtils;


public class AlarmListAdapter extends BaseQuickAdapter<AlarmListBean.DataBeanX.DataBean, BaseViewHolder> {

    public AlarmListAdapter() {
        super(R.layout.item_alarm_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AlarmListBean.DataBeanX.DataBean item) {

        ImageView ivPhoto = helper.getView(R.id.iv_photo);
        GlideApp.with(mContext).load(item.getImgUrl()).error(R.drawable.ic_default_head).into(ivPhoto);

//        type 报警类型  25电量 26定位 27报警 90心率 106离线
        ImageView ivAlarmtype = helper.getView(R.id.iv_alarm_type);
        int type = item.getType();
        boolean showHandle = false;
        String typeShow = "未知";
        switch (type){
            case 25:
                typeShow = "电量过低";
                ivAlarmtype.setImageResource(R.drawable.ic_alarm_power);
                break;
            case 26:
                typeShow = "超出范围";
                ivAlarmtype.setImageResource(R.drawable.ic_alarm_outside);

                break;
            case 27:
                typeShow = "主动报警";
                showHandle = true;
                ivAlarmtype.setImageResource(R.drawable.ic_alarm_call);

                break;
            case 90:
                typeShow = "心率异常";
                ivAlarmtype.setImageResource(R.drawable.ic_alarm_heart_rate);
                break;
            case 106:
                typeShow = "离线报警";
                ivAlarmtype.setImageResource(R.drawable.ic_alarm_offline);
                break;
        }
        long atime = item.getAtime();
        String timeShow;
        if(atime == 0L) {
            timeShow = "未知";
        } else {
            timeShow = DateUtils.longToDate(atime);
        }

        ImageView ivAlarmStatus = helper.getView(R.id.iv_alarm_status);
        int iot_alarm_process_status = item.getIot_alarm_process_status();
        String statusShow = "未知";
        if(iot_alarm_process_status == 46) {
            statusShow = "未处理";
            if(showHandle) {
                ivAlarmStatus.setVisibility(View.INVISIBLE);
            } else {
                ivAlarmStatus.setImageResource(R.drawable.ic_alarm_handle_no);
                ivAlarmStatus.setVisibility(View.VISIBLE);
            }
        } else if(iot_alarm_process_status == 47) {
            statusShow = "已处理";
            ivAlarmStatus.setVisibility(View.VISIBLE);
            ivAlarmStatus.setImageResource(R.drawable.ic_alarm_handle);
            showHandle = false;
        } else if(iot_alarm_process_status == 48) {
            statusShow = "已标注";
            showHandle = false;
            ivAlarmStatus.setVisibility(View.VISIBLE);
            ivAlarmStatus.setImageResource(R.drawable.ic_alarm_sign);
        }
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.getDeviceName()) ? "未知" : item.getDeviceName())
                .setText(R.id.tv_type,"类型：" + typeShow)
                .setText(R.id.tv_time,"时间：" + timeShow)
                .setText(R.id.tv_content,item.getDescription());
//                .setText(R.id.tv_status,"状态：" + statusShow);
        helper.setGone(R.id.tv_handle,showHandle);
        helper.addOnClickListener(R.id.tv_handle);
    }

    public void updateItem(int position) {

        AlarmListBean.DataBeanX.DataBean dataBean = mData.get(position);
        dataBean.setIot_alarm_process_status(47);
        notifyItemChanged(position);
    }
}
