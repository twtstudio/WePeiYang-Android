package com.twtstudio.retrox.bike.read.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.common.ui.BaseBindHolder;
import com.twtstudio.retrox.bike.common.ui.BaseBindingAdapter;
import com.twtstudio.retrox.bike.databinding.ItemBookSearchBinding;
import com.twtstudio.retrox.bike.model.read.SearchBook;
import com.twtstudio.retrox.bike.read.bookdetail.BookDetailActivity;

import java.util.List;

/**
 * Created by jcy on 16-10-24.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookSearchAdapter extends BaseBindingAdapter<SearchBook> {

    static class BookHolder extends BaseBindHolder {
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
        SearchBook book = mDataSet.get(position);
        itemHolder.bind(book);
        itemHolder.getBinding().itemBookSearchParent
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BookDetailActivity.class);
                        intent.putExtra("id",book.index);
                        mContext.startActivity(intent);
                    }
                });
    }

}
