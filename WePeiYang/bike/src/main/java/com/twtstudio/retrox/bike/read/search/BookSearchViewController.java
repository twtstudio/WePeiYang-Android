package com.twtstudio.retrox.bike.read.search;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.read.SearchBook;

import java.util.List;

/**
 * Created by jcy on 16-10-23.
 *
 * @TwtStudio Mobile Develope Team
 */

public interface BookSearchViewController extends IViewController {
    void onSearchFinished(List<SearchBook> bookList);
}
