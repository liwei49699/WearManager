package com.chengzhen.wearmanager.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;

import java.util.List;

public class TemperatureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TemperatureAdapter() {
        super(R.layout.item_temperature);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        helper.setVisible(R.id.v_line,getData().size() != helper.getLayoutPosition() + 1);
    }
}
