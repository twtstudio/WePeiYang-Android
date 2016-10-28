package com.twt.service.rxsrc.read.home.recommend;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.rxsrc.common.ui.BaseAdapter;
import com.twt.service.rxsrc.common.ui.BaseViewHolder;
import com.twt.service.rxsrc.model.read.Review;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by tjliqy on 2016/10/28.
 */

public class BookReviewAdapter extends BaseAdapter<Review> {




    static class BookReviewHolder extends BaseViewHolder {

        @InjectView(R.id.civ_portrait)
        CircleImageView mCivPortrait;
        @InjectView(R.id.tv_name)
        TextView mTvName;
        @InjectView(R.id.tv_share)
        TextView mTvShare;
        @InjectView(R.id.tv_content)
        TextView mTvContent;
        @InjectView(R.id.tv_date)
        TextView mTvDate;
        @InjectView(R.id.tv_like)
        TextView mTvLike;
        @InjectView(R.id.divider)
        View mDivider;
        public BookReviewHolder(View itemView) {
            super(itemView);
        }
    }

    public BookReviewAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new BookReviewHolder(inflater.inflate(R.layout.item_book_review, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        BookReviewHolder reviewHolder = (BookReviewHolder)holder;

        SpannableString title = new SpannableString("《从你的全世界路过》");
        title.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/10/28 书籍名字点击后的跳转写在这里 
                Toast.makeText(mContext, title.toString(), Toast.LENGTH_SHORT).show();
            }
        },0,title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        reviewHolder.mTvContent.setText(title);
        reviewHolder.mTvContent.append("这里是内容这里是内容这里是内容这里是内容");
        reviewHolder.mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
        if (position == getItemCount() - 1) {
            reviewHolder.mDivider.setVisibility(View.INVISIBLE);
        }
    }
}
