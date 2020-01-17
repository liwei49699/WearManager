package com.chengzhen.wearmanager;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.CrashUtils;
import com.chengzhen.wearmanager.adapter.WearListAdapter;
import com.chengzhen.wearmanager.view.RecycleViewDivider;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class WearListFragment extends BaseFragment {

    @BindView(R.id.rv_wear)
    RecyclerView mRvWear;
    @BindView(R.id.refresh_wear)
    SwipeRefreshLayout mRefreshWear;
    private WearListAdapter mWearListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wear_list;
    }

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected void initView() {

        setCenterTitle("终端列表");
        setRightMsgClick("添加终端", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CrashReport.testJavaCrash();
            }
        });

        mRefreshWear.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRefreshWear.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshWear.setRefreshing(false);
                    }
                },1000);
            }
        });


    }

    @Override
    protected void getData() {


        mWearListAdapter = new WearListAdapter();

        mRvWear.setLayoutManager(new LinearLayoutManager(mContext));

        RecycleViewDivider itemDecoration = new RecycleViewDivider.Builder(mContext)
                .setOrientation(RecycleViewDivider.VERTICAL)
                .setStyle(RecycleViewDivider.Style.BETWEEN)
                .setColorRes(R.color.colorBgNormal)
                .setSize(COMPLEX_UNIT_PX,10)
                .build();
        mRvWear.addItemDecoration(itemDecoration);
        mRvWear.setAdapter(mWearListAdapter);


        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            stringList.add("");
        }
        mWearListAdapter.setNewData(stringList);
    }

}
