package com.chengzhen.wearmanager.adapter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.bean.BloodPressureListResponse;
import com.chengzhen.wearmanager.util.SignsValueUtils;

public class BloodGlucoseAdapter extends BaseQuickAdapter<BloodPressureListResponse.DataBeanX.DataBean, BaseViewHolder> {

    public BloodGlucoseAdapter() {
        super(R.layout.item_blood_glucose);
    }
    public boolean mListEnd = false;

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BloodPressureListResponse.DataBeanX.DataBean item) {

        helper.setText(R.id.tv_measure_time, SignsValueUtils.judgeTime(item.getAtime()))
                .setText(R.id.tv_meal_before_show,SignsValueUtils.judgeEmpty(item.getSdata()) + "mmol/L")
                .setText(R.id.tv_meal_after_show,SignsValueUtils.judgeEmpty(item.getSdata1()) + "mmol/L");

        if(getData().size() != helper.getLayoutPosition() + 1 - getHeaderLayoutCount()) {
            helper.setVisible(R.id.v_line,true);
            helper.setVisible(R.id.v_line_end,false);
        } else {
            if(mListEnd) {
                helper.setVisible(R.id.v_line,false);
                helper.setVisible(R.id.v_line_end,true);
            } else {
                helper.setVisible(R.id.v_line,true);
                helper.setVisible(R.id.v_line_end,false);
            }
        }    }
}
