package com.twtstudio.tjwhm.lostfound.mylist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.detail.DetailActivity;
import com.twtstudio.tjwhm.lostfound.release.ReleaseActivity;
import com.twtstudio.tjwhm.lostfound.support.Utils;
import com.twtstudio.tjwhm.lostfound.waterfall.WaterfallBean;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/4.
 **/

public class MylistTableAdapter extends RecyclerView.Adapter {

    private MylistBean mylistBean;
    private Context context;
    private String lostOrFound;
    private MylistContract.MylistView mylistView;

    public MylistTableAdapter(MylistBean mylistBean, Context context, String lostOrFound, MylistContract.MylistView mylistView) {
        this.mylistBean = mylistBean;
        this.context = context;
        this.lostOrFound = lostOrFound;
        this.mylistView = mylistView;
    }

    public class MylistViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.mylist_item_status)
        TextView mylist_item_status;
        @BindView(R2.id.mylist_item_title)
        TextView mylist_item_title;
        @BindView(R2.id.mylist_item_type)
        TextView mylist_item_type;
        @BindView(R2.id.mylist_item_time)
        TextView mylist_item_time;
        @BindView(R2.id.mylist_item_place)
        TextView mylist_item_place;
        @BindView(R2.id.mylist_item_back_blue)
        ImageView mylist_item_back_blue;
        @BindView(R2.id.mylist_item_back_grey)
        ImageView mylist_item_back_grey;
        @BindView(R2.id.mylist_item_pencil)
        ImageView mylist_item_pencil;
        @BindView(R2.id.mylist_item_pic)
        ImageView mylist_item_pic;
        @BindView(R2.id.mylist_status_linear)
        LinearLayout mylist_status_linear;
        @BindView(R2.id.mylist_item_pencil_touch)
        TextView mylist_item_pencil_touch;

        public MylistViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.lf_item_mylist, parent, false);
        return new MylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MylistViewHolder viewHolder = (MylistViewHolder) holder;
        WaterfallBean.DataBean dataBean = mylistBean.data.get(position);
        viewHolder.mylist_item_title.setText(dataBean.title);
        viewHolder.mylist_item_type.setText(Utils.getType(dataBean.detail_type));
        viewHolder.mylist_item_time.setText(dataBean.time);
        viewHolder.mylist_item_place.setText(dataBean.place);
        Glide.with(context)
                .load(Utils.getPicUrl(dataBean.picture))
                .asBitmap()
                .placeholder(R.drawable.lf_waterfall_nopic)
                .into(viewHolder.mylist_item_pic);

        if (dataBean.isback == 1) {
            viewHolder.mylist_item_back_blue.setVisibility(View.VISIBLE);
            viewHolder.mylist_item_back_grey.setVisibility(View.GONE);
            if (Objects.equals(lostOrFound, "found")) {
                viewHolder.mylist_item_status.setText("已交还");
            } else {
                viewHolder.mylist_item_status.setText("已找到");
            }
        } else {
            viewHolder.mylist_item_back_blue.setVisibility(View.GONE);
            viewHolder.mylist_item_back_grey.setVisibility(View.VISIBLE);
            if (Objects.equals(lostOrFound, "found")) {
                viewHolder.mylist_item_status.setText("未交还");
            } else {
                viewHolder.mylist_item_status.setText("未找到");
            }
        }
        viewHolder.mylist_status_linear.setOnClickListener(view -> mylistView.turnStatus(dataBean.id));

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        viewHolder.itemView.setOnClickListener(view -> {
            bundle.putInt("id", dataBean.id);
            intent.putExtras(bundle);
            intent.setClass(context, DetailActivity.class);
            context.startActivity(intent);
        });
        viewHolder.mylist_item_pencil_touch.setOnClickListener(view -> {
            if (Objects.equals(lostOrFound, "lost")) {
                bundle.putString("lostOrFound", "editLost");
            } else {
                bundle.putString("lostOrFound", "editFound");
            }
            bundle.putInt("id", dataBean.id);
            bundle.putInt("type", dataBean.detail_type);
            intent.putExtras(bundle);
            intent.setClass(context, ReleaseActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mylistBean.data.size();
    }
}
