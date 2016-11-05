package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;
import android.content.Intent;
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
import com.twt.service.rxsrc.read.bookreview.AddReviewActivity;

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
    private AdapterController mActController;

    private List<Object> mDataList = new ArrayList<>();

    public BookDetailAdapter(Context context) {
        mContext = context;
        BookDetailActivity activity = (BookDetailActivity) mContext;
        mActController = activity;
    }

    public BookDetailAdapter(Context context, Detail detail) {
        mContext = context;
        setDetail(detail);
        BookDetailActivity activity = (BookDetailActivity) mContext;
        mActController = activity;
    }

    public void setDetail(Detail detail) {
        mDetail = detail;
        mDataList.add(detail);
        //多加的一个bean表示总括信息，另一个来添加margin
        status_count = detail.holding.data.size() + 2;
        review_count = detail.review.data.size();
        Detail.HoldingBean.DataBean dataBean = new Detail.HoldingBean.DataBean();
        dataBean.callno = "索书号";
        dataBean.local = "地点";
        dataBean.state = "状态";
        //detail.holding.data.add(dataBean);
        mDataList.add(dataBean);
        if (detail.holding.data.size() == 0) {
            status_count++;
            Detail.HoldingBean.DataBean dataBean1 = new Detail.HoldingBean.DataBean();
            dataBean1.callno = "无在馆信息";
            mDataList.add(dataBean1);
        }
        mDataList.addAll(detail.holding.data);
        Detail.HoldingBean.DataBean emptyBean = new Detail.HoldingBean.DataBean();
        mDataList.add(emptyBean);
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
        private ImageView likeImage;

        public sReviewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            likeImage = mBinding.itemBookDetailIvLike;
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
            Glide.with(mContext).load(detail.cover_url)
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.default_cover)
                    .listener(GlidePalette.with(detail.cover_url)
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
            binding.bookDetailBtnAddreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddReviewActivity.class);
                    intent.putExtra("id",detail.id);
                    mContext.startActivity(intent);
                }
            });
            binding.bookDetailBtnLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActController.onBookLike(detail.id);
                }
            });
        } else if (type == TYPE_STATUS) {
            sStatusHolder statusHolder = (sStatusHolder) holder;
            statusHolder.mBinding.setStatus((Detail.HoldingBean.DataBean) baseData);
            //statusHolder.setStatus((Detail.statusItem) baseData);
        } else if (type == TYPE_REVIEW) {
            sReviewHolder reviewHolder = (sReviewHolder) holder;
            Detail.ReviewBean.DataBean data = (Detail.ReviewBean.DataBean) baseData;
            reviewHolder.setReview(data);
            if (data.liked) {
                reviewHolder.likeImage.setImageResource(R.mipmap.ic_book_like);
                reviewHolder.likeImage.setClickable(false);
            }
            reviewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reviewHolder.likeImage.setImageResource(R.mipmap.ic_book_like);
                    // TODO: 16-10-31 bookid??????这不科学
                    mActController.onReviewLike(data.review_id);
                    int x = Integer.parseInt(data.like_count);
                    x++;
                    reviewHolder.mBinding.tvLike.setText(String.valueOf(x));
                    v.setClickable(false);

                }
            });
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
