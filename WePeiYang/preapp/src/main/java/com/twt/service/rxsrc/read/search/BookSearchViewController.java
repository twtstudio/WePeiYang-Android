package com.twt.service.rxsrc.read.search;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.read.SearchBook;

import java.util.List;

/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develope Team
 */

public interface BookSearchViewController extends IViewController {
    void onSearchFinished(List<SearchBook> bookList);
}
