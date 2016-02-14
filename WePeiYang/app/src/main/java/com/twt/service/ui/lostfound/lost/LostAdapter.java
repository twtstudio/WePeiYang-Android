package com.twt.service.ui.lostfound.lost;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.Lost;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by RexSun on 15/8/18.
 */
public class LostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context context;
    private ArrayList<Lost> dataSet = new ArrayList<>();

    public LostAdapter(Context context) {
        this.context = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_lost_item_category)
        ImageView ivCategory;
        @InjectView(R.id.tv_lost_item_date)
        TextView tvDate;
        @InjectView(R.id.tv_lost_item_time)
        TextView tvTime;
        @InjectView(R.id.tv_lost_item_position)
        TextView tvPosition;
        @InjectView(R.id.tv_lost_item_name)
        TextView tvName;
        @InjectView(R.id.tv_lost_item_number)
        TextView tvNumber;
        @InjectView(R.id.tv_lost_item_detail)
        TextView tvDetail;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        View view;
            view = inflater.inflate(R.layout.item_lost_rv, parent, false);
            viewHolder = new ItemHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_ITEM) {
            Lost item = dataSet.get(position);
            /*
            在此将item中的数据与viewholder中的view 绑定
             */
        } else {
            /*
             Do nothing
             */
        }

    }

    @Override
    public int getItemCount() {
        int itemCount = dataSet.size();
        return itemCount;
    }

    public void refreshItems(ArrayList<Lost> items) {
        dataSet.clear();
        dataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<Lost> items) {
        dataSet.addAll(items);
        notifyDataSetChanged();
    }

    public Lost getItem(int position) {
        return dataSet.get(position);
    }

}
