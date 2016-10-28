package com.twt.service.rxsrc.read.bookreview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.twt.service.R;
import com.twt.service.databinding.ActivityBookAddReviewBinding;
import com.twt.service.rxsrc.common.ui.BPActivity;

/**
 * Created by jcy on 16-10-28.
 *
 * @TwtStudio Mobile Develope Team
 */

public class AddReviewActivity extends BPActivity<AddReviewPresenter> implements AddReviewController {
    private ActivityBookAddReviewBinding mBinding;
    @Override
    protected AddReviewPresenter getPresenter() {
        return new AddReviewPresenter(this,this);
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.bike_toolbar_color;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_book_add_review);
        setUpToolbar(mBinding.readEditReviewToolbar);
    }

    @Override
    public void onAddFinished() {
        toastMessage("评论已添加");
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_party_yes,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_submit_yes){
            mPresenter.addReview(mBinding.readEditReviewEdittext.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
