package com.twt.service.ui.schedule;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twt.service.R;
import com.twt.service.bean.ClassTable;

public class ScheduleDetailsActivity extends AppCompatActivity {

    private ClassTable.Data.Course mCourse;

    public static void actionStart(Context context, ClassTable.Data.Course course) {
        Intent intent = new Intent(context, ScheduleDetailsActivity.class);
        intent.putExtra("course", course);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        mCourse = (ClassTable.Data.Course) getIntent().getSerializableExtra("course");
    }
}
