package com.twt.service.ecard.window;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

/**
 * PopupWindow with blurred below view.
 * Created by kyle on 2017/3/14.
 */

@SuppressWarnings("ALL")
public class TopPopupWindow extends FrameLayout {
    private static final String TAG = "BlurPopupWindow";

    protected FrameLayout mContentLayout;
    private Activity mActivity;
    private boolean mAnimating;
    private WindowManager mWindowManager;
    private View mContentView;
    private long mAnimationDuration;
    private boolean mDismissOnTouchBackground;
    private boolean mDismissOnClickBack;
    private OnDismissListener mOnDismissListener;

    public TopPopupWindow(@NonNull Context context) {
        super(context);
        init();
    }

    private static int getNaviHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        int contentHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int realHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            final DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            realHeight = metrics.heightPixels;
        } else {
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return realHeight - contentHeight;
    }

    private void init() {
        if (!(getContext() instanceof Activity)) {
            throw new IllegalArgumentException("Context must be Activity");
        }
        mActivity = (Activity) getContext();
        mWindowManager = mActivity.getWindowManager();

        setFocusable(true);
        setFocusableInTouchMode(true);

        mContentLayout = new FrameLayout(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mContentLayout, lp);

        mContentLayout.setBackgroundColor(Color.TRANSPARENT);


    }

    /**
     * Override this to create custom content.
     *
     * @param parent the parent where content view would be.
     * @return
     */
    protected View createContentView(ViewGroup parent) {
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAnimating || !mDismissOnTouchBackground) {
            return super.onTouchEvent(event);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            dismiss();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mAnimating || !mDismissOnClickBack) {
            return super.onKeyUp(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        if (contentView == null) {
            throw new IllegalArgumentException("contentView can not be null");
        }
        if (mContentView != null) {
            if (mContentView.getParent() != null) {
                ((ViewGroup) mContentView.getParent()).removeView(mContentView);
            }
            mContentView = null;
        }
        mContentView = contentView;
        mContentLayout.addView(mContentView);
    }

    public void show() {
        mContentView = createContentView(mContentLayout);
        mContentView.setVisibility(View.INVISIBLE);
        if (mContentView != null) {
            mContentLayout.addView(mContentView);
        }
        mContentView.post(new Runnable() {
            @Override
            public void run() {
                mContentView.setVisibility(View.VISIBLE);
                Log.d(TAG, mContentView.getWidth() + " post " + mContentView.getHeight());
                ObjectAnimator.ofFloat(mContentView, "translationY", -mContentView.getHeight(), 0f).start();
            }
        });

        if (mAnimating) {
            return;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;

        int statusBarHeight = 0;
        int navigationBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        int trimTopHeight = statusBarHeight;
        int trimBottomHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // No need to trim status bar height in SDK > 21.
            trimTopHeight = 0;

            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            if ((lp.flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                trimBottomHeight = navigationBarHeight;
            }

            // This line will cause decor view fill all the screen, even if FLAG_TRANSLUCENT_NAVIGATION
            // was not set.
            params.flags = lp.flags;

            if (trimBottomHeight > 0) {

                // If trimBottomHeight > 0, it means that we cut navigation bar off and we need shrink
                // popup windows' content height by increase bottom padding.
                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + navigationBarHeight);
            } else {

                // If navigation is showing on the screen, whether translucent or not, we should move contentView
                // on top of it.
                boolean moveContent = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    moveContent = true;
                } else if (navigationBarHeight > 0 && (lp.flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) != 0) {
                    // Navigation feature diffs from v19 to v21.
                    moveContent = true;
                }
                if (navigationBarHeight > 0 && moveContent) {
                    if (mContentView != null) {
                        MarginLayoutParams layoutParams = (MarginLayoutParams) mContentView.getLayoutParams();
                        layoutParams.bottomMargin += navigationBarHeight;
                    }
                }
            }
        }

        mWindowManager.addView(this, params);

        onShow();
    }

    public void dismiss() {
        if (mAnimating) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContentView, "translationY", 0f, -mContentView.getHeight());
        onDismiss();
        if (animator == null) {
            mWindowManager.removeView(this);
        } else {
            mAnimating = true;
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeSelf();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    removeSelf();
                }

                private void removeSelf() {
                    try {
                        mWindowManager.removeView(TopPopupWindow.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mAnimating = false;
                    }
                }
            });
            animator.start();
        }
    }


    /**
     * When executing show method in this method, should override {@link TopPopupWindow#createShowAnimator()}
     * and return null as well.
     */
    protected void onShow() {
    }

    /**
     * Do not start any animation in this method. use {@link TopPopupWindow#createDismissAnimator()} instead.
     */
    @CallSuper
    protected void onDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(this);
        }
    }

    public boolean isDismissOnTouchBackground() {
        return mDismissOnTouchBackground;
    }

    public void setDismissOnTouchBackground(boolean dismissOnTouchBackground) {
        mDismissOnTouchBackground = dismissOnTouchBackground;
    }

    public boolean isDismissOnClickBack() {
        return mDismissOnClickBack;
    }

    public void setDismissOnClickBack(boolean dismissOnClickBack) {
        mDismissOnClickBack = dismissOnClickBack;
    }

    public OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss(TopPopupWindow popupWindow);
    }


}
