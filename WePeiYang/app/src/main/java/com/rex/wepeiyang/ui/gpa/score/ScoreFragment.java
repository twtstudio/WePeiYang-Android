package com.rex.wepeiyang.ui.gpa.score;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.Gpa;
import com.rex.wepeiyang.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class ScoreFragment extends BaseFragment implements ScoreView, View.OnClickListener {
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

    private static boolean isOrderByScore = true;
    private ScoreAdapter adapter;
    private List<Gpa.Data.Term.Course> courses = new ArrayList<>();

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void bindData(Gpa gpa) {
        tvZongjiaquan.setText(gpa.data.stat.total.score + "");
        tvZongjidian.setText(gpa.data.stat.total.gpa + "");
        courses.clear();
        courses.addAll(gpa.data.data.get(gpa.data.data.size() - 1).data);
        adapter.refreshItemsByScore(courses);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order_by_score:
                if (isOrderByScore) {
                    return;
                } else {
                    isOrderByScore = true;
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
                    adapter.refreshItemsByCredit(courses);
                    btnOrderByCredit.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gpa_dark_primary_color));
                    btnOrderByScore.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gpa_primary_color));
                }
        }
    }
}
