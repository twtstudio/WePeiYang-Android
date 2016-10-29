package com.twt.service.rxsrc.read.home.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;
import com.twt.service.rxsrc.model.read.MyBookShelf;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookShelfAdapter extends BaseAdapter<MyBookShelf> {

    static class BookShelfHolder extends BaseViewHolder{

        public BookShelfHolder(View itemView) {
            super(itemView);
        }
    }
    public BookShelfAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new BookShelfHolder(inflater.inflate(R.layout.item_book_collect, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }
}
