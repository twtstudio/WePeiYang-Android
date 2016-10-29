package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.twt.service.R;
import com.twt.service.databinding.ItemBookDetailHeaderBinding;
import com.twt.service.databinding.ItemBookDetailReviewBinding;
import com.twt.service.databinding.ItemBookDetailStatusBinding;
import com.twt.service.rxsrc.common.ui.BaseBindHolder;
import com.twt.service.rxsrc.model.read.Detail;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.Y;

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
        //status_count = detail.status.size();
        review_count = detail.review.data.size();
        mDataList.add(detail);
        //mDataList.addAll(detail.status);
        mDataList.addAll(detail.review.data);
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

//        public void setStatus(Detail. status) {
//            mBinding.setStatus(status);
//        }

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

        public void setReview(Detail.ReviewBean.DataBean review) {
            mBinding.setReview(review);
        }

        public ItemBookDetailReviewBinding getBinding() {
            return mBinding;
        }
    }

    @Override
    public BaseBindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_header, parent, false);
            return new sHeaderHolder(view);
        } else if (viewType == TYPE_STATUS) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_status, parent, false);
            return new sStatusHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_review, parent, false);
            return new sReviewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseBindHolder holder, int position) {
        int type = getItemViewType(position);
        Object baseData = mDataList.get(position);
        if (type == TYPE_HEADER) {
            sHeaderHolder headerHolder = (sHeaderHolder) holder;
            ItemBookDetailHeaderBinding binding = headerHolder.getBinding();
            Detail detail = (Detail) baseData;
            binding.setDetail(detail);
            Glide.with(mContext).load(detail.cover_url).listener(GlidePalette.with(detail.cover_url)
                    .use(GlidePalette.Profile.MUTED_LIGHT)
                    .intoBackground(headerHolder.mFrame)
                    .intoCallBack(new BitmapPalette.CallBack() {
                        @Override
                        public void onPaletteLoaded(@Nullable Palette palette) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                int color = palette.getLightMutedColor(0x000000);
                                AppCompatActivity activity = (AppCompatActivity) mContext;
                                activity.getWindow().setStatusBarColor(color);
                            }
                        }
                    }))
                    .into(headerHolder.mCoverImage);
            // TODO: 16-10-27 header样式的处理
        } else if (type == TYPE_STATUS) {
            sStatusHolder statusHolder = (sStatusHolder) holder;
            //statusHolder.setStatus((Detail.statusItem) baseData);
        } else if (type == TYPE_REVIEW) {
            sReviewHolder reviewHolder = (sReviewHolder) holder;
            reviewHolder.setReview((Detail.ReviewBean.DataBean) baseData);
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
