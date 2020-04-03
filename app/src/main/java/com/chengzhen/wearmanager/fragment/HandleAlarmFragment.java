package com.chengzhen.wearmanager.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.activity.MainActivity;
import com.chengzhen.wearmanager.adapter.AlarmListAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.AlarmListBean;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.event.AlarmHandleEvent;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.view.RecycleViewDivider;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class HandleAlarmFragment extends BaseFragment {

    @BindView(R.id.rv_alarm)
    RecyclerView mRvAlarm;
    @BindView(R.id.refresh_alarm)
    SwipeRefreshLayout mRefreshAlarm;

    private AlarmListAdapter mAlarmListAdapter;
    private AppPreferences mAppPreferences;
    private int mCurrentPage = 1;
//    private MessageDialog mHandleDialog;
    private View mEmptyView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_handle_alarm;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(mContext);
        mAlarmListAdapter = new AlarmListAdapter();
        mRvAlarm.setLayoutManager(new LinearLayoutManager(mContext));
        RecycleViewDivider itemDecoration = new RecycleViewDivider.Builder(mContext)
                .setOrientation(RecycleViewDivider.VERTICAL)
                .setStyle(RecycleViewDivider.Style.BOTH)
                .setColorRes(R.color.color_F1F1F1)
                .setSize(COMPLEX_UNIT_DIP,10)
                .build();
        mRvAlarm.addItemDecoration(itemDecoration);
        mAlarmListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                obtainAlarmList();
            }
        },mRvAlarm);
        mAlarmListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AlarmListBean.DataBeanX.DataBean dataBean = mAlarmListAdapter.getData().get(position);
                int id = view.getId();
                switch (id) {
                    case R.id.tv_handle :
                        showHandleDialog(dataBean.getId(),position);
                        break;
                }
            }
        });
        mRvAlarm.setAdapter(mAlarmListAdapter);

        mRefreshAlarm.setOnRefreshListener(() -> {

            mCurrentPage = 1;
            obtainAlarmList();
        });

        obtainAlarmList();

        mEmptyView = getLayoutInflater().inflate(R.layout.empty_list, mRvAlarm, false);
    }

    private void showHandleDialog(long id, int position) {

//        mHandleDialog = MessageDialog.build((AppCompatActivity) mContext)
//                .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
//                .setTheme(DialogSettings.THEME.LIGHT)
//                .setTitle("提示消息")
//                .setMessage("确定要处理该警报吗？")
//                .setOkButton("确定", new OnDialogButtonClickListener() {
//                    @Override
//                    public boolean onClick(BaseDialog baseDialog, View v) {
//
//                        handleAlarm(id,position, dialog);
//                        return true;
//                    }
//                })
//                .setCancelButton("取消");
//
//        if(mContext instanceof MainActivity) {
//            mContext.getResources();
//        }
//        mHandleDialog.show();

        new AlertDialog.Builder(mContext, R.style.BDAlertDialog)
                .setTitle("提示消息")
                .setMessage("确定要处理该警报吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleAlarm(id,position,dialog);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void handleAlarm(long id, int position, DialogInterface dialog) {

        String token = mAppPreferences.getString(Constant.Token,"");
        //http://61.155.106.23:8080/lpro-lgb/service/api/ApiDeviceInfo/delDevice
        //http://127.0.0.1:8080/lpro_lgb/service/api/ApiAlarmInfo/alarmHandle?id=4
        String url = Constant.URL + "/ApiAlarmInfo/alarmHandle";  //ApiAlarmInfo/alarmHandle
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("id",id)
                .asObject(BaseResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(baseResponse -> {

                    boolean success = CodeUtils.judgeSuccessNormal(baseResponse, mContext);
                    if(success) {
                        mAlarmListAdapter.updateItem(position);
                        AlarmHandleEvent alarmHandleEvent = new AlarmHandleEvent();
                        EventBus.getDefault().post(alarmHandleEvent);
                    }
//                    mHandleDialog.doDismiss();
                    dialog.dismiss();

                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
//                    mHandleDialog.doDismiss();
                    dialog.dismiss();
                });
    }

    private void obtainAlarmList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        //api/ApiAlarmInfo/alarmAllPageList type 报警类型 0全部 1主动 2其他
        String url = Constant.URL + "/ApiAlarmInfo/alarmAllPageList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("paged",mCurrentPage)
                .add("pageSize",20)
                .add("status","")
                .add("type","1")
                .asObject(AlarmListBean.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(deviceListResponse -> {

                    if(mRefreshAlarm.isRefreshing()) {
                        mRefreshAlarm.setRefreshing(false);
                    }
                    boolean judgeContinue = CodeUtils.judgeContinue(deviceListResponse, mContext);
                    if(judgeContinue) {

                        int code = deviceListResponse.getCode();
                        if(code == 0) {

                            AlarmListBean.DataBeanX data = deviceListResponse.getData();
                            List<AlarmListBean.DataBeanX.DataBean> dataList;
                            int totalPage = 0;
                            if(data != null) {
                                List<AlarmListBean.DataBeanX.DataBean> dataBeanList = data.getData();
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
                                mAlarmListAdapter.setNewData(dataList);
                                if(dataList.size() == 0) {
                                    mAlarmListAdapter.setEmptyView(mEmptyView);
                                }
                                if(totalPage <= mCurrentPage) {
                                    mAlarmListAdapter.loadMoreEnd(true);
                                } else {
                                    mCurrentPage ++;
                                }
                            } else {
                                //加载更多
                                if(dataList.size() > 0) {
                                    mAlarmListAdapter.addData(dataList);
                                    if(totalPage == mCurrentPage) {
                                        mAlarmListAdapter.loadMoreEnd(true);
                                    } else {
                                        mAlarmListAdapter.loadMoreComplete();
                                    }
                                    mCurrentPage ++;
                                } else {
                                    mAlarmListAdapter.loadMoreEnd(true);
                                }
                            }
                        } else {
                            if(mRefreshAlarm.isRefreshing()) {
                                mRefreshAlarm.setRefreshing(false);
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    if(mRefreshAlarm.isRefreshing()) {
                        mRefreshAlarm.setRefreshing(false);
                    }
                });
    }
}
