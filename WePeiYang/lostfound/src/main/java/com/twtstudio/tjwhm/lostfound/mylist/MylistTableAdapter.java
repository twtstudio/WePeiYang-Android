package com.twtstudio.tjwhm.lostfound.mylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twtstudio.tjwhm.lostfound.R;

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

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
