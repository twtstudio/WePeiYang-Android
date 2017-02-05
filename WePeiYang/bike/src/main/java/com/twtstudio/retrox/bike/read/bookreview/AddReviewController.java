package com.twtstudio.retrox.bike.read.bookreview;


import com.twtstudio.retrox.bike.common.IViewController;
import com.twtstudio.retrox.bike.model.read.ReviewCallback;

/**
 * Created by jcy on 16-10-28.
 *
 * @TwtStudio Mobile Develope Team
 */

public interface AddReviewController extends IViewController {
    void onAddFinished(ReviewCallback callback);
}
