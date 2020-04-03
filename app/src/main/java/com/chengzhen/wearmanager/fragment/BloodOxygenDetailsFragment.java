package com.chengzhen.wearmanager.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chengzhen.wearmanager.R;
import com.chengzhen.wearmanager.adapter.TemperatureAdapter;
import com.chengzhen.wearmanager.base.BaseFragment;
import com.chengzhen.wearmanager.util.SignsValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TemperatureDetailsFragment extends BaseFragment {

    @BindView(R.id.chart_signs)
    LineChart mChartSigns;
    @BindView(R.id.rv_form)
    RecyclerView mRvForm;
    @BindView(R.id.v_line)
    View mVline;
    private TemperatureAdapter mTemperatureAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_temperature_details;
    }

    @Override

    protected void initView() {
        initLineChart();
        initRecycleView();
    }

    private void initRecycleView() {

        mTemperatureAdapter = new TemperatureAdapter();
        mRvForm.setLayoutManager(new LinearLayoutManager(mContext));

        mRvForm.setAdapter(mTemperatureAdapter);
    }

    @Override
    protected void getData() {

        setLineChartData(9,10);
        // redraw
        mChartSigns.invalidate();

        List<String> stringList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            stringList.add("");
        }

        if(stringList.size() > 0) {
            mVline.setVisibility(View.VISIBLE);
        } else {
            mVline.setVisibility(View.INVISIBLE);
        }
        mTemperatureAdapter.setNewData(stringList);
    }

    private void initLineChart() {

        // no description text
        mChartSigns.getDescription().setEnabled(false);

        // enable touch gestures
        mChartSigns.setTouchEnabled(false);

//        mChartSigns.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChartSigns.setDragEnabled(false);
        mChartSigns.setScaleEnabled(false);
        mChartSigns.setDrawGridBackground(false);
        mChartSigns.setHighlightPerDragEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartSigns.setPinchZoom(false);

        // set an alternative background color
        mChartSigns.setBackgroundColor(Color.WHITE);

        // add data
//        seekBarX.setProgress(20);
//        seekBarY.setProgress(30);

//        chart.animateX(1500);

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

//        l.setYOffset(11f);

        XAxis xAxis = mChartSigns.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.parseColor("#01A99D"));
        xAxis.setAxisLineWidth(1);
        xAxis.setTextSize(11f);
        xAxis.setLabelCount(8);
        xAxis.setTextColor(Color.parseColor("#808080"));
        xAxis.setDrawGridLines(false);

        SignsValueFormatter xSignsValueFormatter = new SignsValueFormatter(SignsValueFormatter.TYPE_TIME);
        xAxis.setValueFormatter(xSignsValueFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

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
        leftAxis.setAxisMaximum(40f);
        leftAxis.setAxisMinimum(34f);
//        leftAxis.bo
//        mChartSigns.setDrawBorders(false);
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(5f, 10f, 0f);
        leftAxis.setGridColor(Color.parseColor("#BFE3DF"));
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChartSigns.getAxisRight();
//        rightAxis.setTypeface(tfLight);
        rightAxis.setEnabled(false);
        rightAxis.setTextColor(Color.RED);
//        rightAxis.setAxisMaximum(900);
//        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        LimitLine limitLine = new LimitLine(37.2F); //得到限制线
        limitLine.setLineWidth(1f); //宽度
        limitLine.setTextSize(10f);
        limitLine.setTextColor(Color.RED);  //颜色
        limitLine.setLineColor(Color.BLUE);
        leftAxis.addLimitLine(limitLine); //Y轴添加限制线
    }

    private void setLineChartData(int count, float range) {

//        ArrayList<Entry> values1 = new ArrayList<>();

//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * (range / 2f)) + 50;
//            values1.add(new Entry(i, val));
//        }

//        ArrayList<Entry> values2 = new ArrayList<>();
//
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * range) + 450;
//            values2.add(new Entry(i, val));
//        }
//
        ArrayList<Entry> values3 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 500;
            values3.add(new Entry(i + 10,   34 + i));
        }

        LineDataSet /*set1, set2, */set3;

        if (mChartSigns.getData() != null &&
                mChartSigns.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) mChartSigns.getData().getDataSetByIndex(0);
//            set2 = (LineDataSet) mChartSigns.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mChartSigns.getData().getDataSetByIndex(0);
//            set1.setValues(values1);
//            set2.setValues(values2);
            set3.setValues(values3);
            mChartSigns.getData().notifyDataChanged();
            mChartSigns.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
//            set1 = new LineDataSet(values1, "DataSet 1");
//
//            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set1.setColor(ColorTemplate.getHoloBlue());
//            set1.setCircleColor(Color.WHITE);
//            set1.setLineWidth(2f);
//            set1.setCircleRadius(3f);
//            set1.setFillAlpha(65);
//            set1.setFillColor(ColorTemplate.getHoloBlue());
//            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawCircleHole(false);

            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
//            set2 = new LineDataSet(values2, "DataSet 2");
//            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
//            set2.setColor(Color.RED);
//            set2.setCircleColor(Color.WHITE);
//            set2.setLineWidth(2f);
//            set2.setCircleRadius(3f);
//            set2.setFillAlpha(65);
//            set2.setFillColor(Color.RED);
//            set2.setDrawCircleHole(false);
//            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            set3 = new LineDataSet(values3, "DataSet 3");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.RED);
            set3.setCircleColor(Color.WHITE);
            set3.setLineWidth(2f);
            set3.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//            set3.setCircleRadius(3f);
//            set3.setFillAlpha(65);
//            set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
//            set3.setDrawCircleHole(false);

//            set3.setHighLightColor(Color.rgb(244, 117, 117));

            //隐藏节点值
            set3.setDrawValues(false);
            //隐藏节点圆点
            set3.setDrawCircles(false);

            // create a data object with the data sets
            LineData data = new LineData(set3);
//            data.setValueTextColor(Color.WHITE);
//            data.setValueTextSize(9f);

            // set data
            mChartSigns.setData(data);

            List<Entry> values = set3.getValues();
            for (Entry value : values) {

                Log.d("--TAG--", "setLineChartData: " + value.getX() + "=====" + value.getY());
            }
        }
    }
}
