package com.twt.service.network.view.spy;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.network.modle.StatusBean;
import com.twt.service.network.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

/**
 * Created by chen on 2017/7/10.
 */
//可以考虑不用adapter
public class SpyAdapter extends SwipeMenuRecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int normalType = 1;
    private List<StatusBean.data> mStatusList;
    private Context mContext;

    public SpyAdapter(Context context, List<StatusBean.data> dataList) {
        this.mContext = context;
        this.mStatusList = dataList;
    }
    private class NormalHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public NormalHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(ViewDataBinding binding1) {
            this.binding = binding1;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }
    }


    @Override
    public int getItemCount() {
        return mStatusList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return normalType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_spy_status_cv, parent, false);
        NormalHolder normalHolder = new NormalHolder(binding.getRoot());
        normalHolder.setBinding(binding);
        return normalHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalHolder){
            //实时更新数据
            ((NormalHolder) holder).getBinding().executePendingBindings();
        }
    }

}
