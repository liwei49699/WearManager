package com.chengzhen.wearmanager.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.bean.SignsPageListResponse;

import static com.chengzhen.wearmanager.util.SignsValueUtils.judgeEmpty;
import static com.chengzhen.wearmanager.util.SignsValueUtils.judgeTime;

public class SignsListAdapter extends BaseQuickAdapter<SignsPageListResponse.DataBeanX.DataBean, BaseViewHolder> {

    public SignsListAdapter() {
        super(R.layout.item_signs_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SignsPageListResponse.DataBeanX.DataBean item) {

        helper.setText(R.id.tv_name,item.getDeviceName())
                .setText(R.id.tv_meal_before_show,judgeEmpty(item.getKfxt()) + "mmol/L") //餐前数值
                .setText(R.id.tv_meal_before_time_show, judgeTime(item.getKfxt_time())) //餐前时间
                .setText(R.id.tv_meal_after_show,judgeEmpty(item.getChxt()) + "mmol/L") //餐后数值
                .setText(R.id.tv_meal_after_time_show, judgeTime(item.getChxt_time()) + "") //餐后时间

                .setText(R.id.tv_high_pressure_show,judgeEmpty(item.getGy()) + "mmHg") //高压数值
                .setText(R.id.tv_high_pressure_time_show, judgeTime(item.getGy_time())) //高压时间
                .setText(R.id.tv_low_pressure_show,judgeEmpty(item.getDy()) + "mmHg") //低压数值
                .setText(R.id.tv_low_pressure_time_show, judgeTime(item.getDy_time())) //低压时间

                .setText(R.id.tv_heart_rate_show,judgeEmpty(item.getXl()) + "bmp") //心率数值
                .setText(R.id.tv_heart_rate_time_show, judgeTime(item.getXl_time())) //心率时间

//                .setText(R.id.tv_heart_rate_show,judgeEmpty(item.getXl()) + "mmHg") //心率数值
//                .setText(R.id.tv_heart_rate_time_show,judgeTime(item.getXl_time())) //心率时间
                ;
    }


}
