package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.twt.service.databinding.ItemBookDetailHeaderBinding;
import com.twt.service.databinding.ItemBookDetailReviewBinding;
import com.twt.service.databinding.ItemBookDetailStatusBinding;
import com.twt.service.rxsrc.common.ui.BaseBindHolder;
import com.twt.service.rxsrc.model.read.Detail;

/**
 * Created by jcy on 16-10-26.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailAdapter extends RecyclerView.Adapter<BaseBindHolder> {
    private Context mContext;

    public BookDetailAdapter(Context context) {
        mContext = context;
    }

    static class sHeaderHolder extends BaseBindHolder{
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

    static class sStatusHolder extends BaseBindHolder{

        private ItemBookDetailStatusBinding mBinding;

        public sStatusHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setStatus(Detail.statusItem status){
            mBinding.setStatus(status);
        }

        public ItemBookDetailStatusBinding getBinding() {
            return mBinding;
        }
    }


    static class sReviewHolder extends BaseBindHolder{

        private ItemBookDetailReviewBinding mBinding;

        public sReviewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void setReview(Detail.reviewItem review){
            mBinding.setReview(review);
        }

        public ItemBookDetailReviewBinding getBinding() {
            return mBinding;
        }
    }

    @Override
    public BaseBindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseBindHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
