package com.twtstudio.tjwhm.lostfound.waterfall;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.R2;
import com.twtstudio.tjwhm.lostfound.support.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm on 2017/7/12.
 **/

public class WaterfallTypeTableAdapter extends RecyclerView.Adapter {
    WaterfallActivity waterfallActivity;
    Context context;
    int selectedItem = -1;

    public WaterfallTypeTableAdapter(WaterfallActivity waterfallActivity, Context context, int selectedItem) {
        this.waterfallActivity = waterfallActivity;
        this.context = context;
        this.selectedItem = selectedItem;
    }

    public class WaterfallTypesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.waterfall_type_item)
        TextView waterfall_type_item;

        public WaterfallTypesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.lf_item_waterfall_type, parent, false);
        return new WaterfallTypesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WaterfallTypesViewHolder viewHolder = (WaterfallTypesViewHolder) holder;
        viewHolder.waterfall_type_item.setText(Utils.getType(position + 1));
        viewHolder.waterfall_type_item.setTypeface(Typeface.DEFAULT);
        if (position == selectedItem - 1) {
            viewHolder.waterfall_type_item.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        }
        viewHolder.itemView.setOnClickListener(view -> waterfallActivity.setWaterfallType(position + 1));
    }

    @Override
    public int getItemCount() {
        return 13;
    }
}
