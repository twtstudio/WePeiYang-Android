package com.twtstudio.retrox.gpa.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.R;
import com.twtstudio.retrox.gpa.client.ApiClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tjliqy on 2017/6/2.
 */

public class EvaluateDetailActivity extends RxAppCompatActivity {

    RatingBar mRb1;
    RatingBar mRb2;
    RatingBar mRb3;
    RatingBar mRb4;
    RatingBar mRb5;
    EditText mEtNote;

    private final int star = 5;

    Activity activity;
    GpaBean.Term.Course course;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(getResources().getColor(R.color.gpa_primary_color));
//        }
        activity = this;
        setContentView(R.layout.gpa_activity_evaluate_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("GPA");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRb1 = (RatingBar) findViewById(R.id.rb_1);
        mRb2 = (RatingBar) findViewById(R.id.rb_2);
        mRb3 = (RatingBar) findViewById(R.id.rb_3);
        mRb4 = (RatingBar) findViewById(R.id.rb_4);
        mRb5 = (RatingBar) findViewById(R.id.rb_5);

        mEtNote = (EditText) findViewById(R.id.et_note);
        course = (GpaBean.Term.Course) getIntent().getSerializableExtra("key");
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.gpa_coordinator);

        mRb1.setRating(star);
        mRb2.setRating(star);
        mRb3.setRating(star);
        mRb4.setRating(star);
        mRb5.setRating(star);

    }

    public void postGpaEvaluate(String authorization, String token, String lessonId, String unionId, String courseId, String term, int[] fiveQ, String note) {
        new ApiClient().postGpaEvaluate(authorization, token, lessonId, unionId, courseId, term, fiveQ, note, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                Messenger.getDefault().send(lessonId,EvaluateListViewModel.TOKEN);
                Messenger.getDefault().sendNoMsg(GpaActivityViewModel.TOKEN);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "评价失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_submit_yes) {
            int stars[] = {(int) mRb1.getRating(), (int) mRb2.getRating(), (int) mRb3.getRating(), (int) mRb4.getRating(), (int) mRb5.getRating()};
            postGpaEvaluate(CommonPrefUtil.getToken(), CommonPrefUtil.getGpaToken(), course.evaluate.lesson_id, course.evaluate.union_id, course.evaluate.course_id, course.evaluate.term, stars, mEtNote.getText().toString().trim());
        }
        return true;
    }
}
