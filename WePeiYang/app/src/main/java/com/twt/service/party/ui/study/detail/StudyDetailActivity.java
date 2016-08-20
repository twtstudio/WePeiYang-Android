package com.twt.service.party.ui.study.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.twt.service.R;
import com.twt.service.party.bean.TextDetailInfo;
import com.twt.service.party.interactor.StudyInteractorImpl;
import com.twt.service.party.ui.BaseActivity;
import com.twt.service.party.ui.study.StudyPresenter;
import com.twt.service.party.ui.study.StudyPresenterImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tjliqy on 2016/8/18.
 */
public class StudyDetailActivity extends BaseActivity implements StudyDetailView{

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tv_study_title)
    TextView tvStudyTitle;
    @InjectView(R.id.wv_study_detail)
    WebView wvStudyDetail;

    private Intent intent;

    private String title;

    private String content;

    private int textId;

    public static final String TYPE_COURSE = "course";

    public static final String TYPE_TEXT = "text";

    private StudyPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_study_detail;
    }

    @Override
    public void preInitView() {
        intent = getIntent();
        if(TYPE_COURSE.equals(intent.getStringExtra("type"))){
            title = intent.getStringExtra("title");
            content = intent.getStringExtra("content");
        }else {
            textId = intent.getIntExtra("text_id",0);
        }
    }

    @Override
    public void initView() {
        if(TYPE_COURSE.equals(intent.getStringExtra("type"))){
            tvStudyTitle.setText(title);
            wvStudyDetail.loadData(content,"text/html;charset=utf-8", null);
        }else {
            presenter = new StudyPresenterImpl(this,new StudyInteractorImpl());
            presenter.getTextDetail(textId);
        }

    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle("文章详情");
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context, String type, String title, String content){
        Intent intent = new Intent(context,StudyDetailActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }
    public static void actionStart(Context context,String type, int textId){
        Intent intent = new Intent(context,StudyDetailActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("text_id",textId);
        context.startActivity(intent);
    }

    @Override
    // TODO: 2016/8/19 不知道如何加载网页 
    public void onGetTextDetail(TextDetailInfo detailInfo) {
        tvStudyTitle.setText(detailInfo.getFile_title());
        wvStudyDetail.loadData(detailInfo.getFile_content(),"text/html;charset=utf-8", null);
    }
}
