package com.twtstudio.retrox.bike.read.bookreview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.common.ui.BPActivity;
import com.twtstudio.retrox.bike.databinding.ActivityBookAddReviewBinding;
import com.twtstudio.retrox.bike.model.read.RefreshEvent;
import com.twtstudio.retrox.bike.model.read.ReviewCallback;

import de.greenrobot.event.EventBus;

/**
 * Created by jcy on 16-10-28.
 *
 * @TwtStudio Mobile Develope Team
 */

public class AddReviewActivity extends BPActivity<AddReviewPresenter> implements AddReviewController {
    private ActivityBookAddReviewBinding mBinding;
    private RatingBar mRatingBar;
    private EditText mEditText;
    private String mId;


    @Override
    protected AddReviewPresenter getPresenter() {
        return new AddReviewPresenter(this, this);
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.read_primary_color;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_add_review);
        setUpToolbar(mBinding.readAddReviewToolbar);
        mRatingBar = mBinding.readEditReviewRatingbar;
        mEditText = mBinding.readEditReviewEdittext;
        mId = getIntent().getStringExtra("id");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_party_yes, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_submit_yes) {
            //mPresenter.addReview(mBinding.readEditReviewEdittext.getText().toString());
            String s = mEditText.getText().toString();
            if (s.trim().length() < 10){
                toastMessage("评论不标准或过短,最少10个字哦");
            }else {
                mPresenter.addReview(mId,s.trim(), mRatingBar.getRating());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddFinished(ReviewCallback callback) {
        toastMessage("已评论");
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }
}
