package com.twtstudio.tjwhm.lostfound.waterfall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;

import java.util.ArrayList;

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
        makeSomeFakeData();
    }

    public class WaterfallViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.waterfall_item_pic)
        ImageView waterfall_item_pic;
        @BindView(R.id.waterfall_item_title)
        TextView waterfall_item_title;

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

    }

    @Override
    public int getItemCount() {
        if (waterfallBean == null || waterfallBean.data == null) {
            return 0;
        }
        return waterfallBean.data.size();
    }

    private void makeSomeFakeData() {
        waterfallBean = new WaterfallBean();
        waterfallBean.data = new ArrayList<>();
        WaterfallBean.DataBean d1 = new WaterfallBean.DataBean();
        d1.title = "塑料、透明水杯";
        d1.name = "水杯";
        d1.phone = 170703;
        d1.place = "大学生活动中心一楼";
        waterfallBean.data.add(d1);
        waterfallBean.data.add(d1);
    }
}
