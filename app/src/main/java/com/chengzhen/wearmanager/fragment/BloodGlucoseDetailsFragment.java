package com.chengzhen.wearmanager.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzhen.wearmanager.Constant;
import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.adapter.BloodGlucoseAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.bean.BloodGlucoseCharResponse;
import com.chengzhen.wearmanager.bean.BloodPressureListResponse;
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

public class BloodGlucoseDetailsFragment extends BaseFragment {

    @BindView(R.id.refresh_form)
    SwipeRefreshLayout mRefreshForm;
    @BindView(R.id.rv_form)
    RecyclerView mRvForm;
    private BloodGlucoseAdapter mBloodGlucoseAdapter;
    private String mSignsId;
    private String mSignsNo;
    private TextView mTvMealBeforeShow;
    private TextView mTvMealBeforeTimeShow;
    private TextView mTvMealAfterShow;
    private TextView mTvMealAfterTimeShow;
    private SignsTimeFormatter mXSignsValueFormatter;
    private LineChart mChartSigns;
    private AppPreferences mAppPreferences;
    private int mCurrentPage = 1;

    public static BloodGlucoseDetailsFragment getInstance(String deviceId, String deviceNo) {
        BloodGlucoseDetailsFragment fragment = new BloodGlucoseDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constant.SIGNS_ID, deviceId);
        args.putString(Constant.SIGNS_NO, deviceNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blood_glucose_details;
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

        mBloodGlucoseAdapter = new BloodGlucoseAdapter();
        mRvForm.setLayoutManager(new LinearLayoutManager(mContext));
        mRvForm.setAdapter(mBloodGlucoseAdapter);
        mBloodGlucoseAdapter.setLoadMoreView(new CustomLoadMoreView());
        mBloodGlucoseAdapter.setOnLoadMoreListener(this::obtainHistoryList,mRvForm);

        initHeadView();
    }

    private void initHeadView() {

        View headView = LayoutInflater.from(mContext).inflate(R.layout.head_blood_glucose, mRvForm, false);
        mBloodGlucoseAdapter.addHeaderView(headView);
        mChartSigns = headView.findViewById(R.id.chart_signs);
        mTvMealBeforeShow = headView.findViewById(R.id.tv_meal_before_show);
        mTvMealBeforeTimeShow = headView.findViewById(R.id.tv_meal_before_time_show);
        mTvMealAfterShow = headView.findViewById(R.id.tv_meal_after_show);
        mTvMealAfterTimeShow = headView.findViewById(R.id.tv_meal_after_time_show);
        initLineChart();
    }

    @Override
    protected void getData() {

//        addLineChartData(new ArrayList<>(),new ArrayList<>());
//        mChartSigns.invalidate();

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
        leftAxis.setAxisMaximum(12f);
        leftAxis.setAxisMinimum(3.5f);
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
                .add("type","XT")
                .asObject(BloodGlucoseCharResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(bloodGlucoseCharResponse -> {
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }

                    boolean judgeContinue = CodeUtils.judgeContinue(bloodGlucoseCharResponse, mContext);
                    if(judgeContinue) {

                        int code = bloodGlucoseCharResponse.getCode();
                        if(code == 0) {

                            BloodGlucoseCharResponse.DataBean data = bloodGlucoseCharResponse.getData();
                            BloodGlucoseCharResponse.DataBean.StandardBean standard = data.getStandard();
                            String kfxt_standard = standard.getKfxt_standard();
                            String chxt_standard = standard.getChxt_standard();

                            addLimitLine(NumUtils.stringToFloat(chxt_standard),Color.parseColor("#F38018"));
                            addLimitLine(NumUtils.stringToFloat(kfxt_standard),Color.parseColor("#BBA387"));

                            List<BloodGlucoseCharResponse.DataBean.KfxtBean> kfxt = data.getKfxt();
                            List<BloodGlucoseCharResponse.DataBean.ChxtBean> chxt = data.getChxt();
                            addLineChartData(kfxt,chxt);
                            showDetails(kfxt.get(kfxt.size() - 1),chxt.get(chxt.size() - 1));
                        }
                    }

                }, throwable -> {
                    if(mRefreshForm.isRefreshing()) {
                        mRefreshForm.setRefreshing(false);
                    }
                });
    }

    private void showDetails(BloodGlucoseCharResponse.DataBean.KfxtBean kfxtBean, BloodGlucoseCharResponse.DataBean.ChxtBean chxtBean) {

        String sdataKu = kfxtBean.getSdata();
        long atimeKf = kfxtBean.getAtime();
        String sdataCh = chxtBean.getSdata();
        long atimeCh = chxtBean.getAtime();

        mTvMealBeforeShow.setText(SignsValueUtils.judgeEmpty(sdataKu) + "mmol/L");
        mTvMealBeforeTimeShow.setText(SignsValueUtils.judgeTime(atimeKf));
        mTvMealAfterShow.setText(SignsValueUtils.judgeEmpty(sdataCh) + "mmol/L");
        mTvMealAfterTimeShow.setText(SignsValueUtils.judgeTime(atimeCh));
    }

