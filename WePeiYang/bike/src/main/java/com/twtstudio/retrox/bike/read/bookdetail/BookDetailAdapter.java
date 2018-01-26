package com.twtstudio.retrox.bike.read.bookdetail;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.model.read.Detail;
import com.twtstudio.retrox.bike.read.bookreview.AddReviewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcy on 16-10-26.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private Detail mDetail;
    private int status_count;
    private int review_count;
    private int TYPE_HEADER = 0;
    private int TYPE_STATUS = 1;
    private int TYPE_REVIEW = 2;
    private AdapterController mActController;
    private LifecycleOwner mOwner;

    private List<Object> mDataList = new ArrayList<>();

    public BookDetailAdapter(Context context) {
        mContext = context;
        BookDetailActivity activity = (BookDetailActivity) mContext;
        mActController = activity;
        mOwner=activity;
    }

    public BookDetailAdapter(Context context, Detail detail) {
        mContext = context;
        setDetail(detail);
        BookDetailActivity activity = (BookDetailActivity) mContext;
        mActController = activity;
        mOwner=activity;
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

//    static class sHeaderHolder extends BaseBindHolder {
//        private ItemBookDetailHeaderBinding mBinding;
//        private FrameLayout mFrame;
//        private ImageView mCoverImage;
//
//        public sHeaderHolder(View itemView) {
//            super(itemView);
//            mBinding = DataBindingUtil.bind(itemView);
//            mFrame = mBinding.itemBookDetailHeaderFrame;
//            mCoverImage = mBinding.itemBookDetailHeaderCover;
//        }
//
//        public ItemBookDetailHeaderBinding getBinding() {
//            return mBinding;
//        }
//    }
//
//    static class sStatusHolder extends BaseBindHolder {
//
//        private ItemBookDetailStatusBinding mBinding;
//
//        public sStatusHolder(View itemView) {
//            super(itemView);
//            mBinding = DataBindingUtil.bind(itemView);
//        }
//
////        public void setStatus(Detail. status) {
////            mBinding.setStatus(status);
////        }
//
//        public ItemBookDetailStatusBinding getBinding() {
//            return mBinding;
//        }
//    }
//
//
//    static class sReviewHolder extends BaseBindHolder {
//
//        private ItemBookDetailReviewBinding mBinding;
//        private ImageView likeImage;
//
//        public sReviewHolder(View itemView) {
//            super(itemView);
//            mBinding = DataBindingUtil.bind(itemView);
//            likeImage = mBinding.itemBookDetailIvLike;
//        }
//
//        public void setReview(Detail.ReviewBean.DataBean review) {
//            mBinding.setReview(review);
//        }
//
//        public ItemBookDetailReviewBinding getBinding() {
//            return mBinding;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_STATUS) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_status, parent, false);
            return new StatusViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_book_detail_review, parent, false);
            return new ReviewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        Object baseData = mDataList.get(position);
        if (type == TYPE_HEADER) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            Detail detail = (Detail) baseData;
            headerHolder.bind(mOwner,detail);
            Glide.with(mContext).load(detail.cover_url)
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.default_cover)
                    .listener(GlidePalette.with(detail.cover_url)
                            .use(GlidePalette.Profile.MUTED_LIGHT)
                            .intoBackground(headerHolder.getMFrame())
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
                    .into(headerHolder.getMCoverImage());
            headerHolder.getBtAddReview().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddReviewActivity.class);
                    intent.putExtra("id",detail.id);
                    mContext.startActivity(intent);
                }
            });
            headerHolder.getBtLove().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActController.onBookLike(detail.id);
                }
            });
        } else if (type == TYPE_STATUS) {
            StatusViewHolder statusHolder = (StatusViewHolder) holder;
            statusHolder.bind(mOwner,(Detail.HoldingBean.DataBean) baseData);
            //statusHolder.setStatus((Detail.statusItem) baseData);
        } else if (type == TYPE_REVIEW) {
            ReviewViewHolder reviewHolder = (ReviewViewHolder) holder;
            Detail.ReviewBean.DataBean data = (Detail.ReviewBean.DataBean) baseData;
            reviewHolder.bind(mOwner,data);
            if (data.liked) {
                reviewHolder.getIvLike().setImageResource(R.mipmap.ic_book_like);
                reviewHolder.getIvLike().setClickable(false);
            }else {
                reviewHolder.getIvLike().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reviewHolder.getIvLike().setImageResource(R.mipmap.ic_book_like);
                        // TODO: 16-10-31 bookid??????这不科学
                        mActController.onReviewLike(data.review_id);
                        int x = Integer.parseInt(data.like_count);
                        x++;
                        reviewHolder.getTvLike().setText(String.valueOf(x));
                        v.setClickable(false);

                    }
                });
            }
            if (position == getItemCount()-1){
                reviewHolder.getVDivider().setVisibility(View.INVISIBLE);
            }

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
