package com.twtstudio.retrox.bike.read.search;

import android.content.Context;

import com.twtstudio.retrox.bike.api.OnNextListener;
import com.twtstudio.retrox.bike.api.ReadApiClient;
import com.twtstudio.retrox.bike.api.ReadApiSubscriber;
import com.twtstudio.retrox.bike.common.Presenter;
import com.twtstudio.retrox.bike.model.read.SearchBook;

import java.util.List;

/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develop Team
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
