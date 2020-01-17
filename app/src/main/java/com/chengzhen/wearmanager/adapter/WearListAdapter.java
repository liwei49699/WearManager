package com.chengzhen.wearmanager.adapter;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;
import com.ruffian.library.widget.RTextView;


public class WearListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public WearListAdapter() {
        super(R.layout.item_wear_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        int layoutPosition = helper.getLayoutPosition();

        RTextView tvStatus = helper.getView(R.id.tv_status);
        if(layoutPosition % 2 == 0) {

            tvStatus.getHelper().setBorderColorNormal(Color.parseColor("#4282f4"));
            tvStatus.setText("围栏内");
            tvStatus.setTextColor(Color.parseColor("#4282f4"));

        } else {

            tvStatus.getHelper().setBorderColorNormal(Color.parseColor("#f86734"));
            tvStatus.setText("围栏外");
            tvStatus.setTextColor(Color.parseColor("#f86734"));
        }
    }
}
