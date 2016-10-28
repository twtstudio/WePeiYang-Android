package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;
import com.twt.service.rxsrc.model.read.Recommended;

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

    }
}
