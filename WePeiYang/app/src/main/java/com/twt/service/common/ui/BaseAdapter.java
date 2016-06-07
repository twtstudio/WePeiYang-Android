package com.twt.service.common.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjuntao on 16/6/6.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> mDataSet = new ArrayList<>();
    protected Context mContext;

    public BaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size() + 1;
    }

    public void refreshItems(List<T> items) {
        mDataSet.clear();
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }
}
