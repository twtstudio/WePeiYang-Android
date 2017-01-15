package com.twt.service.ui.gpa.evalution;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.bean.Gpa;
import com.twt.service.ui.BaseActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.R.id.message;

/**
 * Created by tjliqy on 2017/1/12.
 */

public class EvaluateListActivity extends BaseActivity implements EvaluateView{


    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.rv_evalute)
    RecyclerView mRvEvalute;

    public EvaluatePresenter mPresenter;
    private static final String KEY = "key";

    private EvaluateAdapter mAdapter;

    private List<Gpa.Data.Term.Course> mCourseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_evalute_list);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.gpa_primary_color));
        }
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        mPresenter = new EvaluatePresenterImpl(this,new EvaluateIntercepterImpl(),this);
        mCourseList = (List<Gpa.Data.Term.Course>) getIntent().getSerializableExtra(KEY);
        mAdapter = new EvaluateAdapter(this,this,mPresenter);
        mAdapter.hideFooter();
        mRvEvalute.setLayoutManager(new LinearLayoutManager(this));
        mRvEvalute.setAdapter(mAdapter);
        mAdapter.addItems(mCourseList);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void onActionStart(Context context, List<Gpa.Data.Term.Course> list){
        Intent intent = new Intent(context, EvaluateListActivity.class);
        intent.putExtra(KEY,(Serializable)list);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && data!= null){
            if(data.getBooleanExtra("success",false)) {
                deleteCourse();
            }
        }
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteCourse() {
        mAdapter.deleteCourse();
    }
}
