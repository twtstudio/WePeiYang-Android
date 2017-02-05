package com.twtstudio.retrox.bike.read.home.profile;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.PFragment;
import com.twtstudio.retrox.bike.model.read.BookInShelf;
import com.twtstudio.retrox.bike.model.read.RefreshEvent;
import com.twtstudio.retrox.bike.model.read.Review;
import com.twtstudio.retrox.bike.read.home.BookReviewAdapter;
import com.twtstudio.retrox.bike.read.home.BookReviewAdapterInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookProfileFragment extends PFragment<BookProfilePresenter> implements BookProfileController,BookReviewAdapterInterface {

    @BindView(R2.id.pb_)
    ProgressBar mProgressBar;
    @BindView(R2.id.ll_main)
    LinearLayout mLinearLayout;
    @BindView(R2.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R2.id.rv_collect)
    RecyclerView mRvCollect;
    @BindView(R2.id.rv_review)
    RecyclerView mRvReview;

    private BookReviewAdapter mReviewAdapter;
    private BookShelfAdapter mShelfAdapter;
    private boolean isDeleteMode = false;
    private Unbinder mUnbinder;
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
        mPresenter.getMyReview();
    }

    @Override
    protected BookProfilePresenter getPresenter() {
        return new BookProfilePresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    public void onEvent(RefreshEvent event){
        mPresenter.getBookShelf();
        mPresenter.getMyReview();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void addLike(String id) {
        mPresenter.addLike(id);
    }

    @Override
    public void delLike(String id) {
        mPresenter.delLike(id);
    }

    @OnClick({R2.id.iv_delete})
    void onClick(View view){
        switch (view.getId()){
            case R2.id.iv_delete:
                if (!isDeleteMode){
                    Glide.with(this).load(R.mipmap.ic_book_deleting).into(mIvDelete);
                    isDeleteMode = true;
                    mShelfAdapter.setDeleteMode(isDeleteMode);
                }else {
                    if (mShelfAdapter.getDeleteArray().length == 0){
                        Glide.with(this).load(R.mipmap.ic_book_delete).into(mIvDelete);
                        isDeleteMode = false;
                        mShelfAdapter.setDeleteMode(isDeleteMode);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("是否要删除选中的书目？");
                        builder.setPositiveButton("删除", (dialog, which) -> {
                            mPresenter.delBookShelf(mShelfAdapter.getDeleteArray());
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
        mShelfAdapter.refreshItems(booksInShelf);
    }

    @Override
    public void delBookInShelfSuccess() {
        Glide.with(this).load(R.mipmap.ic_book_delete).into(mIvDelete);
        isDeleteMode = false;
        mShelfAdapter.setDeleteMode(isDeleteMode);
        mPresenter.getBookShelf();
    }

    @Override
    public void bindReviewData(List<Review> reviews) {
        mProgressBar.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.VISIBLE);
        mReviewAdapter.refreshItems(reviews);
    }
}
