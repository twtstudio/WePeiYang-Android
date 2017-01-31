package com.twt.service.rxsrc.read.bookdetail;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.read.Detail;

/**
 * Created by jcy on 16-10-25.
 *
 * @TwtStudio Mobile Develope Team
 */

public interface BookDetailController extends IViewController {
    void onDetailGot(Detail detail);
}
