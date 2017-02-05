package com.twtstudio.retrox.bike.common.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunjuntao on 16/6/6.
 */
public abstract class BaseBindingAdapter<T> extends RecyclerView.Adapter<BaseBindHolder> {
    protected List<T> mDataSet = new ArrayList<>();
    protected Context mContext;

    protected boolean isShowFooter = true;

    public void showFooter() {
        isShowFooter = true;
    }

    public void hideFooter() {
        isShowFooter = false;
    }

    public BaseBindingAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        if (isShowFooter) {
            return mDataSet.size() + 1;
        }
        return mDataSet.size();
    }

    public void refreshItems(List<T> items) {
        mDataSet.clear();
        mDataSet.addAll(items);
        hideFooter();
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }
}
