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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.MenuItem;
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
    private FrameLayout mSharedElement;
    private int mToolBarColor;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private ScheduleDetailAdapter mAdapter;
    

    public static void actionStart(Context context, ClassTable.Data.Course course) {
        Intent intent = new Intent(context, ScheduleDetailsActivity.class);
        intent.putExtra("course", course);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        mRecyclerView = (RecyclerView) findViewById(R.id.schedule_detail_rcv);
        mToolbar = (Toolbar) findViewById(R.id.schedule_detail_toolbar);
        mToolbar.setTitle(" ");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSharedElement = (FrameLayout) findViewById(R.id.schedule_detail_shared_element);
        //get Intent Data
        mToolBarColor = getIntent().getIntExtra("color",0);
        //取得序列化的course
        mCourse = (ClassTable.Data.Course) getIntent().getSerializableExtra("course");

        mAdapter = new ScheduleDetailAdapter(this,new ScheduleDetail(mCourse));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(mToolBarColor);
        }

        //api21
        setTint(getResources().getDrawable(R.drawable.circle_24dp),mToolBarColor);
        mSharedElement.setBackground(getResources().getDrawable(R.drawable.circle_24dp));
        setUpAnimations();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpAnimations(){
        Explode explode = new Explode();
        explode.setDuration(300);

        Slide slide = new Slide(Gravity.LEFT);
        slide.setDuration(300);

        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);

        Transition transition = getWindow().getSharedElementEnterTransition();
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                mSharedElement.setVisibility(View.GONE);
                animateRevealShow(mToolbar);
                mToolbar.setTitle(mCourse.coursename);

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
        anim.setInterpolator(new AccelerateInterpolator());
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(mToolBarColor);
        }
    }

    private Drawable setTint(Drawable drawable, int color) {
        final Drawable newDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(newDrawable, color);
        return newDrawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
