package com.twtstudio.tjliqy.party.ui.study.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.CourseDetailInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by tjliqy on 2016/8/18.
 */
public class StudyDetailAdapter extends RecyclerView.Adapter<StudyDetailAdapter.MyViewHolder>{
    private Context context;

    private List<CourseDetailInfo.DataBean> list = new ArrayList<>();

    public StudyDetailAdapter(Context context) {
        this.context = context;
    }

    public void addCourse(List<CourseDetailInfo.DataBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_party_study, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvCourseName.setText(list.get(position).getArticle_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyDetailActivity.actionStart(context,StudyDetailActivity.TYPE_COURSE,list.get(position).getArticle_name(), list.get(position).getArticle_content());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_study_course_name)
        TextView tvCourseName;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
