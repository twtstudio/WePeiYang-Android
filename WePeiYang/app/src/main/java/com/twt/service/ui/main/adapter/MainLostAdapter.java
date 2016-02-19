package com.twt.service.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.Main;
import com.twt.service.ui.common.LostType;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 16/2/17.
 */
public class MainLostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Main.Data.Service.MainLost> dataSet = new ArrayList<>();
    private static final int ITEM_TYPE_ITEM = 0;
    private static final int ITEM_TYPE_FOOTER = 1;
    private Context context;

    public MainLostAdapter(Context context) {
        this.context = context;
    }

    public class ItemHoder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_main_lost_type)
        ImageView ivMainLostType;
        @InjectView(R.id.tv_main_lost_name)
        TextView tvMainLostName;

        public ItemHoder(View itemView) {
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
            return new ItemHoder(inflater.inflate(R.layout.item_main_lost_found, parent, false));
        } else {
            return new FooterHolder(inflater.inflate(R.layout.footer_main_lost_found, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_ITEM) {
            ItemHoder itemHoder = (ItemHoder) holder;
            Main.Data.Service.MainLost item = dataSet.get(position);
            switch (item.lost_type) {
                case LostType.OTHERS:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_others);
                    break;
                case LostType.BANK_CARD:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_bank_card);
                    break;
                case LostType.ID_CARD:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_id_card);
                    break;
                case LostType.KEY:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_key);
                    break;
                case LostType.BACKPACK:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_backpack);
                    break;
                case LostType.COMPUTER_PAG:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_computer_pag);
                    break;
                case LostType.WATCH:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_watch);
                    break;
                case LostType.UDISK:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_udisk);
                    break;
                case LostType.CUP:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_cup);
                    break;
                case LostType.BOOK:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_books);
                    break;
                case LostType.MOBILE_PHONE:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_mobile_phone);
                    break;
                case LostType.WALLET:
                    itemHoder.ivMainLostType.setImageResource(R.mipmap.icon_wallet);
                    break;
            }
            itemHoder.tvMainLostName.setText(item.title);
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

    public void bindData(List<Main.Data.Service.MainLost> items) {
        dataSet.clear();
        for (Main.Data.Service.MainLost item : items) {
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }
}
