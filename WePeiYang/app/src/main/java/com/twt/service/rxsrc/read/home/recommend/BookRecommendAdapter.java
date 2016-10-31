package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;
import com.twt.service.rxsrc.model.read.Recommended;
import com.twt.service.rxsrc.read.bookdetail.BookDetailActivity;

import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookRecommendAdapter extends BaseAdapter<Recommended> {

    static class BookRecommendHolder extends BaseViewHolder {

        @InjectView(R.id.iv_book)
        ImageView mIvBook;
        @InjectView(R.id.iv_title)
        TextView mIvTitle;
        @InjectView(R.id.iv_author)
        TextView mIvAuthor;
        @InjectView(R.id.iv_layout)
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
        recommendHolder.mIvTitle.setText(mDataSet.get(position).title);
        recommendHolder.mIvAuthor.setText(mDataSet.get(position).author + " 著");
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
