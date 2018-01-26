package com.twtstudio.retrox.bike.read.search;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.common.ui.BaseBindHolder;
import com.twtstudio.retrox.bike.common.ui.BaseBindingAdapter;
import com.twtstudio.retrox.bike.model.read.SearchBook;
import com.twtstudio.retrox.bike.read.bookdetail.BookDetailActivity;

import java.util.List;

/**
 * Created by jcy on 16-10-24.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookSearchAdapter extends BaseBindingAdapter<SearchBook> {
    private LifecycleOwner mOwner;
//    static class BookHolder extends BaseBindHolder {
//        private ItemBookSearchBinding mBinding;
//
//        public BookHolder(View itemView) {
//            super(itemView);
//            mBinding = DataBindingUtil.bind(itemView);
//        }
//
//        public void bind(SearchBook book){
//            mBinding.setBook(book);
//        }
//
//        public ItemBookSearchBinding getBinding() {
//            return mBinding;
//        }
//    }

    public BookSearchAdapter(Context context) {
        super(context);
        mOwner=(BookSearchActivity) context;
    }

    public BookSearchAdapter(Context context, List<SearchBook> bookList) {
        super(context);
        addItems(bookList);
        mOwner=(BookSearchActivity) context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_book_search,parent,false);
        return new BookSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookSearchViewHolder itemHolder = (BookSearchViewHolder) holder;
        SearchBook book = mDataSet.get(position);
        itemHolder.bind(mOwner,book);
        itemHolder.getLlSearchParent()
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
