package com.chengzhen.wearmanager.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;

public class BloodPressureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BloodPressureAdapter() {
        super(R.layout.item_blood_pressure);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        helper.setVisible(R.id.v_line,getData().size() != helper.getLayoutPosition() + 1);
    }
}
