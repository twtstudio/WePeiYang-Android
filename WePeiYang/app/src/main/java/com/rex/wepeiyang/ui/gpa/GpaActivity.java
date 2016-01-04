package com.rex.wepeiyang.ui.gpa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rex.wepeiyang.bean.GpaCaptcha;
import com.rex.wepeiyang.interactor.GpaInteractorImpl;
import com.rex.wepeiyang.support.PrefUtils;
import com.rex.wepeiyang.support.StatusBarHelper;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.bind.BindActivity;
import com.rex.wepeiyang.ui.common.NextActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class GpaActivity extends BaseActivity implements GpaView, OnChartValueSelectedListener, View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.line_chart)
    LineChart lineChart;
    @InjectView(R.id.tv_zongjiaquan)
    TextView tvZongjiaquan;
    @InjectView(R.id.tv_zongjidian)
    TextView tvZongjidian;
    @InjectView(R.id.btn_order_by_score)
    Button btnOrderByScore;
    @InjectView(R.id.btn_order_by_credit)
    Button btnOrderByCredit;
    @InjectView(R.id.rv_my_score)
    RecyclerView rvMyScore;
    @InjectView(R.id.ll_my_score)
    LinearLayout llMyScore;
    @InjectView(R.id.pb_gpa)
    ProgressBar pbGpa;
    public static GpaPresenter presenter;

    private static boolean isOrderByScore = true;
    private GpaAdapter adapter;
    private List<Gpa.Data.Term.Course> courses = new ArrayList<>();
    private List<Gpa.Data.Term> terms = new ArrayList<>();
    private LineDataSet set;
    private int presentTermIndex;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GpaActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new GpaPresenterImpl(this, new GpaInteractorImpl());
        presenter.getGpaWithoutToken();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMyScore.setLayoutManager(layoutManager);
        adapter = new GpaAdapter(this);
        rvMyScore.setAdapter(adapter);
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
        //StatusBarHelper.setStatusBar(this, getResources().getColor(R.color.gpa_primary_color));
    }


    @Override
    public void showProgress() {
        pbGpa.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbGpa.setVisibility(View.GONE);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.text_secondary_color));
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(5);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaxValue(4f);
        yAxis.setStartAtZero(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);


        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Integer> circleColors = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            xVals.add(terms.get(i).name);
            if (i != terms.size() - 1) {
                circleColors.add(ContextCompat.getColor(this, R.color.text_secondary_color));
            } else {
                circleColors.add(ContextCompat.getColor(this, R.color.gpa_primary_color));
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
        set.setColor(ContextCompat.getColor(this, R.color.text_secondary_color));
        set.setValueTextColor(ContextCompat.getColor(this, R.color.text_secondary_color));
    }

    @Override
    public void showCaptchaDialog(GpaCaptcha gpaCaptcha) {
        CaptchaDialogFragment fragment = new CaptchaDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token", gpaCaptcha.token);
        bundle.putString("raw", gpaCaptcha.raw);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "Captcha Dialog");
    }

    @Override
    public void setClickable(boolean clickable) {
        if (clickable){
            btnOrderByScore.setOnClickListener(this);
            btnOrderByCredit.setOnClickListener(this);
        }
    }

    @Override
    public void startBindActivity() {
        BindActivity.actionStart(this, NextActivity.Gpa);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GpaActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        set.resetCircleColors();
        ArrayList<Integer> circleColors = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            if (i == e.getXIndex()) {
                circleColors.add(ContextCompat.getColor(this, R.color.gpa_primary_color));
            } else {
                circleColors.add(ContextCompat.getColor(this, R.color.text_secondary_color));
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
                    btnOrderByScore.setBackgroundColor(ContextCompat.getColor(this, R.color.gpa_dark_primary_color));
                    btnOrderByCredit.setBackgroundColor(ContextCompat.getColor(this, R.color.gpa_primary_color));
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
                    btnOrderByCredit.setBackgroundColor(ContextCompat.getColor(this, R.color.gpa_dark_primary_color));
                    btnOrderByScore.setBackgroundColor(ContextCompat.getColor(this, R.color.gpa_primary_color));
                }
        }
    }
}
