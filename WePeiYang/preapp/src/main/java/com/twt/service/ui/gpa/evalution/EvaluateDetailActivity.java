package com.twt.service.ui.gpa.evalution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.Gpa;
import com.twt.service.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2017/1/15.
 */

public class EvaluateDetailActivity extends BaseActivity implements EvaluateView{

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.rb_1)
    RatingBar mRb1;
    @InjectView(R.id.rb_2)
    RatingBar mRb2;
    @InjectView(R.id.rb_3)
    RatingBar mRb3;
    @InjectView(R.id.rb_4)
    RatingBar mRb4;
    @InjectView(R.id.rb_5)
    RatingBar mRb5;
    @InjectView(R.id.et_note)
    EditText mEtNote;

    EvaluatePresenter mPresenter;

    private Gpa.Data.Term.Course mCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_evaluate_detail);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.gpa_primary_color));
        }
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolbar.setOnMenuItemClickListener(item -> {
            int stars[] = {(int)mRb1.getRating(),(int)mRb2.getRating(),(int)mRb3.getRating(),(int)mRb4.getRating(),(int)mRb5.getRating()};
            mPresenter.postEvaluate(mCourse.evaluate.lesson_id,mCourse.evaluate.union_id,mCourse.evaluate.course_id,mCourse.evaluate.term,stars,mEtNote.getText().toString().trim());
            return true;
        });

        mPresenter = new EvaluatePresenterImpl(this,new EvaluateIntercepterImpl(),this);
        int star = getIntent().getIntExtra("star",5);
        mCourse = (Gpa.Data.Term.Course) getIntent().getSerializableExtra("key");

        mRb1.setRating(star);
        mRb2.setRating(star);
        mRb3.setRating(star);
        mRb4.setRating(star);
        mRb5.setRating(star);
    }

    public static void onActionStart(Activity activity, Gpa.Data.Term.Course course, int star){
        Intent intent = new Intent(activity,EvaluateDetailActivity.class);
        intent.putExtra("key",course);
        intent.putExtra("star", star);
        activity.startActivityForResult(intent,0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_party_yes,menu);
            return true;
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteCourse() {
        Intent intent = new Intent();
        intent.putExtra("success",true);
        this.setResult(0,intent);
        finish();
    }
}
