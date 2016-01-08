package com.rex.wepeiyang.ui.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.common.internal.Objects;
import com.rex.wepeiyang.R;
import com.rex.wepeiyang.interactor.FeedbackInteractorImpl;
import com.rex.wepeiyang.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedbackActivity extends BaseActivity implements FeedbackView {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.et_email_address)
    EditText etEmailAddress;
    @InjectView(R.id.et_feedback)
    EditText etFeedback;
    private boolean isSendClickable = true;
    private FeedbackPresenter presenter;
    private String ua;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new FeedbackPresenterImpl(this, new FeedbackInteractorImpl());
        ua = new WebView(this).getSettings().getUserAgentString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_primary));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.send:
                if (etEmailAddress.getText().toString().isEmpty()) {
                    etEmailAddress.setError("不能为空");
                } else if (etFeedback.getText().toString().isEmpty()) {
                    etFeedback.setError("不能为空");
                } else if (!isEmailValid(etEmailAddress.getText().toString())) {
                    etEmailAddress.setError("无效的email地址");
                } else {
                    presenter.feedback(ua, etFeedback.getText().toString(), etEmailAddress.getText().toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setEnabled(isSendClickable);
        return true;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSendClickable(boolean clickable) {
        isSendClickable = clickable;
        invalidateOptionsMenu();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
