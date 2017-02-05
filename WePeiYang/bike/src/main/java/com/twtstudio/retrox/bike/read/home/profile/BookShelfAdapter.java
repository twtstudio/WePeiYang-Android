package com.twtstudio.retrox.bike.read.home.profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.BaseAdapter;
import com.twtstudio.retrox.bike.common.ui.BaseViewHolder;
import com.twtstudio.retrox.bike.model.read.BookInShelf;
import com.twtstudio.retrox.bike.read.bookdetail.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookShelfAdapter extends BaseAdapter<BookInShelf> {


    List<String> deleteList;
    private boolean isDeleteMode = false;

    static class BookShelfHolder extends BaseViewHolder {

        @BindView(R2.id.cb_del)
        CheckBox mCbDel;
        @BindView(R2.id.tv_title)
        TextView mTvTitle;
        @BindView(R2.id.tv_author)
        TextView mTvAuthor;

        public BookShelfHolder(View itemView) {
            super(itemView);
        }
    }

    public BookShelfAdapter(Context context) {
        super(context);
        deleteList = new ArrayList<>();
    }

    public void refreshItems(List<BookInShelf> booksInShelf){
        mDataSet.clear();
        mDataSet.addAll(booksInShelf);
        notifyDataSetChanged();
    }
    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
        if (!deleteMode){
            deleteList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new BookShelfHolder(inflater.inflate(R.layout.item_book_collect, parent, false));
    }

    public String[] getDeleteArray(){
        String[] array =new String[deleteList.size()];
        System.arraycopy(deleteList.toArray(), 0, array, 0, deleteList.size());
        return array;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BookShelfHolder shelfHolder = (BookShelfHolder)holder;
        BookInShelf book = mDataSet.get(position);
        if (isDeleteMode){

            shelfHolder.mCbDel.setVisibility(View.VISIBLE);
            if (deleteList.contains(book.book_id)) {
                shelfHolder.mCbDel.setChecked(true);
            }
            shelfHolder.mCbDel.setOnClickListener(view -> {
                if (!shelfHolder.mCbDel.isChecked()){
                    deleteList.remove(book.book_id);
                }else {
                    deleteList.add(book.book_id);
                }
            });
        }else {
            shelfHolder.mCbDel.setVisibility(View.INVISIBLE);

            shelfHolder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("id",book.book_id);
                mContext.startActivity(intent);
            });

        }

        if (book.author != null) {
            shelfHolder.mTvAuthor.setText(book.author);
        }
        if (book.title != null) {
            shelfHolder.mTvTitle.setText(book.title);
        }

    }
}
