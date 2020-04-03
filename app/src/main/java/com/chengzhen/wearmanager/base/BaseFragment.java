package com.chengzhen.wearmanager.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chengzhen.wearmanager.event.EmptyEvent;
import com.chengzhen.wearmanager.view.LoadingDialog;
import com.chengzhen.wearmanager.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public abstract class BaseFragment extends Fragment {

    protected Activity mContext;
    protected BaseFragment mFragment;

    private LoadingDialog mLoadingDialog;
    Unbinder unbinder;
    protected LinearLayout mLlFragmentRoot;
    protected TextView mTvFragmentTitle;
    private TextView mTvRightMore;
    private ImageView mIvFragmentMore;
    private LinearLayout mLlAdd;

    public final String TAG = getClass().getSimpleName();

    @Override
    public void onAttach(@NonNull Context context) {
        bindMvp();
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mFragment = this;
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_base,container,false);

        mLlFragmentRoot =  contentView.findViewById(R.id.ll_fragment_root);
        View vgContent;
        if(getLayoutId() != 0) {

            vgContent = mContext.getLayoutInflater().inflate(getLayoutId(), mLlFragmentRoot, false);
        } else {

            vgContent = bindView(bindText());
        }
        mLlFragmentRoot.addView(vgContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    protected String bindText(){

        return "默认文本";
    }

    protected abstract int getLayoutId();

    private View bindView(String msg){

        TextView textView = new TextView(mContext);
        textView.setText(msg);

        textView.setTextSize(COMPLEX_UNIT_SP,20);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mContext, null);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        return textView;
    }


    /**
     * 强制子类重写，实现子类特有的ui
     * @return
     */
    protected abstract void initView();

    protected abstract void getData();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout mRlFragmentTitle = view.findViewById(R.id.rl_fragment_title);
        mIvFragmentMore = view.findViewById(R.id.iv_fragment_more);
        mTvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        mIvFragmentMore.setVisibility(View.GONE);
        mTvRightMore = view.findViewById(R.id.tv_right_more);
        mTvRightMore.setVisibility(View.GONE);
        mLlAdd = view.findViewById(R.id.ll_add);
        mLlAdd.setVisibility(View.GONE);
        if(hasTitleBar()) {
            mRlFragmentTitle.setVisibility(View.VISIBLE);
        } else {
            mRlFragmentTitle.setVisibility(View.GONE);
        }
    }

    protected boolean hasTitleBar() {
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化进度提示框
        if(mLoadingDialog == null){

            mLoadingDialog =new LoadingDialog.Builder(mContext)
                    .setMessage("加载中...")
                    .setCancelable(false)
                    .create();
        }

        initView();
        mViewCreated = true;
        if(mFragmentVisible && !mHasLoad) {
            mHasLoad = true;
        }
        getData();
    }

    protected void bindMvp(){

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private boolean mFragmentVisible;
    private boolean mViewCreated;
    private boolean mHasLoad;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            mFragmentVisible = true;
            if(mViewCreated && !mHasLoad) {
                mHasLoad = true;
//                getData();
            }
        } else {
            mFragmentVisible = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EmptyEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
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

    protected void startActivity(Class aClass){

        startActivity(aClass,null);
    }

    protected void startActivity(Class aClass, Bundle bundle){

        Intent intent = new Intent(mContext, aClass);
        if(bundle != null) {

            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void setCenterTitle(String title){
        mTvFragmentTitle.setText(title);
    }

    protected void setCenterTitle(CharSequence title){
        mTvFragmentTitle.setText(title);
    }

    protected void setMoreClickListener(@DrawableRes int resId, View.OnClickListener listener){

        mIvFragmentMore.setVisibility(View.VISIBLE);
        mIvFragmentMore.setImageResource(resId);
        mIvFragmentMore.setOnClickListener(listener);
    }

    protected void setRightMsgClick(String msg, View.OnClickListener listener){

        mTvRightMore.setVisibility(View.VISIBLE);
        mTvRightMore.setText(msg);
        mTvRightMore.setOnClickListener(listener);
    }

    protected void setAddListener(View.OnClickListener listener){

        mLlAdd.setVisibility(View.VISIBLE);
        mLlAdd.setOnClickListener(listener);
    }
}
