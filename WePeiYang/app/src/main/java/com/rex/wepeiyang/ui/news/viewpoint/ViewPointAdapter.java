package com.rex.wepeiyang.ui.news.viewpoint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.NewsItem;
import com.rex.wepeiyang.ui.news.details.NewsDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/18.
 */
public class ViewPointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private boolean useFooter = false;
    private ArrayList<NewsItem> dataSet = new ArrayList<>();
    private static final int ITEM_TYPE_ITEM = 0;
    private static final int ITEM_TYPE_FOOTER = 1;
    private Context context;

    public ViewPointAdapter(Context context) {
        this.context = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_news_title)
        TextView tvNewsTitle;
        @InjectView(R.id.tv_view_count)
        TextView tvViewCount;
        @InjectView(R.id.tv_news_date)
        TextView tvNewsDate;
        @InjectView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @InjectView(R.id.iv_news_picture)
        ImageView ivNewsPicture;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public static class FooterHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.pb_footer)
        ProgressBar pbFooter;

        public FooterHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_ITEM) {
            return new ItemHolder(inflater.inflate(R.layout.item_news_with_pic, parent, false));
        } else {
            return new FooterHolder(inflater.inflate(R.layout.footer, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_ITEM) {
            ItemHolder itemHolder = (ItemHolder) holder;
            final NewsItem item = dataSet.get(position);
            itemHolder.tvNewsTitle.setText(item.subject);
            itemHolder.tvCommentCount.setText(item.comments);
            itemHolder.tvViewCount.setText(item.visitcount);
            if (!item.pic.isEmpty()) {
                Picasso.with(context).load(item.pic).into(itemHolder.ivNewsPicture);
            } else {
                itemHolder.ivNewsPicture.setImageResource(R.mipmap.ic_launcher);
            }
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewsDetailsActivity.actionStart(context, item.index);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useFooter) {
            return ITEM_TYPE_ITEM;
        } else {
            if (position < getItemCount() - 1) {
                return ITEM_TYPE_ITEM;
            } else {
                return ITEM_TYPE_FOOTER;
            }
        }
    }

    public void setUseFooter(boolean useFooter) {
        this.useFooter = useFooter;
        notifyDataSetChanged();
    }

    public void refreshItems(List<NewsItem> items){
        dataSet.clear();
        dataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<NewsItem> items){
        for (NewsItem item : items){
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }
}
