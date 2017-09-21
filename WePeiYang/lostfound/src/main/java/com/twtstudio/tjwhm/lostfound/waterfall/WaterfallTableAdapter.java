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

import com.bumptech.glide.Glide;
import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.detail.DetailActivity;
import com.twtstudio.tjwhm.lostfound.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/3.
 **/

public class WaterfallTableAdapter extends RecyclerView.Adapter {

    WaterfallBean waterfallBean;
    Context context;
    String lostOrFound;

    public WaterfallTableAdapter(WaterfallBean waterfallBean, Context context, String lostOrFound) {
        this.waterfallBean = waterfallBean;
        this.context = context;
        this.lostOrFound = lostOrFound;
    }

    public class WaterfallViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.waterfall_item_pic)
        ImageView waterfall_item_pic;
        @BindView(R2.id.waterfall_item_title)
        TextView waterfall_item_title;
        @BindView(R2.id.waterfall_item_type)
        TextView waterfall_item_type;
        @BindView(R2.id.waterfall_item_time)
        TextView waterfall_item_time;
        @BindView(R2.id.waterfall_item_place)
        TextView waterfall_item_place;

        public WaterfallViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.lf_item_waterfall, parent, false);

        return new WaterfallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        WaterfallBean.DataBean dataBean = waterfallBean.data.get(position);

        WaterfallViewHolder viewHolder = (WaterfallViewHolder) holder;
        Glide.with(context)
                .load(Utils.getPicUrl(dataBean.picture))
                .asBitmap()
                .placeholder(R.drawable.lf_waterfall_nopic)
                .into(viewHolder.waterfall_item_pic);
        viewHolder.waterfall_item_title.setText(dataBean.title);
        viewHolder.waterfall_item_type.setText(Utils.getType(dataBean.detail_type));
        viewHolder.waterfall_item_time.setText(dataBean.time);
        viewHolder.waterfall_item_place.setText(dataBean.place);

        viewHolder.itemView.setOnClickListener(view -> startDetailActivity(dataBean.id));
    }

    @Override
    public int getItemCount() {
        if (waterfallBean == null || waterfallBean.data == null) {
            return 0;
        }
        return waterfallBean.data.size();
    }

    private void startDetailActivity(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("lostOrFound", lostOrFound);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, DetailActivity.class);
        context.startActivity(intent);

    }
}
