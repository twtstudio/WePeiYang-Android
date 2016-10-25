package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;

import com.twt.service.rxsrc.common.ui.BPActivity;
import com.twt.service.rxsrc.common.ui.BaseBindActivity;
import com.twt.service.rxsrc.model.read.Detail;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailActivity extends BPActivity<BookDetailPresenter> implements BookDetailController{
    @Override
    protected BookDetailPresenter getPresenter() {
        return new BookDetailPresenter(this,this);
    }

    @Override
    protected void actionStart(Context context) {

    }

    @Override
    protected int getStatusbarColor() {
        return 0;
    }

    @Override
    public void onDetailGot(Detail detail) {

    }
}
