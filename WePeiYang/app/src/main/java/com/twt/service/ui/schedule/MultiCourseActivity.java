package com.twt.service.ui.schedule;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;

public class MultiCourseActivity extends AppCompatActivity {

    private double percent_x;
    private double percent_y;

    private CardView mCardView1;
    private CardView mCardView2;
    private TextView mTextView1;
    private TextView mTextView2;

    private ClassTable.Data.Course mCourse1;
    private ClassTable.Data.Course mCourse2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();
        setUpWindow();
        setContentView(R.layout.activity_multi_course);


        mCardView1 = (CardView) findViewById(R.id.multi_course_one_card);
        mCardView2 = (CardView) findViewById(R.id.multi_course_two_card);
        mTextView1 = (TextView) findViewById(R.id.multi_course_one_text);
        mTextView2 = (TextView) findViewById(R.id.multi_course_two_text);

        mCardView1.setCardBackgroundColor(mCourse1.coursecolor);
        mTextView1.setText(mCourse1.coursename+"@ "+mCourse1.arrange.get(0).room);
        mCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToContent(mCourse1,mCardView1);
            }
        });


        mCardView2.setCardBackgroundColor(mCourse2.coursecolor);
        mTextView2.setText(mCourse2.coursename+"@ "+mCourse2.arrange.get(0).room);
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToContent(mCourse2,mCardView2);
            }
        });

    }

    private void setUpWindow(){
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;//dim param
        getWindow().setAttributes(params);

        //size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int hight = size.y;
        getWindow().setLayout((int)(width*percent_x),(int)(hight*percent_y));


    }

    private void getIntentData(){
        Intent intent = getIntent();
        percent_x = intent.getDoubleExtra("percent_x",0.00);
        percent_y = intent.getDoubleExtra("percent_y",0.00);
        mCourse1 = (ClassTable.Data.Course) intent.getSerializableExtra("course1");
        mCourse2 = (ClassTable.Data.Course) intent.getSerializableExtra("course2");
    }

    private void moveToContent(ClassTable.Data.Course course, View view){
        Intent intent = new Intent(MultiCourseActivity.this, ScheduleDetailsActivity.class);
        intent.putExtra("color", course.coursecolor);
        intent.putExtra("course", course);

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MultiCourseActivity.this, view, getString(R.string.schedule_transition));
        startActivity(intent, activityOptions.toBundle());
    }
}
