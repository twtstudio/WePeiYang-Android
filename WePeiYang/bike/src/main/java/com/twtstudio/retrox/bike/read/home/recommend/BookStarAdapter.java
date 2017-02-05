package com.twtstudio.retrox.bike.read.home.recommend;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.BaseAdapter;
import com.twtstudio.retrox.bike.common.ui.BaseViewHolder;
import com.twtstudio.retrox.bike.model.read.User;
import com.twtstudio.retrox.bike.utils.ResourceHelper;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookStarAdapter extends BaseAdapter<User> {

    static class BookStarViewHolder extends BaseViewHolder {

        @BindView(R2.id.civ_portrait)
        CircleImageView mCivPortrait;
        @BindView(R2.id.iv_flag)
        ImageView mIvFlag;
        @BindView(R2.id.tv_name)
        TextView mTvName;
        @BindView(R2.id.tv_reviewtime)
        TextView mTvReviewtime;

        public BookStarViewHolder(View itemView) {
            super(itemView);
        }
    }

    public BookStarAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new BookStarViewHolder(inflater.inflate(R.layout.item_book_star, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BookStarViewHolder starViewHolder = (BookStarViewHolder) holder;
        User user = mDataSet.get(position);

        switch (position) {
            case 0:
                starViewHolder.mIvFlag.setImageResource(R.mipmap.star_1);
                break;
            case 1:
                starViewHolder.mIvFlag.setImageResource(R.mipmap.star_2);
                break;
            case 2:
                starViewHolder.mIvFlag.setImageResource(R.mipmap.star_3);
                break;
        }

        if (user.avatar != null && !user.avatar.equals("")){
            Glide.with(mContext).load(user.avatar).placeholder(R.mipmap.ic_book_avatar).error(R.mipmap.ic_book_avatar).into(starViewHolder.mCivPortrait);
        }

        if (user.review_count != null) {
            SpannableString reviewCountNum = new SpannableString(user.review_count);
            reviewCountNum.setSpan(new ForegroundColorSpan(ResourceHelper.getColor(R.color.read_primary_color)), 0, reviewCountNum.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder reviewCount = new SpannableStringBuilder("写过");
            reviewCount.append(reviewCountNum).append("个书评");
            starViewHolder.mTvReviewtime.setText(reviewCount);
        }

        if (user.twtuname != null) {
            starViewHolder.mTvName.setText(user.twtuname);
        }
    }
}
