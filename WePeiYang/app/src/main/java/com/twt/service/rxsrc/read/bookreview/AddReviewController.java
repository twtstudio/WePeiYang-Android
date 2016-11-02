package com.twt.service.rxsrc.read.bookreview;

import com.twt.service.rxsrc.common.IViewController;
import com.twt.service.rxsrc.model.read.ReviewCallback;

/**
 * Created by jcy on 16-10-28.
 *
 * @TwtStudio Mobile Develope Team
 */

public interface AddReviewController extends IViewController {
    void onAddFinished(ReviewCallback callback);
}
