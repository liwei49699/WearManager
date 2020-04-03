package com.chengzhen.wearmanager.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.chengzhen.wearmanager.R;

public class CustomLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.footer_view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
