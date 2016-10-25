package com.twt.service.rxsrc.read.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twt.service.R;
import com.twt.service.databinding.ItemBookSearchBinding;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseBindHolder;
import com.twt.service.rxsrc.common.ui.BaseBindingAdapter;
import com.twt.service.rxsrc.model.read.SearchBook;

import java.util.List;

/**
 * Created by jcy on 16-10-24.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookSearchAdapter extends BaseBindingAdapter<SearchBook> {

    static class BookHolder extends BaseBindHolder{
        private ItemBookSearchBinding mBinding;

        public BookHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(SearchBook book){
            mBinding.setBook(book);
        }

        public ItemBookSearchBinding getBinding() {
            return mBinding;
        }
    }

    public BookSearchAdapter(Context context) {
        super(context);
    }

    public BookSearchAdapter(Context context, List<SearchBook> bookList) {
        super(context);
        addItems(bookList);
    }


    @Override
    public BaseBindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_book_search,parent,false);
        return new BookHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BaseBindHolder holder, int position) {
        BookHolder itemHolder = (BookHolder) holder;
        itemHolder.bind(mDataSet.get(position));
    }

}
