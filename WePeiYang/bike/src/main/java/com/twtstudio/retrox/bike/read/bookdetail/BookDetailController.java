package com.twtstudio.retrox.bike.read.bookdetail;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.read.Detail;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public interface BookDetailController extends IViewController {
    void onDetailGot(Detail detail);
}
