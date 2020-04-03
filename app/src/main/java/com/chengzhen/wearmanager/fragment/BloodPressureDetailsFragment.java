package com.chengzhen.wearmanager.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.chengzhen.wearmanager.adapter.BloodPressureAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.BloodPressureCharResponse;
import com.chengzhen.wearmanager.bean.BloodPressureListResponse;
import com.chengzhen.wearmanager.util.CodeUtils;
import com.chengzhen.wearmanager.util.DateUtils;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rxjava.rxlife.RxLife;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import rxhttp.wrapper.param.RxHttp;

public class BloodPressureDetailsFragment extends BaseFragment {

    @BindView(R.id.refresh_form)
    SwipeRefreshLayout mRefreshForm;
    @BindView(R.id.rv_form)
    RecyclerView mRvForm;
    private BloodPressureAdapter mBloodPressureAdapter;
    private String mSignsId;
    private String mSignsNo;
    private AppPreferences mAppPreferences;
    private int mCurrentPage = 1;
    private LineChart mChartSigns;
    private SignsTimeFormatter mXSignsValueFormatter;
    private TextView mTvHighPressureShow;
    private TextView mTvHighPressureTimeShow;
    private TextView mTvLowPressureShow;
    private TextView mTvLowPressureTimeShow;

    public static BloodPressureDetailsFragment getInstance(String deviceId, String deviceNo) {
        BloodPressureDetailsFragment fragment = new BloodPressureDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constant.SIGNS_ID, deviceId);
        args.putString(Constant.SIGNS_NO, deviceNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blood_pressure_details;
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

        mBloodPressureAdapter = new BloodPressureAdapter();
        mRvForm.setLayoutManager(new LinearLayoutManager(mContext));
        mRvForm.setAdapter(mBloodPressureAdapter);
        mBloodPressureAdapter.setLoadMoreView(new CustomLoadMoreView());
        mBloodPressureAdapter.setOnLoadMoreListener(this::obtainHistoryList,mRvForm);

        initHeadView();
    }

    private void initHeadView() {

        View headView = LayoutInflater.from(mContext).inflate(R.layout.head_blood_pressure, mRvForm, false);
        mBloodPressureAdapter.addHeaderView(headView);
        mChartSigns = headView.findViewById(R.id.chart_signs);
        mTvHighPressureShow = headView.findViewById(R.id.tv_high_pressure_show);
        mTvHighPressureTimeShow = headView.findViewById(R.id.tv_high_pressure_time_show);
        mTvLowPressureShow = headView.findViewById(R.id.tv_low_pressure_show);
        mTvLowPressureTimeShow = headView.findViewById(R.id.tv_low_pressure_time_show);
        initLineChart();
    }

    @Override
    protected void getData() {

//        addLineChartData(new ArrayList<>(),new ArrayList<>());

        //历史数据
        obtainHistoryChar();
        obtainHistoryList();
    }

