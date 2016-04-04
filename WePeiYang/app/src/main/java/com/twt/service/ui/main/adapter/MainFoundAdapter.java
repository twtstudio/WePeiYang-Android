package com.twt.service.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twt.service.R;
import com.twt.service.bean.Main;
import com.twt.service.ui.lostfound.post.lost.event.LostFoundActivity;
import com.twt.service.ui.lostfound.found.details.FoundDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 16/2/17.
 */
public class MainFoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Main.Data.Service.MainFound> dataSet = new ArrayList<>();
    private int ITEM_TYPE_ITEM = 0;
    private int ITEM_TYPE_FOOTER = 1;
    private Context context;

    public MainFoundAdapter(Context context) {
        this.context = context;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_main_lost_type)
        ImageView ivMainLostType;
        @InjectView(R.id.tv_main_lost_name)
        TextView tvMainLostName;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_ITEM) {
            return new ItemHolder(inflater.inflate(R.layout.item_main_lost_found, parent, false));
        } else {
            return new FooterHolder(inflater.inflate(R.layout.footer_main_lost_found, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_ITEM) {
            ItemHolder itemHolder = (ItemHolder) holder;
            final Main.Data.Service.MainFound found = dataSet.get(position);
            if (!found.found_pic.isEmpty()) {
                Picasso.with(context).load(found.found_pic);
            } else {
                itemHolder.ivMainLostType.setImageResource(R.mipmap.icon_others);
            }
            itemHolder.tvMainLostName.setText(found.title);
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoundDetailsActivity.actionStart(context, found.id);
                }
            });
        }
        if (type == ITEM_TYPE_FOOTER) {
            FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LostFoundActivity.actionStart(context,1);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dataSet.size()) {
            return ITEM_TYPE_FOOTER;
        } else {
            return ITEM_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    public void bindData(List<Main.Data.Service.MainFound> items) {
        dataSet.clear();
        for (Main.Data.Service.MainFound item : items) {
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }
}
