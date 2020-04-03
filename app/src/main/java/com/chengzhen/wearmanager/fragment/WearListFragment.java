package com.chengzhen.wearmanager.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.AddWearActivity;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.chengzhen.wearmanager.activity.PeopleListActivity;
import com.chengzhen.wearmanager.activity.WearDetailsActivity;
import com.chengzhen.wearmanager.activity.WearUpdateActivity;
import com.chengzhen.wearmanager.adapter.WearListAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.DevicePageListResponse;
import com.chengzhen.wearmanager.event.AddDeviceEvent;
import com.chengzhen.wearmanager.event.AlarmHandleEvent;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.view.RecycleViewDivider;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
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

public class WearListFragment extends BaseFragment {

    @BindView(R.id.rv_wear)
    RecyclerView mRvWear;
    @BindView(R.id.refresh_wear)
    SwipeRefreshLayout mRefreshWear;
    private WearListAdapter mWearListAdapter;
    private AppPreferences mAppPreferences;
    private int mCurrentPage = 1;

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
        setAddListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.DEVICE_TYPE,0);
            startActivity(PeopleListActivity.class,bundle);
        });

        mRefreshWear.setOnRefreshListener(() -> {

            mCurrentPage = 1;
            obtainDeviceList();
        });

        mAppPreferences = new AppPreferences(mContext);
    }

    @Override
    protected void getData() {

        mWearListAdapter = new WearListAdapter(mContext);

        mRvWear.setLayoutManager(new LinearLayoutManager(mContext));

        RecycleViewDivider itemDecoration = new RecycleViewDivider.Builder(mContext)
                .setOrientation(RecycleViewDivider.VERTICAL)
                .setStyle(RecycleViewDivider.Style.BOTH)
                .setColorRes(R.color.color_F1F1F1)
                .setSize(COMPLEX_UNIT_DIP,10)
                .build();
        mRvWear.addItemDecoration(itemDecoration);
        mRvWear.setAdapter(mWearListAdapter);
        mWearListAdapter.setOnLoadMoreListener(this::obtainDeviceList,mRvWear);

        mWearListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            DevicePageListResponse.DataBeanX.DataBean dataBean = mWearListAdapter.getData().get(position);


            int id = view.getId();
            switch (id) {
                case R.id.rrl_change :
                    Bundle bundleChange = new Bundle();
                    bundleChange.putSerializable(Constant.WEAR_DETAILS,dataBean);
                    bundleChange.putInt(Constant.POSITION,position);
                    startActivity(WearUpdateActivity.class,bundleChange);
                    break;
                case R.id.rrl_delete :
                    showDeleteDialog(dataBean,position);
                    break;
                case R.id.rl_content:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.WEAR_DETAILS,dataBean);
                    startActivity(WearDetailsActivity.class,bundle);
                    break;
            }
        });

        obtainDeviceList();
    }

    private void showDeleteDialog(DevicePageListResponse.DataBeanX.DataBean dataBean, int position) {

         new AlertDialog.Builder(mContext, R.style.BDAlertDialog)
                .setTitle("提示消息")
                .setMessage("确定要删除该终端吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteWear(dataBean,position,dialog);                    }
                })
                .setNegativeButton("取消", null)
                 .show();


//        mDeleteDialog = MessageDialog.build((AppCompatActivity) mContext)
//                .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
//                .setTheme(DialogSettings.THEME.LIGHT)
//                .setTitle("提示消息")
//                .setMessage("确定要删除该终端吗？")
//                .setOkButton("确定", new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//
//                        deleteWear(dataBean,position);
//                        return true;
//                    }
//                })
//                .setCancelButton("取消");
//
//        if(mContext instanceof MainActivity) {
//            mContext.getResources();
//        }
//        mDeleteDialog.show();
    }

    private void deleteWear(DevicePageListResponse.DataBeanX.DataBean dataBean, int position, DialogInterface dialog) {

        String token = mAppPreferences.getString(Constant.Token,"");
        //http://61.155.106.23:8080/lpro-lgb/service/api/ApiDeviceInfo/delDevice
        String url = Constant.URL + "/ApiDeviceInfo/delDevice";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",dataBean.getDevice_id())
                .add("device_no",dataBean.getDevice_no())
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    boolean success = CodeUtils.judgeSuccessNormal(baseResponse, mContext);
                    if(success) {
                        mWearListAdapter.remove(position);
                    }
                    dialog.dismiss();

                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    dialog.dismiss();
                });
    }

    private void obtainDeviceList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        //http://192.168.18.30:8080/lpro_lgb/service/api/ApiDeviceInfo/devicePageList?paged=1&pageSize=2  设备列表分页  paged当前页，pageSize每页条数
        String url = Constant.URL + "/ApiDeviceInfo/devicePageList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("paged",mCurrentPage)
                .add("pageSize",20)
                .asObject(DevicePageListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(deviceListResponse -> {

                    if(mRefreshWear.isRefreshing()) {
                        mRefreshWear.setRefreshing(false);
                    }
                    boolean judgeContinue = CodeUtils.judgeContinue(deviceListResponse, mContext);
                    if(judgeContinue) {

                        int code = deviceListResponse.getCode();
                        if(code == 0) {

                            DevicePageListResponse.DataBeanX data = deviceListResponse.getData();
                            List<DevicePageListResponse.DataBeanX.DataBean> dataList;
                            int totalPage = 0;
                            if(data != null) {
                                List<DevicePageListResponse.DataBeanX.DataBean> dataBeanList = data.getData();
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
                                mWearListAdapter.setNewData(dataList);
                                if(totalPage <= mCurrentPage ) {
                                    mWearListAdapter.loadMoreEnd(true);
                                }
                                mCurrentPage ++;

                            } else {
                                //加载更多
                                if(dataList.size() > 0) {
                                    mWearListAdapter.addData(dataList);
                                    if(totalPage == mCurrentPage) {
                                        mWearListAdapter.loadMoreEnd(true);
                                    } else {
                                        mWearListAdapter.loadMoreComplete();
                                    }
                                    mCurrentPage ++;
                                } else {
                                    mWearListAdapter.loadMoreEnd(true);
                                }
                            }
                        } else {
                            if(mRefreshWear.isRefreshing()) {
                                mRefreshWear.setRefreshing(false);
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    if(mRefreshWear.isRefreshing()) {
                        mRefreshWear.setRefreshing(false);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWearList(AddDeviceEvent addDeviceEvent){

        boolean success = addDeviceEvent.success;
        if(success) {
            if(addDeviceEvent.type == 1) {
                mRefreshWear.setRefreshing(true);
                mCurrentPage = 1;
                obtainDeviceList();
            }
//            else if(addDeviceEvent.type == 2) {
//                mWearListAdapter.updateItem(addDeviceEvent.position,addDeviceEvent.name);
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWearList(AlarmHandleEvent alarmHandleEvent){
        if(alarmHandleEvent.success) {
            mRefreshWear.setRefreshing(true);
            mCurrentPage = 1;
            obtainDeviceList();
        }
    }
}
