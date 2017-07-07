package com.twtstudio.tjwhm.lostfound.waterfall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.detail.DetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallTableAdapter extends RecyclerView.Adapter {

    WaterfallBean waterfallBean;
    Context context;

    public WaterfallTableAdapter(WaterfallBean waterfallBean, Context context) {
        this.waterfallBean = waterfallBean;
        this.context = context;
    }

    public class WaterfallViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.waterfall_item_pic)
        ImageView waterfall_item_pic;
        @BindView(R.id.waterfall_item_title)
        TextView waterfall_item_title;
        @BindView(R.id.waterfall_item_type)
        TextView waterfall_item_type;
        @BindView(R.id.waterfall_item_time)
        TextView waterfall_item_time;
        @BindView(R.id.waterfall_item_place)
        TextView waterfall_item_place;

        public WaterfallViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_waterfall, parent, false);

        return new WaterfallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        WaterfallBean.DataBean dataBean = waterfallBean.data.get(position);

        // TODO: 2017/7/5 现在是假图片,需换成真图片
        WaterfallViewHolder viewHolder = (WaterfallViewHolder) holder;
        if (position == 0) {
            viewHolder.waterfall_item_pic.setImageResource(R.drawable.waterfall_pic1);
        } else {
            viewHolder.waterfall_item_pic.setImageResource(R.drawable.waterfall_pic2);
        }
        viewHolder.waterfall_item_title.setText(dataBean.title);
        viewHolder.waterfall_item_type.setText(String.valueOf(dataBean.detail_type));
        viewHolder.waterfall_item_time.setText(dataBean.time);
        viewHolder.waterfall_item_place.setText(dataBean.place);

        viewHolder.itemView.setOnClickListener(view -> {
           startDetailActivity(dataBean.id);
        });
    }

    @Override
    public int getItemCount() {
        if (waterfallBean == null || waterfallBean.data == null) {
            return 0;
        }
        return waterfallBean.data.size();
    }

    private void startDetailActivity(int id){
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, DetailActivity.class);
        context.startActivity(intent);
    }
}
