package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.twt.service.R;
import com.twt.service.databinding.ItemBookDetailHeaderBinding;
import com.twt.service.databinding.ItemBookDetailReviewBinding;
import com.twt.service.databinding.ItemBookDetailStatusBinding;
import com.twt.service.rxsrc.common.ui.BaseBindHolder;
import com.twt.service.rxsrc.model.read.Detail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcy on 16-10-26.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailAdapter extends RecyclerView.Adapter<BaseBindHolder> {
    private Context mContext;
    private Detail mDetail;
    private int status_count;
    private int review_count;
    private int TYPE_HEADER = 0;
    private int TYPE_STATUS = 1;
    private int TYPE_REVIEW = 2;

    private List<Object> mDataList = new ArrayList<>();

    public BookDetailAdapter(Context context) {
        mContext = context;
    }

    public BookDetailAdapter(Context context, Detail detail) {
        mContext = context;
        setDetail(detail);
    }

    public void setDetail(Detail detail) {
        mDetail = detail;
        status_count = detail.status.size();
        review_count = detail.reviews.size();
        mDataList.add(detail);
        mDataList.addAll(detail.status);
        mDataList.addAll(detail.reviews);
    }

    static class sHeaderHolder extends BaseBindHolder {
        private ItemBookDetailHeaderBinding mBinding;
        private FrameLayout mFrame;
        private ImageView mCoverImage;

        public sHeaderHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mFrame = mBinding.itemBookDetailHeaderFrame;
            mCoverImage = mBinding.itemBookDetailHeaderCover;
        }

        public ItemBookDetailHeaderBinding getBinding() {
            return mBinding;
        }
    }

    static class sStatusHolder extends BaseBindHolder {

        private ItemBookDetailStatusBinding mBinding;

        public sStatusHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setStatus(Detail.statusItem status) {
            mBinding.setStatus(status);
        }

        public ItemBookDetailStatusBinding getBinding() {
            return mBinding;
        }
    }


    static class sReviewHolder extends BaseBindHolder {

        private ItemBookDetailReviewBinding mBinding;

        public sReviewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setReview(Detail.reviewItem review) {
            mBinding.setReview(review);
        }

        public ItemBookDetailReviewBinding getBinding() {
            return mBinding;
        }
    }

    @Override
    public BaseBindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_header,parent,false);
            return new sHeaderHolder(view);
        }else if (viewType == TYPE_STATUS){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_status,parent,false);
            return new sStatusHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_review,parent,false);
            return new sReviewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseBindHolder holder, int position) {
        int type = getItemViewType(position);
        Object baseData = mDataList.get(position);
        if (type == TYPE_HEADER){
            sHeaderHolder headerHolder = (sHeaderHolder) holder;
            // TODO: 16-10-27 header样式的处理
        }else if (type == TYPE_REVIEW){
            sStatusHolder statusHolder = (sStatusHolder) holder;
            statusHolder.setStatus((Detail.statusItem) baseData);
        }else if (type == TYPE_REVIEW){
            sReviewHolder reviewHolder = (sReviewHolder) holder;
            reviewHolder.setReview((Detail.reviewItem) baseData);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position <= status_count) {
            return TYPE_STATUS;
        } else {
            return TYPE_REVIEW;
        }
    }
}
