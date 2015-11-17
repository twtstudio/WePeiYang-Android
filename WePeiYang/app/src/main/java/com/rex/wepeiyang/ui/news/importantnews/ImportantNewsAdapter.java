package com.rex.wepeiyang.ui.news.importantnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.bean.NewsItem;
import com.rex.wepeiyang.ui.news.details.NewsDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/14.
 */
public class ImportantNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private static final int ITEM_VIEW_TYPE_FOOTER = 2;


    private ArrayList<NewsItem> dataSet = new ArrayList<>();
    private boolean useFooter = false;
    private Context context;

    public ImportantNewsAdapter(Context context) {
        this.context = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_news_title)
        TextView tvNewsTitle;
        @InjectView(R.id.tv_view_count)
        TextView tvViewCount;
        @InjectView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @InjectView(R.id.tv_news_date)
        TextView tvNewsDate;
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

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.important_news_slider)
        SliderLayout importantNewsSlider;
        @InjectView(R.id.important_news_indicator)
        PagerIndicator importantNewsIndicator;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new HeaderHolder(inflater.inflate(R.layout.header_important_news, parent, false));
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return new FooterHolder(inflater.inflate(R.layout.footer, parent, false));
        } else {
            return new ItemHolder(inflater.inflate(R.layout.item_news, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_VIEW_TYPE_ITEM) {
            ItemHolder itemHolder = (ItemHolder) holder;
            final NewsItem newsItem = dataSet.get(position);
            itemHolder.tvNewsTitle.setText(newsItem.subject);
            itemHolder.tvViewCount.setText(newsItem.visitcount + "");
            itemHolder.tvCommentCount.setText(newsItem.comments + "");
            itemHolder.tvNewsDate.setText("没有");
            itemHolder.ivNewsPicture.setVisibility(View.VISIBLE);
            if (!newsItem.pic.isEmpty()) {
                Picasso.with(context).load(newsItem.pic).into(itemHolder.ivNewsPicture);
            } else {
                itemHolder.ivNewsPicture.setImageResource(R.mipmap.ic_launcher);
            }

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewsDetailsActivity.actionStart(context, newsItem.index);
                }
            });
        } else if (type == ITEM_VIEW_TYPE_HEADER) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
        }

    }

    @Override
    public int getItemCount() {
        int itemCount = dataSet.size();
        if (useFooter) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_VIEW_TYPE_HEADER;
        } else {
            if (!useFooter) {
                return ITEM_VIEW_TYPE_ITEM;
            } else if (position < getItemCount() - 1) {
                return ITEM_VIEW_TYPE_ITEM;
            } else {
                return ITEM_VIEW_TYPE_FOOTER;
            }
        }
    }

    public void refreshItems(List<NewsItem> items) {
        dataSet.clear();
        dataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<NewsItem> items) {
        useFooter = false;
        for (NewsItem item : items) {
            dataSet.add(item);
        }
        notifyDataSetChanged();
    }

    public void setUseFooter(boolean useFooter) {
        this.useFooter = useFooter;
        notifyDataSetChanged();
    }

}
