package com.twt.service.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twt.service.R;
import com.twt.service.common.ui.BaseAdapter;
import com.twt.service.common.ui.BaseViewHolder;
import com.twt.service.model.NewsItem;

import butterknife.InjectView;

/**
 * Created by sunjuntao on 16/6/6.
 */
public class NewsAdapter extends BaseAdapter<NewsItem> {

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_FOOTER = 1;
    private boolean mHasPic;


    public NewsAdapter(Context context, boolean hasPic) {
        super(context);
        mHasPic = hasPic;
    }


    static class ItemHolderWithPic extends BaseViewHolder {

        @InjectView(R.id.tv_news_title)
        TextView mNewsTitle;
        @InjectView(R.id.tv_view_count)
        TextView mViewCount;
        @InjectView(R.id.tv_comment_count)
        TextView mCommentCount;
        @InjectView(R.id.iv_news_picture)
        ImageView mNewsPicture;

        public ItemHolderWithPic(View itemView) {
            super(itemView);
        }
    }

    static class ItemHolderWithoutPic extends BaseViewHolder {

        @InjectView(R.id.tv_news_title)
        TextView mNewsTitle;
        @InjectView(R.id.tv_view_count)
        TextView mViewCount;
        @InjectView(R.id.tv_comment_count)
        TextView mCommentCount;

        public ItemHolderWithoutPic(View itemView) {
            super(itemView);
        }
    }

    static class FooterHolder extends BaseViewHolder {

        @InjectView(R.id.pb_footer)
        ProgressBar mFooter;

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_FOOTER) {
            return new FooterHolder(inflater.inflate(R.layout.footer, parent, false));
        } else {
            if (mHasPic) {
                return new ItemHolderWithPic(inflater.inflate(R.layout.item_news_with_pic, parent, false));
            } else {
                return new ItemHolderWithoutPic(inflater.inflate(R.layout.item_news_without_pic, parent, false));
            }
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_NORMAL) {
            if (mHasPic) {
                ItemHolderWithPic itemholder = (ItemHolderWithPic) holder;
                NewsItem newsItem = mDataSet.get(position);
                itemholder.mNewsTitle.setText(newsItem.subject);
                itemholder.mViewCount.setText(newsItem.visitcount);
                itemholder.mCommentCount.setText(newsItem.comments);
                if (!newsItem.pic.isEmpty()) {
                    Picasso.with(mContext)
                            .load(newsItem.pic)
                            .into(itemholder.mNewsPicture);
                } else {
                    Picasso.with(mContext)
                            .load(R.mipmap.ic_login)
                            .resize(itemholder.mNewsPicture.getWidth(), itemholder.mNewsPicture.getHeight())
                            .into(itemholder.mNewsPicture);
                }
            } else {
                ItemHolderWithoutPic itemholder = (ItemHolderWithoutPic) holder;
                NewsItem newsItem = mDataSet.get(position);
                itemholder.mNewsTitle.setText(newsItem.subject);
                itemholder.mViewCount.setText(newsItem.subject);
                itemholder.mViewCount.setText(newsItem.visitcount);
                itemholder.mCommentCount.setText(newsItem.comments);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataSet.size()) {
            return ITEM_FOOTER;
        } else {
            return ITEM_NORMAL;
        }
    }
}
