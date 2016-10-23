package com.twt.service.rxsrc.read.search;

import android.content.Context;

import com.twt.service.rxsrc.api.OnNextListener;
import com.twt.service.rxsrc.api.ReadApiClient;
import com.twt.service.rxsrc.api.ReadApiSubscriber;
import com.twt.service.rxsrc.common.Presenter;
import com.twt.service.rxsrc.model.read.SearchBook;

import java.util.List;

/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develope Team
 */

public class BookSearchPresenter extends Presenter {
    private BookSearchViewController mViewController;
    public BookSearchPresenter(Context context ,BookSearchViewController viewController) {
        super(context);
        mViewController = viewController;
    }

    public void search(String info){
        ReadApiClient.getInstance().searchBooks(mContext,new ReadApiSubscriber(mContext,mSearchListener),info);
    }

    private OnNextListener<List<SearchBook>> mSearchListener = new OnNextListener<List<SearchBook>>() {
        @Override
        public void onNext(List<SearchBook> searchBooks) {
            mViewController.onSearchFinished(searchBooks);
        }
    };
}
