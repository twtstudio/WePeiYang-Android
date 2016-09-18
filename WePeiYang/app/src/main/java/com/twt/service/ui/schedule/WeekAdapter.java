package com.twt.service.ui.schedule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.service.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/9/11.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    Context context;

    List<String> mList;

    private ScheduleView mScheduleView;

    public WeekAdapter(Context context, ScheduleView view) {
        this.context = context;
        mScheduleView = view;
        if(mList == null){
            mList = new ArrayList<>();
        }
        for (int i = 0; i < 25; i++) {
            mList.add("第" + i + "周");
        }
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new WeekViewHolder(inflater.inflate(R.layout.item_week, parent, false));
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScheduleView.changeWeek(holder.mTextView.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class WeekViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.tv_weeki)
        TextView mTextView;
        public WeekViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
