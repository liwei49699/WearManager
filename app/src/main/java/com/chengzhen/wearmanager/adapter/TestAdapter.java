package com.chengzhen.wearmanager.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.bean.PeopleListResponse;

public class TestAdapter extends BaseQuickAdapter<PeopleListResponse.DataBean, BaseViewHolder> {

    public TestAdapter() {
        super(R.layout.item_test);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PeopleListResponse.DataBean item) {

        helper.setText(R.id.tv_test,item.getDeviceName());
    }
}
