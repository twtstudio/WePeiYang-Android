package com.twtstudio.tjliqy.party.ui.study.answer;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.sothree.slidinguppanel.ScrollableViewHelper;

/**
 * Created by tjliqy on 2016/8/23.
 */
public class NestedScrollableViewHelper extends ScrollableViewHelper{
    @Override
    public int getScrollableViewScrollPosition(View mScrollableView, boolean isSlidingUp) {
        if (mScrollableView instanceof NestedScrollView) {
            if(isSlidingUp){
                return mScrollableView.getScrollY();
            } else {
                NestedScrollView nsv = ((NestedScrollView) mScrollableView);
                View child = nsv.getChildAt(0);
                return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
            }
        } else {
            return 0;
        }
    }
}
