package com.twtstudio.retrox.gpa;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.gpa.view.TermCourseViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by retrox on 2017/1/29.
 */

public class GpaRadarBindingAdapter {
    @BindingAdapter({"gpaRadar"})
    public static void setGpaRadarData(RadarChart chart, ObservableList<ViewModel> viewModels){
        chart.setNoDataText("gg");

        if (null==viewModels||viewModels.size()==0){
            return;
        }

        List<DataType> list = Stream.of(viewModels)
                .map(viewModel -> (TermCourseViewModel)viewModel)
                .map(termCourseViewModel -> new DataType(termCourseViewModel.name.get(),
                        Double.valueOf(termCourseViewModel.score.get())))
//                .sortBy(dataType -> dataType.score)
                .collect(Collectors.toList());

        List<String> courseNameList = Stream.of(list)
                .map(DataType::getName)
                .collect(Collectors.toList());

        List<RadarEntry> entries = Stream.of(list)
                .map(dataType -> new RadarEntry((float) dataType.score))
                .collect(Collectors.toList());


        RadarDataSet set1 = new RadarDataSet(entries, "Last Week");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setValueTextColor(Color.WHITE);
        set1.setValueTextSize(10f);
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarData data = new RadarData(set1);

        chart.setData(data);

        chart.setBackgroundColor(Color.rgb(60, 65, 82));


        chart.getDescription().setEnabled(false);

        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.LTGRAY);
        chart.setWebAlpha(100);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return courseNameList.get((int) (value%courseNameList.size()));
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = chart.getYAxis();
        yAxis.setTextSize(9f);
        yAxis.setDrawLabels(false);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);
        float min = Stream.of(list)
                .map(dataType -> (float)dataType.score)
                .min(Float::compare)
                .get();
        yAxis.setAxisMinimum(min - 20f);
        yAxis.setTextColor(Color.WHITE);
//        yAxis.setAxisMaximum(100f);
//        yAxis.setSpaceTop(0f);

        Legend l = chart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(5f);
//        l.setTextColor(Color.WHITE);

        chart.setTouchEnabled(false);
        chart.setRotationEnabled(false);
//        chart.setFitsSystemWindows(true);
        chart.invalidate();
    }

    public static class DataType{
        public String name;
        public double score;

        public DataType(String name, double score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public double getScore() {
            return score;
        }
    }
}
