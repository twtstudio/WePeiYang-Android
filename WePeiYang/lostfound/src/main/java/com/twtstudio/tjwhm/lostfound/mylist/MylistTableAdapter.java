package com.twtstudio.tjwhm.lostfound.mylist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.detail.DetailActivity;
import com.twtstudio.tjwhm.lostfound.support.IntToType;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/4.
 **/

public class MylistTableAdapter extends RecyclerView.Adapter {

    MylistBean mylistBean;
    Context context;

    public MylistTableAdapter(MylistBean mylistBean, Context context) {
        this.mylistBean = mylistBean;
        this.context = context;
    }

    public class MylistViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mylist_item_status)
        TextView mylist_item_status;
        @BindView(R.id.mylist_item_title)
        TextView mylist_item_title;
        @BindView(R.id.mylist_item_type)
        TextView mylist_item_type;
        @BindView(R.id.mylist_item_time)
        TextView mylist_item_time;
        @BindView(R.id.mylist_item_place)
        TextView mylist_item_place;

        public MylistViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_mylist, parent, false);
        return new MylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MylistViewHolder viewHolder = (MylistViewHolder) holder;
        WaterfallBean.DataBean dataBean = mylistBean.data.get(position);
        viewHolder.mylist_item_title.setText(dataBean.title);
        viewHolder.mylist_item_type.setText(IntToType.getType(dataBean.detail_type));
        viewHolder.mylist_item_time.setText(dataBean.time);
        viewHolder.mylist_item_place.setText(dataBean.place);
        if (dataBean.isback == 1) {
            viewHolder.mylist_item_status.setText("已交还");
        } else {
            viewHolder.mylist_item_status.setText("未交还");
        }
        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("id",dataBean.id);
            intent.putExtras(bundle);
            intent.setClass(context, DetailActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mylistBean.data.size();
    }
}
