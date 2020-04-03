package com.chengzhen.wearmanager.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.adapter.TestAdapter;
import com.chengzhen.wearmanager.base.BaseActivity;
import com.chengzhen.wearmanager.bean.PeopleListResponse;
import com.chengzhen.wearmanager.event.AddDeviceEvent;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.view.RecycleViewDivider;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class PeopleListActivity extends BaseActivity {

    @BindView(R.id.rv_people_list)
    RecyclerView mRvPeopleList;
    private AppPreferences mAppPreferences;
    private TestAdapter mTestAdapter;
    private int mDeviceType;

    @Override
    protected boolean hasTitleBar() {
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_people_list;
    }

    @Override
    protected void init() {

        //状态栏颜色，不写默认透明色
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true)
                .init();

        setCenterTitle("选择使用者");
    }

    @Override
    protected void getData() {

        mAppPreferences = new AppPreferences(this);

        mDeviceType = getIntent().getIntExtra(Constant.DEVICE_TYPE, 0);
        mTestAdapter = new TestAdapter();
        RecycleViewDivider itemDecoration = new RecycleViewDivider.Builder(mContext)
                .setOrientation(RecycleViewDivider.VERTICAL)
                .setStyle(RecycleViewDivider.Style.BOTH)
                .setColorRes(R.color.color_F1F1F1)
                .setSize(COMPLEX_UNIT_DIP,5)
                .build();
        mRvPeopleList.addItemDecoration(itemDecoration);
        mRvPeopleList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvPeopleList.setAdapter(mTestAdapter);

        mTestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                PeopleListResponse.DataBean dataBean = mTestAdapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.PEOPLE_INFO,dataBean);
                if(mDeviceType == 0) {
                    startActivity(AddWearActivity.class,bundle);
                } else {
                    startActivity(AddSignsActivity.class,bundle);
                }
            }
        });

        testPeopleList();
    }

    private void testPeopleList() {

        String token = mAppPreferences.getString(Constant.Token, "");
//        showProDialogHint();
        String url = Constant.URL + "/ApiDeviceInfo/nodeUserList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .asObject(PeopleListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(peopleListResponse -> {

                    boolean success = CodeUtils.judgeSuccessOther(peopleListResponse, mContext);
                    if(success) {

//                        if(TextUtils.isEmpty(mSosNumber1) && TextUtils.isEmpty(mSosNumber2) && TextUtils.isEmpty(mSosNumber3)) {
//                            ToastUtils.showShort("设备添加成功");
//                            AddDeviceEvent addDeviceEvent = new AddDeviceEvent();
//                            addDeviceEvent.success = true;
//                            addDeviceEvent.type = 1;
//                            EventBus.getDefault().post(addDeviceEvent);
//                            finish();
//                        } else {
//                            setSosInfo();
//                        }
                        List<PeopleListResponse.DataBean> data = peopleListResponse.getData();

                        mTestAdapter.setNewData(data);
//                        String name = "";
//                        for (PeopleListResponse.DataBean datum : data) {
//
//                            name = name + " " + datum.getDeviceName() + " ";
//
//                            Log.d("--TAG--", "testPeopleList: " + datum);
//                        }

//                        ToastUtils.showShort(name);
//                        hideProDialogHint();

                    } else {
                        ToastUtils.showShort(peopleListResponse.getMsg());
//                        hideProDialogHint();
                    }
                }, throwable -> {
                    //出错
                    ToastUtils.showShort("访问服务器错误");
//                    hideProDialogHint();
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closePeopleList(AddDeviceEvent addDeviceEvent){

        boolean success = addDeviceEvent.success;
        if(success) {
            if(addDeviceEvent.type == 1 || addDeviceEvent.type == 3) {
                finish();
            }
        }
    }
}
