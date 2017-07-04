package com.twtstudio.tjwhm.lostfound.support;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by tjwhm on 2017/7/4.
 **/

public class FloatingActionMenuBehavior extends android.support.design.widget.CoordinatorLayout.Behavior<FloatingActionsMenu> {

    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutLinearInInterpolator();

    private int mTotalDy = 0;
    private boolean isAnimating = false;
    private FloatingActionsMenu.OnFloatingActionsMenuUpdateListener mFloatingMenuUpdateListener = new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
        @Override
        public void onMenuExpanded() {
            isAnimating = false;
        }

        @Override
        public void onMenuCollapsed() {
            isAnimating = false;
        }
    };

    public FloatingActionMenuBehavior() {
    }

    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            updateFabTranslationForSnackbar(child, dependency);
        }
        return false;
    }

    private void updateFabTranslationForSnackbar(FloatingActionsMenu child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        ViewCompat.setTranslationY(child, translationY);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        mTotalDy = dyConsumed < 0 && mTotalDy > 0 || dyConsumed > 0 && mTotalDy < 0 ? 0 : mTotalDy;
        attemptCancelAnimation(child);
        mTotalDy += dyConsumed;
        updateFloatingActionMenu(child);
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, final FloatingActionsMenu child, View target, float velocityX, float velocityY) {
        Log.i("onNestedPreFling", " velocityY: " + velocityY);
        if (Math.abs(velocityY) < Math.abs(velocityX)) return false;

        final int childCount = child.getChildCount();

        final View firstFab = child.getChildAt(childCount - 1);
        if (velocityY < 0 && isScaleX(firstFab, 0f)) {
            /*
                Velocity is negative, we are flinging up
             */
            scaleTo(firstFab, 1f, null);
            firstFab.setClickable(true);
        } else if (velocityY > 0) {
              /*
                Velocity is positive, we are flinging down
             */
            if (child.isExpanded()) {
                child.collapse();
                child.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                    @Override
                    public void onMenuExpanded() {

                    }

                    @Override
                    public void onMenuCollapsed() {
                        if (isScaleX(firstFab, 1f)) {
                            scaleTo(firstFab, 0f, null);
                            firstFab.setClickable(false);
                        }
                    }
                });
            } else {
                if (isScaleX(firstFab, 1f)) {
                    scaleTo(firstFab, 0f, null);
                    firstFab.setClickable(false);
                }
            }

        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private void attemptCancelAnimation(FloatingActionsMenu child) {
        if (mTotalDy == 0) {
            ViewCompat.animate(child).cancel();
            ViewCompat.animate(child.getChildAt(child.getChildCount() - 1)).cancel();
        }
    }


    private void updateFloatingActionMenu(final FloatingActionsMenu child) {
        int totalHeight = child.getHeight();
        View firstFab = child.getChildAt(0);
        if (child.getChildCount() > 1) {
            /*
               More than one fab in the menu
             */
            if (child.isExpanded()) {
                /*
                    We have expanded menu and it needs to be collapsed,
                    when we have scrolled to the height of the menu
                 */
                totalHeight -= firstFab.getHeight();
                if (mTotalDy > totalHeight && !isAnimating) {
                    /*
                       Scrolling down.
                     */
                    isAnimating = true;
                    child.collapse();
                    child.setOnFloatingActionsMenuUpdateListener(mFloatingMenuUpdateListener);
                } else if (mTotalDy < 0 && !child.isExpanded()) {
                    /*
                       Scrolling up
                     */
                    if (isScaleX(child, 0f)) {
                        scaleTo(child, 1f, null);
                        firstFab.setClickable(true);
                    }
                }
            } else {
                /*
                  The menu is collapsed. We are left with only one fab.
                 */
                totalHeight = firstFab.getHeight();
                firstFab = child.getChildAt(child.getChildCount() - 1);
                updateFirstFloatingActionButton(firstFab, totalHeight);
            }
        } else {
            // Only one fab.
            totalHeight = child.getHeight();
            updateFirstFloatingActionButton(child.getChildAt(0), totalHeight);
        }
    }


    private void updateFirstFloatingActionButton(View firstFab, int totalHeight) {
        if (mTotalDy > totalHeight && isScaleX(firstFab, 1f)) {
            firstFab.setClickable(false);
            scaleTo(firstFab, 0f, null);
        } else if (mTotalDy < 0 && Math.abs(mTotalDy) >= totalHeight && isScaleX(firstFab, 0f)) {
            scaleTo(firstFab, 1f, null);
            firstFab.setClickable(true);
        }
    }

    private void scaleTo(View floatingActionButton, float value, Runnable runnable) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(floatingActionButton)
                .scaleX(value).scaleY(value).setDuration(100)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        if (runnable != null) {
            viewPropertyAnimatorCompat.withEndAction(runnable);
        }
        viewPropertyAnimatorCompat.start();
    }

    private boolean isScaleX(View v, float value) {
        return ViewCompat.getScaleX(v) == value;
    }


    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout && ViewCompat.getTranslationY(child) != 0.0F) {
            ViewCompat.animate(child).translationY(0.0F).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener((ViewPropertyAnimatorListener) null);
        }

    }

}