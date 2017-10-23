package com.twt.service.home.common.gpaItem;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.twt.service.R;
import com.twtstudio.retrox.gpa.GpaChartBindingAdapter;

/**
 * Created by retrox on 22/10/2017.
 */

public class GpaItemViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public LineChart lineChart;

    public GpaItemViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.card_item_gpa);
        lineChart = itemView.findViewById(R.id.chart_item_gpa);
    }

    public void bind(LifecycleOwner owner, GpaItemViewModel viewModel) {
        viewModel.observableGpa.observe(owner, gpaBean -> {
            GpaChartBindingAdapter.setGpaChartData(lineChart, gpaBean, null);
        });
    }
}
