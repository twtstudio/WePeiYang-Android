package com.twt.service.party.ui.inquiry.Score20;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.party.bean.ScoreInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/7/29.
 */

public class Score20Adapter extends RecyclerView.Adapter<Score20Adapter.MyViewHolder> {


    private Context context;

    private List<ScoreInfo> list = new ArrayList<>();

    public Score20Adapter(Context context) {
        this.context = context;
    }

    public void addScore(List<ScoreInfo> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_party_score, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvScoreCourseName.setText(list.get(position).getCourse_name());
        holder.tvScoreCompleteTime.setText(list.get(position).getComplete_time().replace(" ","\n"));
        holder.tvScoreScore.setText(list.get(position).getScore());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_score_course_name)
        TextView tvScoreCourseName;
        @InjectView(R.id.tv_score_complete_time)
        TextView tvScoreCompleteTime;
        @InjectView(R.id.tv_score_score)
        TextView tvScoreScore;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
