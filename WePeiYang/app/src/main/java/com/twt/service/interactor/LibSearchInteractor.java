package com.twt.service.interactor;

import com.twt.service.ui.library.search.OnSearchCallback;

/**
 * Created by jcy on 2016/7/18.
 */

public interface LibSearchInteractor {
    void libSearch(String title, int page , OnSearchCallback onSearchCallback);
}
