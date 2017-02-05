package com.twtstudio.retrox.bike.read.home.recommend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.BaseAdapter;
import com.twtstudio.retrox.bike.common.ui.BaseViewHolder;
import com.twtstudio.retrox.bike.model.read.Recommended;
import com.twtstudio.retrox.bike.read.bookdetail.BookDetailActivity;

import butterknife.BindView;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookRecommendAdapter extends BaseAdapter<Recommended> {

    static class BookRecommendHolder extends BaseViewHolder {

        @BindView(R2.id.iv_book)
        ImageView mIvBook;
        @BindView(R2.id.tv_title)
        TextView mTvTitle;
        @BindView(R2.id.tv_author)
        TextView mTvAuthor;
        @BindView(R2.id.ll_layout)
        LinearLayout mLayout;

        public BookRecommendHolder(View itemView) {
            super(itemView);
        }
    }

    public BookRecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new BookRecommendHolder(inflater.inflate(R.layout.item_book_recommend, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BookRecommendHolder recommendHolder = (BookRecommendHolder)holder;
        Glide.with(mContext).load(mDataSet.get(position).cover_url).into(recommendHolder.mIvBook);
        recommendHolder.mTvTitle.setText(mDataSet.get(position).title);
        recommendHolder.mTvAuthor.setText(mDataSet.get(position).author + " 著");
        recommendHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataSet.get(position).id != null) {
                    Intent intent = new Intent(mContext, BookDetailActivity.class);
                    intent.putExtra("id",mDataSet.get(position).id);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
