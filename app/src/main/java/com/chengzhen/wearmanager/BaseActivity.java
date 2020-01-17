package com.chengzhen.wearmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnBinder;
    protected Context mContext;
    protected RelativeLayout mRlTitle;
    protected LinearLayout mLlRoot;
    private LoadingDialog mLoadingDialog;
    private ImageView mIvLeft;
    private TextView mTvCenterTitle;
    private ImageView mIvLeftClose;
    private ImageView mIvRightMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //绑定Mvp
        bindMvp();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //应用内事件通讯
        EventBus.getDefault().register(this);
        //添加到activity栈
        ActivityManager.getInstance().addActivity(this);
        //最外层布局
        mLlRoot = findViewById(R.id.ll_root);
        //整个标题栏
        mRlTitle = findViewById(R.id.rl_title);
        mIvLeft = findViewById(R.id.iv_left_back);
        mTvCenterTitle = findViewById(R.id.tv_center_title);
        mIvLeftClose = findViewById(R.id.iv_left_close);
        mIvRightMore = findViewById(R.id.iv_right_more);

        mIvLeft.setOnClickListener(v -> finish());
        mIvLeftClose.setVisibility(View.GONE);
        mIvRightMore.setVisibility(View.GONE);

        View vgContent = getLayoutInflater().inflate(getLayoutID(), null);
        mLlRoot.addView(vgContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if(hasTitleBar()) {
            mRlTitle.setVisibility(View.VISIBLE);
        } else {
            mRlTitle.setVisibility(View.GONE);
        }

        mContext = this;

        mUnBinder = ButterKnife.bind(this);
        //初始化进度提示框
        if(mLoadingDialog == null){
            mLoadingDialog =new LoadingDialog.Builder(mContext)
                    .setMessage("加载中...")
                    .setCancelable(false)
                    .create();
        }
        init();
        getData();
    }

    protected void bindMvp(){

    }

    protected abstract int getLayoutID();

    protected boolean hasTitleBar(){
        return false;
    }

    protected abstract void init();
    protected abstract void getData();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.getInstance().removeActivity(this);

        if (mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }

        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EmptyEvent event) {

    }

    protected void startActivity(Class aClass) {
        startActivity(aClass, null);
    }

    protected void startActivity(Class aClass, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void showProDialogHint(){
        if(!mLoadingDialog.isShowing()){
            mLoadingDialog.show();
        }
    }

    protected void showProDialogHint(String msg){

        if(!mLoadingDialog.isShowing()){
            mLoadingDialog.setMessage(msg);
            mLoadingDialog.show();
        }
    }

    protected void hideProDialogHint(){
        if(mLoadingDialog != null){
            if(mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
    }

    protected void setLeftImg(int res, View.OnClickListener listener){

        mIvLeft.setImageResource(res);
        mIvLeft.setOnClickListener(listener);
    }

    protected void setLeftClickListener(View.OnClickListener listener){

        mIvLeft.setOnClickListener(listener);
    }

    protected void setCenterTitle(String title){
        mTvCenterTitle.setText(title);
    }

    protected void setCenterTitle(CharSequence title){
        mTvCenterTitle.setText(title);
    }

    protected void setCloseClickListener(View.OnClickListener listener){

        mIvLeftClose.setVisibility(View.VISIBLE);
        mIvLeftClose.setOnClickListener(listener);
    }

    protected void setMoreClickListener(View.OnClickListener listener){

        mIvRightMore.setVisibility(View.VISIBLE);
        mIvRightMore.setOnClickListener(listener);
    }
}
