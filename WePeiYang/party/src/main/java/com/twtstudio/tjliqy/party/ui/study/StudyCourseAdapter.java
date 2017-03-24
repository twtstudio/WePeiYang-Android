package com.twtstudio.tjliqy.party.ui.study;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.CourseInfo;
import com.twtstudio.tjliqy.party.support.ResourceHelper;
import com.twtstudio.tjliqy.party.ui.study.detail.StudyDetailListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by tjliqy on 2016/8/17.
 */
public class StudyCourseAdapter extends RecyclerView.Adapter<StudyCourseAdapter.MyViewHolder> {

    private Context context;

    private List<CourseInfo> list = new ArrayList<>();

    public StudyCourseAdapter(Context context) {
        this.context = context;
    }

    public void addCourse(List<CourseInfo> list){
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
        holder.tvCourseName.setText(list.get(position).getCourse_name());
        if(list.get(position).getStatus() == 1){
            holder.tvStatus.setText("已完成");
            holder.tvStatus.setTextColor(ResourceHelper.getColor(R.color.myTextPrimaryColorGreen));
        }
        holder.tvStatus.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyDetailListActivity.actionStart(context,list.get(position).getCourse_id());
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
        @BindView(R2.id.tv_study_course_status)
        TextView tvStatus;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
