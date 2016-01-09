package com.twt.service.ui.lostfound.found;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.LostFound;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by RexSun on 15/8/19.
 */
public class FoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<LostFound> dataSet = new ArrayList<>();

    public FoundAdapter(Context context) {
        this.context = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.sdv_item_found)
        ImageView sdvPhoto;
        @InjectView(R.id.tv_found_item_date)
        TextView tvDate;
        @InjectView(R.id.tv_found_item_time)
        TextView tvTime;
        @InjectView(R.id.tv_found_item_position)
        TextView tvPosition;
        @InjectView(R.id.tv_found_item_name)
        TextView tvName;
        @InjectView(R.id.tv_found_item_number)
        TextView tvNumber;
        @InjectView(R.id.tv_found_item_detail)
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
        view = inflater.inflate(R.layout.item_found_rv, parent, false);
        viewHolder = new ItemHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        LostFound item = dataSet.get(position);
        /*
        Bind Item
         */
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void refreshItems(ArrayList<LostFound> items) {
        dataSet.clear();
        dataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<LostFound> items) {
        dataSet.addAll(getItemCount(), items);
        notifyDataSetChanged();
    }

    public LostFound getItem(int position) {
        return dataSet.get(position);
    }
}
