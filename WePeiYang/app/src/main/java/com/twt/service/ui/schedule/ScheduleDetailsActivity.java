package com.twt.service.ui.schedule;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;

public class ScheduleDetailsActivity extends AppCompatActivity {

    private ClassTable.Data.Course mCourse;
    private FrameLayout mLayout;
    private int mToolBarColor;
    private Toolbar mToolbar;
    private CardView mCardView;
    private RelativeLayout mRelativeLayout;

    public static void actionStart(Context context, ClassTable.Data.Course course) {
        Intent intent = new Intent(context, ScheduleDetailsActivity.class);
        intent.putExtra("course", course);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        mToolbar = (Toolbar) findViewById(R.id.schedule_detail_toolbar);
        //mCardView = (CardView) findViewById(R.id.schedule_detail_shared_element);
        mLayout = (FrameLayout) findViewById(R.id.schedule_detail_shared_element);
        //mRelativeLayout = (RelativeLayout) findViewById(R.id.schedule_back_relative_layout);



        //mLayout = (FrameLayout) findViewById(R.id.course_back_detail);
        mToolBarColor = getIntent().getIntExtra("color",0);


        setTint(getResources().getDrawable(R.drawable.circle_24dp),mToolBarColor);
        mLayout.setBackground(getResources().getDrawable(R.drawable.circle_24dp));
        //mLayout.setBackgroundColor(color);

        setUpAnimations();
        //mCourse = (ClassTable.Data.Course) getIntent().getSerializableExtra("course");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimations(){
        Explode explode = new Explode();
        explode.setDuration(300);

        Slide slide = new Slide();
        slide.setDuration(300);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Transition transition = getWindow().getSharedElementEnterTransition();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
//                ColorStateList list = new ColorStateList()
//                mLayout.setBackgroundTintList(mToolBarColor);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                //mCardView.setVisibility(View.GONE);
                //mRelativeLayout.setVisibility(View.GONE);
//                mCardView.setCardBackgroundColor();
                animateRevealShow(mToolbar);

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

    }

    private void animateRevealShow(View viewRoot){
        viewRoot.setBackgroundColor(mToolBarColor);
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    private Drawable setTint(Drawable drawable, int color) {
        final Drawable newDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newDrawable, color);
        return newDrawable;
    }
}
