package com.twt.service.ui.lostfound.found;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twt.service.R;
import com.twt.service.bean.FoundItem;
import com.twt.service.ui.lostfound.found.details.FoundDetailsActivity;
import com.twt.service.ui.lostfound.post.ObjectType;
import com.twt.service.ui.lostfound.post.PostLostFoundActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by RexSun on 15/8/19.
 */
public class FoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int adapterType;
    private ArrayList<FoundItem> dataSet = new ArrayList<>();
    public static final int TYPE_ALL_FOUND = 1;
    public static final int TYPE_MY_FOUND = 2;

    public FoundAdapter(Context context, int adapterType) {
        this.context = context;
        this.adapterType = adapterType;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.sdv_item_found)
        ImageView sdvPhoto;
        @InjectView(R.id.tv_found_item_date)
        TextView tvDate;
        @InjectView(R.id.tv_found_item_position)
        TextView tvPosition;
        @InjectView(R.id.tv_found_item_name)
        TextView tvName;
        @InjectView(R.id.tv_found_item_number)
        TextView tvNumber;
        @InjectView(R.id.tv_found_item_title)
        TextView tvTitle;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        FoundItem item = dataSet.get(position);
        if (item.found_pic.isEmpty()) {
            itemHolder.sdvPhoto.setImageResource(R.mipmap.icon_others);
        } else {
            Picasso.with(context).load(item.found_pic).into(itemHolder.sdvPhoto);
        }
        itemHolder.tvDate.setText(item.time);
        itemHolder.tvPosition.setText(item.place);
        itemHolder.tvName.setText(item.name);
        itemHolder.tvNumber.setText(item.phone);
        itemHolder.tvTitle.setText(item.title);
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (adapterType) {
                    case TYPE_ALL_FOUND:
                        FoundDetailsActivity.actionStart(context, dataSet.get(position).id);
                        break;
                    case TYPE_MY_FOUND:
                        PostLostFoundActivity.actionStart(context, dataSet.get(position).id, ObjectType.FOUND);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void refreshItems(List<FoundItem> items) {
        dataSet.clear();
        for (FoundItem item : items) {
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }

    public void loadMoreItems(List<FoundItem> items) {
        for (FoundItem item : items) {
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }
}