    private void obtainHistoryChar() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiStatisInfo/historyEchart";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",mSignsId)
                .add("device_no",mSignsNo)
                .add("type","XY")
                .asObject(BloodPressureCharResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(bloodPressureCharResponse -> {
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }

                    boolean judgeContinue = CodeUtils.judgeContinue(bloodPressureCharResponse, mContext);
                    if(judgeContinue) {

                        int code = bloodPressureCharResponse.getCode();
                        if(code == 0) {

                            BloodPressureCharResponse.DataBean data = bloodPressureCharResponse.getData();
                            BloodPressureCharResponse.DataBean.StandardBean standard = data.getStandard();
                            String gy_standard = standard.getGy_standard();
                            String dy_standard = standard.getDy_standard();

                            addLimitLine(NumUtils.stringToFloat(gy_standard),Color.parseColor("#F38018"));
                            addLimitLine(NumUtils.stringToFloat(dy_standard),Color.parseColor("#BBA387"));
                            List<BloodPressureCharResponse.DataBean.GyBean> gy = data.getGy();
                            List<BloodPressureCharResponse.DataBean.DyBean> dy = data.getDy();
                            addLineChartData(gy,dy);
                            showDetails(gy.get(gy.size() - 1),dy.get(dy.size() - 1));
                        }
                    }

                }, throwable -> {
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }
                });
    }

    private void showDetails(BloodPressureCharResponse.DataBean.GyBean gyBean, BloodPressureCharResponse.DataBean.DyBean dyBean) {

        String sdataGy = gyBean.getSdata();
        long atimeGy = gyBean.getAtime();
        String sdataDy = dyBean.getSdata();
        long atimeDy = dyBean.getAtime();

        mTvHighPressureShow.setText(SignsValueUtils.judgeEmpty(sdataGy) + "mmHg");
        mTvHighPressureTimeShow.setText(SignsValueUtils.judgeTime(atimeGy));
        mTvLowPressureShow.setText(SignsValueUtils.judgeEmpty(sdataDy) + "mmHg");
        mTvLowPressureTimeShow.setText(SignsValueUtils.judgeTime(atimeDy));
    }

    private void obtainHistoryList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiStatisInfo/historyList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",mSignsId)
                .add("device_no",mSignsNo)
                .add("type","XY")
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
                                    mBloodPressureAdapter.loadMoreEnd(true);
                                    mBloodPressureAdapter.mListEnd = true;
                                } else {
                                    mBloodPressureAdapter.mListEnd = false;
                                }
                                mBloodPressureAdapter.setNewData(dataList);
                                mCurrentPage ++;

                            } else {
                                //加载更多
                                if(dataList.size() > 0) {
                                    if(totalPage == mCurrentPage) {
                                        mBloodPressureAdapter.mListEnd = true;
                                        mBloodPressureAdapter.loadMoreEnd(true);
                                    } else {
                                        mBloodPressureAdapter.mListEnd = false;
                                        mBloodPressureAdapter.loadMoreComplete();
                                    }
                                    mBloodPressureAdapter.addData(dataList);
                                    mCurrentPage ++;
                                } else {
                                    mBloodPressureAdapter.mListEnd = true;
                                    mBloodPressureAdapter.loadMoreEnd(true);
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
        leftAxis.setAxisMaximum(160f);
        leftAxis.setAxisMinimum(69.5f);
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

    private void addLimitLine(float limit,int color){

        LimitLine limitLine = new LimitLine(limit); //得到限制线
        limitLine.setLineWidth(1f); //宽度
        limitLine.setLineColor(color);
        mChartSigns.getAxisLeft().addLimitLine(limitLine); //Y轴添加限制线
    }

    private void addLineChartData(List<BloodPressureCharResponse.DataBean.GyBean> gyBeans, List<BloodPressureCharResponse.DataBean.DyBean> dyBeans) {

        ArrayList<Entry> valuesGy = new ArrayList<>();
        ArrayList<Entry> valuesDy = new ArrayList<>();

        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < gyBeans.size(); i++) {
            longList.add(gyBeans.get(i).getAtime());
        }

        mXSignsValueFormatter.setLongList(longList);

        for (int i = 0; i < gyBeans.size(); i++) {
            valuesGy.add(new Entry(i,NumUtils.stringToFloat(gyBeans.get(i).getSdata())));
        }
        for (int i = 0; i < dyBeans.size(); i++) {
            valuesDy.add(new Entry(i,NumUtils.stringToFloat(dyBeans.get(i).getSdata())));
        }

        LineDataSet setGy, setDy;

        if (mChartSigns.getData() != null &&
                mChartSigns.getData().getDataSetCount() > 0) {
            setGy = (LineDataSet) mChartSigns.getData().getDataSetByIndex(0);
            setDy = (LineDataSet) mChartSigns.getData().getDataSetByIndex(1);
            setGy.setValues(valuesGy);
            setDy.setValues(valuesDy);
            mChartSigns.getData().notifyDataChanged();
            mChartSigns.notifyDataSetChanged();
        } else {

            setGy = new LineDataSet(valuesGy, "DataSet_gy");
            setGy.setAxisDependency(YAxis.AxisDependency.LEFT);
            setGy.setColor(Color.parseColor("#E5001D"));
            setGy.setCircleColor(Color.parseColor("#E5001D"));
            setGy.setLineWidth(2f);
            setGy.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //隐藏节点值
            setGy.setDrawValues(longList.size() == 1);
            //隐藏节点圆点
            setGy.setDrawCircles(longList.size() == 1);

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
            LineData data = new LineData(setGy,setDy);
            // set data
            mChartSigns.setData(data);
        }
        mChartSigns.invalidate();
    }
}
