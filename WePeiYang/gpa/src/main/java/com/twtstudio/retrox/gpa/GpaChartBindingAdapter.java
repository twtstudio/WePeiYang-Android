package com.twtstudio.retrox.gpa;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kelin.mvvmlight.command.ReplyCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by retrox on 2017/1/21.
 */

public class GpaChartBindingAdapter {

    @BindingAdapter(value={"gpaData","valueCommand"},requireAll = false)
    public static void setGpaChartData(LineChart chart, final GpaBean gpaData, final ReplyCommand<Integer> valueCommand){

//        Description description = new Description();
//        description.setText("GPA图示");
//        chart.setDescription(description);
        chart.getDescription().setEnabled(false);
        chart.setNoDataText("还没有成绩哟");

        if (gpaData == null){
            return;
        }

        if (valueCommand != null){
            chart.setTouchEnabled(true);
        }else {
            chart.setTouchEnabled(false);
        }

        List<String> xVals = Stream.of(gpaData.data)
                .map(term -> term.name)
                .collect(Collectors.toList());

//        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(false);
        chart.setDrawBorders(false);
        //chart.setDescription("");
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false);
        chart.setAutoScaleMinMaxEnabled(true);
//        chart.setGridBackgroundColor(Color.BLACK);
//        chart.setDrawBorders(true);
        chart.getLegend().setEnabled(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setSpaceMax(0.2f);
        xAxis.setSpaceMin(0.2f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xVals.get((int) value);
            }
        });

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setSpaceTop(20f);
        yAxis.setSpaceBottom(20f);
        yAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        ArrayList<Entry> yVals = new ArrayList<>();


        for (int i = 0; i < gpaData.data.size(); i++) {
            yVals.add(new Entry(i,(float)gpaData.data.get(i).stat.score));
        }


        LineDataSet dataSet = new LineDataSet(yVals,"加权成绩: "+String.valueOf(gpaData.stat.total.score));
//        LineDataSet xdataSet = new LineDataSet()

        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(ContextCompat.getDrawable(chart.getContext(),R.drawable.gpa_chart_background));
        //dataSet.setFillColor(Color.BLUE);

        dataSet.setValueTextSize(10f);
        dataSet.setCircleRadius(2f);
        dataSet.setLineWidth(1.5f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        int colorPink = Color.rgb(255,66,130);
        dataSet.setColor(colorPink);
        dataSet.setCircleColor(colorPink);
        dataSet.setValueTextColor(colorPink);

        LineData lineData = new LineData(dataSet);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (valueCommand!=null){
                    valueCommand.execute((int) e.getX());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });


        chart.setData(lineData);
        chart.invalidate();

//        List<Entry> entryList = new ArrayList<>();
//
//        for (GpaBean.Term term : gpaBean.data) {
//            entryList.add(term.name);
//        }
    }
}
