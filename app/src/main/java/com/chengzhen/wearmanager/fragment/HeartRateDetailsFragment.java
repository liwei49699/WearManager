package com.chengzhen.wearmanager.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.adapter.BloodOxygenAdapter;
import com.chengzhen.wearmanager.adapter.HeartRateAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.BaseResponse;
import com.chengzhen.wearmanager.bean.BloodPressureCharResponse;
import com.chengzhen.wearmanager.bean.BloodPressureListResponse;
import com.chengzhen.wearmanager.bean.HeartRateCharResopnse;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.util.NumUtils;
import com.chengzhen.wearmanager.util.SignsTimeFormatter;
import com.chengzhen.wearmanager.util.SignsValueFormatter;
import com.chengzhen.wearmanager.util.SignsValueUtils;
import com.chengzhen.wearmanager.view.CustomLoadMoreView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class HeartRateDetailsFragment extends BaseFragment {

    @BindView(R.id.refresh_form)
    SwipeRefreshLayout mRefreshForm;
    @BindView(R.id.rv_form)
    RecyclerView mRvForm;
    LineChart mChartSigns;
    private HeartRateAdapter mHeartRateAdapter;
    private String mSignsId;
    private String mSignsNo;
    private AppPreferences mAppPreferences;
    private int mCurrentPage = 1;
    private TextView mTvHeartRateShow;
    private TextView mTvHeartRatetimeShow;
    private SignsTimeFormatter mXSignsValueFormatter;

    public static HeartRateDetailsFragment getInstance(String deviceId, String deviceNo) {
        HeartRateDetailsFragment fragment = new HeartRateDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constant.SIGNS_ID, deviceId);
        args.putString(Constant.SIGNS_NO, deviceNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_heart_rate_details;
    }

    @Override

    protected void initView() {
        initRecycleView();

        if (getArguments() != null) {
            mSignsId = getArguments().getString(Constant.SIGNS_ID);
            mSignsNo = getArguments().getString(Constant.SIGNS_NO);
        }

        mAppPreferences = new AppPreferences(mContext);
        mRefreshForm.setOnRefreshListener(() -> {

            mCurrentPage = 1;
            obtainHistoryChar();
            obtainHistoryList();
        });
    }

    private void initRecycleView() {

        mHeartRateAdapter = new HeartRateAdapter();
        mRvForm.setLayoutManager(new LinearLayoutManager(mContext));

        mRvForm.setAdapter(mHeartRateAdapter);
        mHeartRateAdapter.setLoadMoreView(new CustomLoadMoreView());
        mHeartRateAdapter.setOnLoadMoreListener(this::obtainHistoryList,mRvForm);

        initHeadView();
    }

    private void initHeadView() {

        View headView = LayoutInflater.from(mContext).inflate(R.layout.head_heart_rate, mRvForm, false);
        mHeartRateAdapter.addHeaderView(headView);
        mChartSigns = headView.findViewById(R.id.chart_signs);
        mTvHeartRateShow = headView.findViewById(R.id.tv_heart_rate_show);
        mTvHeartRatetimeShow = headView.findViewById(R.id.tv_heart_rate_time_show);
        initLineChart();
    }

    @Override
    protected void getData() {

        //历史数据
        obtainHistoryChar();
        obtainHistoryList();
    }

    private void initLineChart() {

        mChartSigns.getDescription().setEnabled(false);
        mChartSigns.setTouchEnabled(false);

        mChartSigns.setDragEnabled(false);
        mChartSigns.setScaleEnabled(false);
        mChartSigns.setDrawGridBackground(false);
        mChartSigns.setHighlightPerDragEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartSigns.setPinchZoom(false);

        // set an alternative background color
        mChartSigns.setBackgroundColor(Color.WHITE);

        // get the legend (only possible after setting data)
        Legend l = mChartSigns.getLegend();

        l.setEnabled(false);
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        mChartSigns.setPinchZoom(true);

        l.setYOffset(11f);

        XAxis xAxis = mChartSigns.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.parseColor("#01A99D"));
        xAxis.setAxisLineWidth(1);
        xAxis.setTextSize(11f);
//        xAxis.setLabelCount(8);
        xAxis.setTextColor(Color.parseColor("#808080"));
        xAxis.setDrawGridLines(false);

        mXSignsValueFormatter = new SignsTimeFormatter();
        xAxis.setValueFormatter(mXSignsValueFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = mChartSigns.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setEnabled(true);

        SignsValueFormatter ySignsValueFormatter = new SignsValueFormatter(SignsValueFormatter.TYPE_VALUE);
        leftAxis.setValueFormatter(ySignsValueFormatter);

        leftAxis.setTextColor(Color.parseColor("#808080"));
        leftAxis.setDrawAxisLine(true);
        leftAxis.setAxisLineColor(Color.parseColor("#01A99D"));
        leftAxis.setAxisLineWidth(1);
        leftAxis.setLabelCount(5);
        leftAxis.setDrawLabels(true);
//        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(150f);
        leftAxis.setAxisMinimum(39.f);
//        leftAxis.bo
//        mChartSigns.setDrawBorders(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(5f, 10f, 0f);
        leftAxis.setGridColor(Color.parseColor("#BFE3DF"));
        leftAxis.setGranularityEnabled(true);

        leftAxis.setCenterAxisLabels(true);


        YAxis rightAxis = mChartSigns.getAxisRight();
//        rightAxis.setTypeface(tfLight);
        rightAxis.setEnabled(false);
        rightAxis.setTextColor(Color.RED);
//        rightAxis.setAxisMaximum(900);
//        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        rightAxis.setCenterAxisLabels(true);
        mChartSigns.setData(new LineData());
        mChartSigns.invalidate();

    }

    private void obtainHistoryChar() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiStatisInfo/historyEchart";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",mSignsId)
                .add("device_no",mSignsNo)
                .add("type","XL")
                .asObject(HeartRateCharResopnse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(heartRateCharResopnse -> {
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }

                    boolean judgeContinue = CodeUtils.judgeContinue(heartRateCharResopnse, mContext);
                    if(judgeContinue) {

                        int code = heartRateCharResopnse.getCode();
                        if(code == 0) {

                            HeartRateCharResopnse.DataBean data = heartRateCharResopnse.getData();
                            HeartRateCharResopnse.DataBean.StandardBean standard = data.getStandard();
                            String xl_standard = standard.getXl_standard();
                            addLimitLine(NumUtils.stringToFloat(xl_standard),Color.parseColor("#F38018"));

                            List<HeartRateCharResopnse.DataBean.XlBean> xl = data.getXl();

                            addLineChartData(xl);
                            showDetails(xl.get(xl.size() - 1));
                        }
                    }

                }, throwable -> {
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }
                });
    }

    private void addLimitLine(float limit,int color){

        LimitLine limitLine = new LimitLine(limit); //得到限制线
        limitLine.setLineWidth(1f); //宽度
        limitLine.setLineColor(color);
        mChartSigns.getAxisLeft().addLimitLine(limitLine); //Y轴添加限制线
    }

    private void showDetails(HeartRateCharResopnse.DataBean.XlBean xlBean) {

        String xlBeanSdata = xlBean.getSdata();
        long xlBeanAtime = xlBean.getAtime();

        mTvHeartRateShow.setText(SignsValueUtils.judgeEmpty(xlBeanSdata) + "bpm");
        mTvHeartRatetimeShow.setText(SignsValueUtils.judgeTime(xlBeanAtime));
    }

    private void obtainHistoryList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiStatisInfo/historyList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",mSignsId)
                .add("device_no",mSignsNo)
                .add("type","XL")
                .add("paged",mCurrentPage)
                .add("pageSize",10)
                .asObject(BloodPressureListResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(bloodPressureListResponse -> {

                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }
                    boolean judgeContinue = CodeUtils.judgeContinue(bloodPressureListResponse, mContext);
                    if(judgeContinue) {

                        int code = bloodPressureListResponse.getCode();
                        if(code == 0) {

                            BloodPressureListResponse.DataBeanX data = bloodPressureListResponse.getData();
                            List<BloodPressureListResponse.DataBeanX.DataBean> dataList;
                            int totalPage = 0;
                            if(data != null) {
                                List<BloodPressureListResponse.DataBeanX.DataBean> dataBeanList = data.getData();
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
                                if(totalPage <= mCurrentPage ) {
                                    mHeartRateAdapter.loadMoreEnd(true);
                                    mHeartRateAdapter.mListEnd = true;
                                } else {
                                    mHeartRateAdapter.mListEnd = false;
                                }
                                mHeartRateAdapter.setNewData(dataList);
                                mCurrentPage ++;

                            } else {
                                //加载更多
                                if(dataList.size() > 0) {
                                    if(totalPage == mCurrentPage) {
                                        mHeartRateAdapter.mListEnd = true;
                                        mHeartRateAdapter.loadMoreEnd(true);
                                    } else {
                                        mHeartRateAdapter.mListEnd = false;
                                        mHeartRateAdapter.loadMoreComplete();
                                    }
                                    mHeartRateAdapter.addData(dataList);
                                    mCurrentPage ++;
                                } else {
                                    mHeartRateAdapter.mListEnd = true;
                                    mHeartRateAdapter.loadMoreEnd(true);
                                }
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtils.showShort("访问服务器异常");
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }
                });
    }

    private void addLineChartData(List<HeartRateCharResopnse.DataBean.XlBean> xl) {

        ArrayList<Entry> valuesDy = new ArrayList<>();

        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < xl.size(); i++) {
            longList.add(xl.get(i).getAtime());
        }

        mXSignsValueFormatter.setLongList(longList);

        for (int i = 0; i < xl.size(); i++) {
            valuesDy.add(new Entry(i,NumUtils.stringToFloat(xl.get(i).getSdata())));
        }

        LineDataSet setDy;

        if (mChartSigns.getData() != null &&
                mChartSigns.getData().getDataSetCount() > 0) {
            setDy = (LineDataSet) mChartSigns.getData().getDataSetByIndex(0);
            setDy.setValues(valuesDy);
            mChartSigns.getData().notifyDataChanged();
            mChartSigns.notifyDataSetChanged();
        } else {
            setDy = new LineDataSet(valuesDy, "DataSet_dy");
            setDy.setAxisDependency(YAxis.AxisDependency.LEFT);
            setDy.setColor(Color.parseColor("#15998B"));
            setDy.setCircleColor(Color.parseColor("#15998B"));
            setDy.setLineWidth(2f);
            setDy.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //隐藏节点值
            setDy.setDrawValues(longList.size() == 1);
            //隐藏节点圆点
            setDy.setDrawCircles(longList.size() == 1);
            LineData data = new LineData(setDy);
            // set data
            mChartSigns.setData(data);
        }
        mChartSigns.invalidate();
    }
}
