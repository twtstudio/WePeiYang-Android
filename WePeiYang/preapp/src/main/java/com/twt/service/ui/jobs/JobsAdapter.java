package com.twt.service.ui.jobs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.JobsItem;
import com.twt.service.ui.jobs.jobsdetails.JobsDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 16/2/14.
 */
public class JobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JobsItem> dataSet = new ArrayList<>();
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private static final int ITEM_VIEW_TYPE_FOOTER = 2;
    private Context context;

    public JobsAdapter(Context context) {
        this.context = context;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_jobs_title)
        TextView tvJobsTitle;
        @InjectView(R.id.tv_view_count)
        TextView tvViewCount;
        @InjectView(R.id.tv_jobs_date)
        TextView tvJobsDate;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    static class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_VIEW_TYPE_ITEM) {
            return new ItemHolder(inflater.inflate(R.layout.item_jobs, parent, false));
        } else {
            return new FooterHolder(inflater.inflate(R.layout.footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_VIEW_TYPE_ITEM) {
            ItemHolder itemHolder = (ItemHolder) holder;
            final JobsItem item = dataSet.get(position);
            itemHolder.tvJobsTitle.setText(item.title);
            itemHolder.tvJobsDate.setText(item.date);
            itemHolder.tvViewCount.setText(item.click+"");
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JobsDetailsActivity.actionStart(context, item.id);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataSet.size()) {
            return ITEM_VIEW_TYPE_FOOTER;
        } else {
            return ITEM_VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    public void add(List<JobsItem> items) {
        for (JobsItem item : items) {
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }

    public void refresh(List<JobsItem> items) {
        dataSet.clear();
        dataSet.addAll(items);
        notifyDataSetChanged();
    }
}
