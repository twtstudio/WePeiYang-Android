package com.twt.service.ui.news.comments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/21.
 */
public class NewsCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Comment> dataSet = new ArrayList<>();
    private Context context;

    public NewsCommentsAdapter(Context context) {
        this.context = context;
    }


    static class ItemHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.tv_comment_uname)
        TextView tvCommentUname;
        @InjectView(R.id.tv_comment_date)
        TextView tvCommentDate;
        @InjectView(R.id.tv_comment_content)
        TextView tvCommentContent;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ItemHolder(inflater.inflate(R.layout.item_news_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        Comment item = dataSet.get(position);
        itemHolder.tvCommentUname.setText(item.cuser);
        itemHolder.tvCommentDate.setText(item.ctime);
        itemHolder.tvCommentContent.setText(item.ccontent);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void loadItems(List<Comment> comments) {
        dataSet.clear();
        dataSet.addAll(comments);
        notifyDataSetChanged();
    }

    public void addItems(List<Comment> comments) {
        for (Comment comment : comments) {
            dataSet.add(comment);
        }
        notifyDataSetChanged();
    }
}
