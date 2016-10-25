package com.twt.service.rxsrc.read.bookdetail;

import android.content.Context;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.Detail;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookDetailPresenter extends Presenter {

    private BookDetailController mController;

    public BookDetailPresenter(Context context, BookDetailController controller) {
        super(context);
        mController = controller;
    }

    public void getBookDetail(String id){
        ReadApiClient.getInstance().getBookDetail(mContext,new ReadApiSubscriber(mContext,mDetailOnNextListener ),id);
    }

    private OnNextListener<Detail> mDetailOnNextListener = new OnNextListener<Detail>() {
        @Override
        public void onNext(Detail detail) {
            mController.onDetailGot(detail);
        }
    };
}
