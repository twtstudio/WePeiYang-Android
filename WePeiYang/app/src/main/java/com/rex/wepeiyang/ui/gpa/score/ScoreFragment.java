package com.rex.wepeiyang.ui.gpa.score;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.support.PrefUtils;
import com.rex.wepeiyang.ui.BaseFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class ScoreFragment extends BaseFragment implements ScoreView, View.OnClickListener, OnChartValueSelectedListener {
    @InjectView(R.id.line_chart)
    LineChart lineChart;
    @InjectView(R.id.tv_zongjiaquan)
    TextView tvZongjiaquan;
    @InjectView(R.id.tv_zongjidian)
    TextView tvZongjidian;
    @InjectView(R.id.rv_my_score)
    RecyclerView rvMyScore;
    @InjectView(R.id.btn_order_by_score)
    Button btnOrderByScore;
    @InjectView(R.id.btn_order_by_credit)
    Button btnOrderByCredit;
    @InjectView(R.id.ll_my_score)
    LinearLayout llMyScore;

    private static boolean isOrderByScore = true;
    private ScoreAdapter adapter;
    private List<Gpa.Data.Term.Course> courses = new ArrayList<>();
    private List<Gpa.Data.Term> terms = new ArrayList<>();
    private LineDataSet set;
    private int presentTermIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_score, container, false);
        ButterKnife.inject(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvMyScore.setLayoutManager(layoutManager);
        adapter = new ScoreAdapter(getActivity());
        rvMyScore.setAdapter(adapter);
        btnOrderByScore.setOnClickListener(this);
        btnOrderByCredit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!PrefUtils.isKnowGpaUsage()) {
            final Snackbar snackbar = Snackbar.make(llMyScore, "点击折线图中的圆圈切换成绩列表所属学期", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("知道了", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    PrefUtils.setKnowGpaUsage(true);
                }
            });
            snackbar.show();
        }
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void bindData(Gpa gpa) {
        final DecimalFormat decimalFormat = new DecimalFormat("###.00");
        tvZongjiaquan.setText(gpa.data.stat.total.score + "");
        tvZongjidian.setText(decimalFormat.format(gpa.data.stat.total.gpa));
        terms.clear();
        terms.addAll(gpa.data.data);
        presentTermIndex = terms.size() - 1;
        courses.clear();
        courses.addAll(gpa.data.data.get(presentTermIndex).data);
        adapter.refreshItemsByScore(courses);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setNoDataTextDescription("还没有成绩哟");
        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(false);
        lineChart.animateX(1000);
        lineChart.setDrawBorders(false);
        lineChart.setDescription("");
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setPinchZoom(true);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setGridBackgroundColor(Color.BLACK);
        lineChart.setBorderWidth(3);
        lineChart.setDrawBorders(false);
        lineChart.getLegend().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_secondary_color));
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(5);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaxValue(4f);
        yAxis.setStartAtZero(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        //yAxis.setLabelCount(4, true);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);


        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> circleColors = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            xVals.add(terms.get(i).name);
            if (i != terms.size() - 1) {
                circleColors.add(ContextCompat.getColor(getActivity(), R.color.text_secondary_color));
            } else {
                circleColors.add(ContextCompat.getColor(getActivity(), R.color.gpa_primary_color));
            }
        }
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            double val = terms.get(i).stat.gpa;
            yVals.add(new Entry((float) val, i));
        }
        set = new LineDataSet(yVals, null);
        formatLineDataSet(set);
        set.setCircleColors(circleColors);
        LineData data = new LineData(xVals, set);
        lineChart.setData(data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order_by_score:
                if (isOrderByScore) {
                    return;
                } else {
                    isOrderByScore = true;
                    courses.clear();
                    courses.addAll(terms.get(presentTermIndex).data);
                    adapter.refreshItemsByScore(courses);
                    btnOrderByScore.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gpa_dark_primary_color));
                    btnOrderByCredit.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gpa_primary_color));
                }
                break;
            case R.id.btn_order_by_credit:
                if (!isOrderByScore) {
                    return;
                } else {
                    isOrderByScore = false;
                    courses.clear();
                    courses.addAll(terms.get(presentTermIndex).data);
                    adapter.refreshItemsByCredit(courses);
                    btnOrderByCredit.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gpa_dark_primary_color));
                    btnOrderByScore.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gpa_primary_color));
                }
        }
    }

    public void formatLineDataSet(LineDataSet set) {
        final DecimalFormat decimalFormat = new DecimalFormat("###.00");
        set.setCircleSize(4);
        set.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return decimalFormat.format(value);
            }
        });
        set.setDrawHighlightIndicators(false);
        set.setValueTextSize(10);
        set.setLineWidth(1);
        set.setColor(ContextCompat.getColor(getActivity(), R.color.text_secondary_color));
        set.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.text_secondary_color));
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        set.resetCircleColors();
        ArrayList<Integer> circleColors = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            if (i == e.getXIndex()) {
                circleColors.add(ContextCompat.getColor(getActivity(), R.color.gpa_primary_color));
            } else {
                circleColors.add(ContextCompat.getColor(getActivity(), R.color.text_secondary_color));
            }
        }
        presentTermIndex = e.getXIndex();
        set.setCircleColors(circleColors);
        if (isOrderByScore) {
            adapter.refreshItemsByScore(terms.get(e.getXIndex()).data);
        } else {
            adapter.refreshItemsByCredit(terms.get(e.getXIndex()).data);
        }
    }

    @Override
    public void onNothingSelected() {

    }
}
