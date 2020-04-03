package com.chengzhen.wearmanager.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.AddWearActivity;
import com.chengzhen.wearmanager.activity.PeopleListActivity;
import com.chengzhen.wearmanager.activity.SignsDetailsActivity;
import com.chengzhen.wearmanager.activity.WearDetailsActivity;
import com.chengzhen.wearmanager.activity.WearUpdateActivity;
import com.chengzhen.wearmanager.adapter.SignsListAdapter;
import com.chengzhen.wearmanager.adapter.WearListAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.bean.SignsPageListResponse;
import com.chengzhen.wearmanager.event.AddDeviceEvent;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.view.RecycleViewDivider;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class SignsFragment extends BaseFragment {

    @BindView(R.id.rv_signs)
    RecyclerView mRvSigns;
    @BindView(R.id.refresh_signs)
    SwipeRefreshLayout mRefreshSigns;
    private AppPreferences mAppPreferences;
    private int mCurrentPage = 1;
    private SignsListAdapter mSignsListAdapter;

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signs;
    }

    @Override
    protected void initView() {

        setCenterTitle("体    征");
        setAddListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.DEVICE_TYPE,1);
            startActivity(PeopleListActivity.class,bundle);
        });

        mRefreshSigns.setOnRefreshListener(() -> {

            mCurrentPage = 1;
            obtainSignsList();
        });

        mAppPreferences = new AppPreferences(mContext);
    }

    @Override
    protected void getData() {

        mSignsListAdapter = new SignsListAdapter();
        mRvSigns.setLayoutManager(new LinearLayoutManager(mContext));

        RecycleViewDivider itemDecoration = new RecycleViewDivider.Builder(mContext)
                .setOrientation(RecycleViewDivider.VERTICAL)
                .setStyle(RecycleViewDivider.Style.BOTH)
                .setColorRes(R.color.color_F1F1F1)
                .setSize(COMPLEX_UNIT_DIP,10)
                .build();
        mRvSigns.addItemDecoration(itemDecoration);
        mRvSigns.setAdapter(mSignsListAdapter);
        mSignsListAdapter.setOnLoadMoreListener(this::obtainSignsList,mRvSigns);

        mSignsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                SignsPageListResponse.DataBeanX.DataBean dataBean = mSignsListAdapter.getData().get(position);
                Bundle bundleChange = new Bundle();
                bundleChange.putSerializable(Constant.SIGNS_NAME,dataBean.getDeviceName());
                bundleChange.putString(Constant.SIGNS_ID,dataBean.getDevice_id());
                bundleChange.putString(Constant.SIGNS_NO,dataBean.getDevice_no());
                startActivity(SignsDetailsActivity.class,bundleChange);
            }
        });
        obtainSignsList();
    }

    private void obtainSignsList() {

        String token = mAppPreferences.getString(Constant.Token, "");
//        http://192.168.18.30:8080/lpro_lgb/service/api/ApiDeviceInfo/devicePageList?paged=1&pageSize=2  设备列表分页  paged当前页，pageSize每页条数
        String url = Constant.URL + "/ApiDeviceInfo/tzDevicePageList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("paged",mCurrentPage)
                .add("pageSize",5)
                .asObject(SignsPageListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(signsPageListResponse -> {

                    if(mRefreshSigns.isRefreshing()) {
                        mRefreshSigns.setRefreshing(false);
                    }
                    boolean judgeContinue = CodeUtils.judgeContinue(signsPageListResponse, mContext);
                    if(judgeContinue) {

                        int code = signsPageListResponse.getCode();
                        if(code == 0) {

                            SignsPageListResponse.DataBeanX data = signsPageListResponse.getData();
                            List<SignsPageListResponse.DataBeanX.DataBean> dataList;
                            int totalPage = 0;
                            if(data != null) {
                                List<SignsPageListResponse.DataBeanX.DataBean> dataBeanList = data.getData();
                                if(dataBeanList != null) {
                                    dataList = dataBeanList;
                                } else {
                                    dataList = new ArrayList<>();
                                }
                                totalPage = data.getTotalPage();
                            } else {
                                dataList = new ArrayList<>();
                            }

                            if(mCurrentPage == 1) {
                                //刷新
                                mSignsListAdapter.setNewData(dataList);
                                if(totalPage <= mCurrentPage ) {
                                    mSignsListAdapter.loadMoreEnd(true);
                                }
                                mCurrentPage ++;

                            } else {
                                //加载更多
                                if(dataList.size() > 0) {
                                    mSignsListAdapter.addData(dataList);
                                    if(totalPage == mCurrentPage) {
                                        mSignsListAdapter.loadMoreEnd(true);
                                    } else {
                                        mSignsListAdapter.loadMoreComplete();
                                    }
                                    mCurrentPage ++;
                                } else {
                                    mSignsListAdapter.loadMoreEnd(true);
                                }
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    if(mRefreshSigns.isRefreshing()) {
                        mRefreshSigns.setRefreshing(false);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWearList(AddDeviceEvent addDeviceEvent){

        boolean success = addDeviceEvent.success;
        if(success) {
            if(addDeviceEvent.type == 3) {
                mRefreshSigns.setRefreshing(true);
                mCurrentPage = 1;
                obtainSignsList();
            }
        }
    }
}