    private void obtainHistoryList() {

        String token = mAppPreferences.getString(Constant.Token, "");
        String url = Constant.URL + "/ApiStatisInfo/historyList";
        RxHttp.postForm(url)
                .addHeader("access-token",token)
                .add("device_id",mSignsId)
                .add("device_no",mSignsNo)
                .add("type","XT")
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
                                    mBloodGlucoseAdapter.loadMoreEnd(true);
                                    mBloodGlucoseAdapter.mListEnd = true;
                                } else {
                                    mBloodGlucoseAdapter.mListEnd = false;
                                }
                                mBloodGlucoseAdapter.setNewData(dataList);
                                mCurrentPage ++;

                            } else {
                                //加载更多
                                if(dataList.size() > 0) {
                                    if(totalPage == mCurrentPage) {
                                        mBloodGlucoseAdapter.mListEnd = true;
                                        mBloodGlucoseAdapter.loadMoreEnd(true);
                                    } else {
                                        mBloodGlucoseAdapter.mListEnd = false;
                                        mBloodGlucoseAdapter.loadMoreComplete();
                                    }
                                    mBloodGlucoseAdapter.addData(dataList);
                                    mCurrentPage ++;
                                } else {
                                    mBloodGlucoseAdapter.mListEnd = true;
                                    mBloodGlucoseAdapter.loadMoreEnd(true);
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

    private void addLimitLine(float limit,int color){

        LimitLine limitLine = new LimitLine(limit); //得到限制线
        limitLine.setLineWidth(1f); //宽度
        limitLine.setLineColor(color);
        mChartSigns.getAxisLeft().addLimitLine(limitLine); //Y轴添加限制线
    }

    private void addLineChartData(List<BloodGlucoseCharResponse.DataBean.KfxtBean> kfxtBeans, List<BloodGlucoseCharResponse.DataBean.ChxtBean> chxtBeans) {

        ArrayList<Entry> valuesGy = new ArrayList<>();
        ArrayList<Entry> valuesDy = new ArrayList<>();

        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < kfxtBeans.size(); i++) {
            longList.add(kfxtBeans.get(i).getAtime());
        }

        mXSignsValueFormatter.setLongList(longList);

        for (int i = 0; i < kfxtBeans.size(); i++) {
            valuesGy.add(new Entry(i,NumUtils.stringToFloat(kfxtBeans.get(i).getSdata())));
        }
        for (int i = 0; i < chxtBeans.size(); i++) {
            valuesDy.add(new Entry(i,NumUtils.stringToFloat(chxtBeans.get(i).getSdata())));
        }

        LineDataSet setChxt, setKfxt;

        if (mChartSigns.getData() != null &&
                mChartSigns.getData().getDataSetCount() > 0) {
            setChxt = (LineDataSet) mChartSigns.getData().getDataSetByIndex(0);
            setKfxt = (LineDataSet) mChartSigns.getData().getDataSetByIndex(1);
            setChxt.setValues(valuesGy);
            setKfxt.setValues(valuesDy);
            mChartSigns.getData().notifyDataChanged();
            mChartSigns.notifyDataSetChanged();
        } else {

            setChxt = new LineDataSet(valuesGy, "DataSet_gy");
            setChxt.setAxisDependency(YAxis.AxisDependency.LEFT);
            setChxt.setColor(Color.parseColor("#E5001D"));
            setChxt.setCircleColor(Color.parseColor("#E5001D"));
            setChxt.setLineWidth(2f);
            setChxt.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //隐藏节点值
            setChxt.setDrawValues(longList.size() == 1);
            //隐藏节点圆点
            setChxt.setDrawCircles(longList.size() == 1);

            setKfxt = new LineDataSet(valuesDy, "DataSet_dy");
            setKfxt.setAxisDependency(YAxis.AxisDependency.LEFT);
            setKfxt.setColor(Color.parseColor("#15998B"));
            setKfxt.setCircleColor(Color.parseColor("#15998B"));
            setKfxt.setLineWidth(2f);
            setKfxt.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //隐藏节点值
            setKfxt.setDrawValues(longList.size() == 1);
            //隐藏节点圆点
            setKfxt.setDrawCircles(longList.size() == 1);
            LineData data = new LineData(setChxt,setKfxt);
            // set data
            mChartSigns.setData(data);
        }
        mChartSigns.invalidate();
    }

}
