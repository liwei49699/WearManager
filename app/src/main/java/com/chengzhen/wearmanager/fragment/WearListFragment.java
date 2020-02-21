package com.chengzhen.wearmanager;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.chengzhen.wearmanager.adapter.WearListAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.DeviceListEvent;
import com.chengzhen.wearmanager.bean.DeviceListResponse;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.view.RecycleViewDivider;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class WearListFragment extends BaseFragment {

    @BindView(R.id.rv_wear)
    RecyclerView mRvWear;
    @BindView(R.id.refresh_wear)
    SwipeRefreshLayout mRefreshWear;
    private WearListAdapter mWearListAdapter;
    private AppPreferences mAppPreferences;

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
        setAddListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRefreshWear.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.obtainDeviceList();
            }
        });

        mAppPreferences = new AppPreferences(mContext);
    }

    @Override
    protected void getData() {

        mWearListAdapter = new WearListAdapter();

        mRvWear.setLayoutManager(new LinearLayoutManager(mContext));

        RecycleViewDivider itemDecoration = new RecycleViewDivider.Builder(mContext)
                .setOrientation(RecycleViewDivider.VERTICAL)
                .setStyle(RecycleViewDivider.Style.START)
                .setColorRes(R.color.color_F1F1F1)
                .setSize(COMPLEX_UNIT_DIP,10)
                .build();
        mRvWear.addItemDecoration(itemDecoration);
        mRvWear.setAdapter(mWearListAdapter);

        obtainDeviceList();
    }

    public void obtainDeviceList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = "http://61.155.106.23:8080/lpro-lgb/service/api/ApiDeviceInfo/deviceList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .asObject(DeviceListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(deviceListResponse -> {

                    boolean judgeContinue = CodeUtils.judgeContinue(deviceListResponse, mContext);
                    if(judgeContinue) {

                        int code = deviceListResponse.getCode();
                        if(code == 0) {
                            List<DeviceListResponse.DataBean> data = deviceListResponse.getData();
                            mWearListAdapter.setNewData(data);
                        } else {

                        }
                    }
                }, throwable -> ToastUtils.showShort("访问服务器异常"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMap(DeviceListEvent deviceListEvent){

        boolean success = deviceListEvent.success;
        if(success) {
            List<DeviceListResponse.DataBean> data = deviceListEvent.data;
            mWearListAdapter.setNewData(data);
        } else {

        }
        if(mRefreshWear.isRefreshing()) {
            mRefreshWear.setRefreshing(false);
        }
    }
}
