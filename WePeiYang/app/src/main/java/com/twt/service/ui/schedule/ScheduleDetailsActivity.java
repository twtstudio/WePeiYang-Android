package com.twt.service.ui.schedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;

public class ScheduleDetailsActivity extends AppCompatActivity {

    private ClassTable.Data.Course mCourse;
    private FrameLayout mLayout;

    public static void actionStart(Context context, ClassTable.Data.Course course) {
        Intent intent = new Intent(context, ScheduleDetailsActivity.class);
        intent.putExtra("course", course);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        //mLayout = (FrameLayout) findViewById(R.id.course_back_detail);
        //int color = getIntent().getIntExtra("color",0);
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

    }
}
