package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;
import com.twt.service.rxsrc.model.read.User;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookStarAdapter extends BaseAdapter<User> {

    static class BookStarViewHolder extends BaseViewHolder{

        public BookStarViewHolder(View itemView) {
            super(itemView);
        }
    }
    public BookStarAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new BookStarViewHolder(inflater.inflate(R.layout.item_book_star, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }
}
