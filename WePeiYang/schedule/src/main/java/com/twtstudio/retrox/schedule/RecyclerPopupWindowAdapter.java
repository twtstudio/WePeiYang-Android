package com.twtstudio.retrox.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by cmj on 2016/3/29.
 */
public class RecyclerPopupWindowAdapter extends RecyclerView.Adapter<RecyclerPopupWindowAdapter.MyViewHolder> {


    /*RecyclerPopupWindowAdapter开始*/
    //数据项
    private List<WeekItem> items;
    //用户上次选择的时间
    private int prePosition;
    //未选中项
    private static final int TYPE_INACTIVE = 0;
    //选中项
    private static final int TYPE_ACTIVE = 1;

    public RecyclerPopupWindowAdapter(List<WeekItem> items) {
        super();
        prePosition = 0;
        this.items = items;
    }

    //设置不同样式
    @Override
    public int getItemViewType(int position) {
        WeekItem item = items.get(position);
        return item.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == TYPE_INACTIVE ? R.layout.schedule_item_inactive : R.layout.schedule_item_active;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MyViewHolder(itemView, onItemClickListener);
    }

    //设置值
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeekItem item = items.get(position);
        holder.getTimeTV().setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //点击事件监听器
    private OnItemClickListener onItemClickListener;

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /*RecyclerPopupWindowAdapter结束*/


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView timeTV;
        private OnItemClickListener mListener;

        public MyViewHolder(View rootView, OnItemClickListener listener) {
            super(rootView);
            this.mListener = listener;
            timeTV = (TextView) rootView.findViewById(R.id.tv_item_time);
            rootView.setOnClickListener(this);
        }

        /**
         * 点击监听
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(getAdapterPosition());
            }
        }

        public TextView getTimeTV() {
            return timeTV;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
