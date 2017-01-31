package com.twt.service.party.ui.study;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.bean.CourseInfo;
import com.twt.service.party.bean.TextInfo;
import com.twt.service.party.ui.study.detail.StudyDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/8/18.
 */
public class StudyTextAdapter extends RecyclerView.Adapter<StudyTextAdapter.MyViewHolder>{

    private Context context;

    private List<TextInfo> list = new ArrayList<>();

    public StudyTextAdapter(Context context) {
        this.context = context;
    }

    public void addText(List<TextInfo> list){
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
        holder.tvCourseName.setText(list.get(position).getFile_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "此功能暂未开放！可以于网页端查看", Toast.LENGTH_SHORT).show();
//                StudyDetailActivity.actionStart(context,StudyDetailActivity.TYPE_TEXT,list.get(position).getFile_id());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_study_course_name)
        TextView tvCourseName;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
