package com.twt.service.rxsrc.read.home.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseFragment;
import com.twt.service.rxsrc.common.ui.PFragment;
import com.twt.service.rxsrc.model.read.BookInShelf;
import com.twt.service.rxsrc.model.read.Review;
import com.twt.service.rxsrc.read.home.BookReviewAdapter;
import com.twt.service.rxsrc.read.home.BookReviewAdapterInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.R.attr.id;
import static android.R.attr.toDegrees;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookProfileFragment extends PFragment<BookProfilePresenter> implements BookProfileController,BookReviewAdapterInterface {
    @InjectView(R.id.iv_delete)
    ImageView mIvDelete;
    @InjectView(R.id.rv_collect)
    RecyclerView mRvCollect;
    @InjectView(R.id.rv_review)
    RecyclerView mRvReview;

    private BookReviewAdapter mReviewAdapter;
    private BookShelfAdapter mShelfAdapter;
    private boolean isDeleteMode = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_book_profile;
    }

    @Override
    protected void initView() {

        mShelfAdapter = new BookShelfAdapter(getContext());
        mShelfAdapter.hideFooter();
        mRvCollect.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCollect.setAdapter(mShelfAdapter);
        mPresenter.getBookShelf();

        mReviewAdapter = new BookReviewAdapter(getContext(),this);
        mReviewAdapter.hideFooter();
        mRvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvReview.setAdapter(mReviewAdapter);


        // TODO: 2016/10/28 测试代码


//        List<BookInShelf> books = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            books.add(new BookInShelf());
//        }
//        mShelfAdapter.addItems(books);


        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            reviews.add(new Review());
        }
        mReviewAdapter.addItems(reviews);
    }

    @Override
    protected BookProfilePresenter getPresenter() {
        return new BookProfilePresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void addLike(String id) {

    }

    @Override
    public void delLike(String id) {

    }

    @OnClick({R.id.iv_delete})
    void onClick(View view){
        switch (view.getId()){
            case R.id.iv_delete:
                if (!isDeleteMode){
                    Glide.with(this).load(R.mipmap.ic_book_deleting).into(mIvDelete);
                    isDeleteMode = true;
                    mShelfAdapter.setDeleteMode(isDeleteMode);
                }else {
                    if (mShelfAdapter.getDeleteList().length == 0){
                        Glide.with(this).load(R.mipmap.ic_book_delete).into(mIvDelete);
                        isDeleteMode = false;
                        mShelfAdapter.setDeleteMode(isDeleteMode);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("是否要删除选中的书目？");
                        builder.setPositiveButton("删除", (dialog, which) -> {
                            // TODO: 2016/11/2 删除条目
                            mPresenter.delBookShelf(mShelfAdapter.getDeleteList());
                            dialog.dismiss();
                        });
                        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                        builder.create().show();
                    }

                }

                break;
        }

    }

    @Override
    public void bindBookShelfData(List<BookInShelf> booksInShelf) {
        mShelfAdapter.addItems(booksInShelf);
    }

    @Override
    public void delBookInShelfSuccess() {
        Glide.with(this).load(R.mipmap.ic_book_delete).into(mIvDelete);
        isDeleteMode = false;
        mShelfAdapter.setDeleteMode(isDeleteMode);
    }
}
