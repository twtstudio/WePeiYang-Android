package com.twt.service.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.interactor.LoginInteractorImpl;
import com.twt.service.support.StatusBarHelper;
import com.twt.service.ui.BaseActivity;
import com.twt.service.ui.common.NextActivity;
import com.twt.service.ui.gpa.GpaActivity;
import com.twt.service.ui.main.MainActivity;
import com.twt.service.ui.schedule.ScheduleActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class LoginActivity extends BaseActivity implements LoginView {
    @InjectView(R.id.et_login_account)
    EditText etLoginAccount;
    @InjectView(R.id.et_login_password)
    EditText etLoginPassword;
    @InjectView(R.id.bt_login)
    Button btLogin;
    LoginPresenter loginPresenter;
    @InjectView(R.id.tv_login_later)
    TextView tvLoginLater;
    @InjectView(R.id.pb_login)
    ProgressBar pbLogin;
    private NextActivity nextActivity = NextActivity.Main;


    public static void actionStart(Context context, NextActivity nextActivity) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("nextactivity", nextActivity);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        NextActivity temp = (NextActivity) getIntent().getSerializableExtra("nextactivity");
        if (temp != null) {
            this.nextActivity = temp;
        }
        loginPresenter = new LoginPresenterImpl(this, new LoginInteractorImpl(), nextActivity);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.validateLogin(etLoginAccount.getText().toString(), etLoginPassword.getText().toString());
            }
        });
        tvLoginLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
    }


    @Override
    public void startMainActivity() {
        MainActivity.actionStart(this);
        finish();
    }

    @Override
    public void startGpaActivity() {
        GpaActivity.actionStart(this);
        finish();
    }

    @Override
    public void startScheduleActivity() {
        ScheduleActivity.actionStart(this);
        finish();
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(etLoginPassword.getWindowToken(), 0);

    }

    @Override
    public void usernameError(String errorMsg) {
        etLoginAccount.setError(errorMsg);
    }

    @Override
    public void passwordError(String errorMsg) {
        etLoginPassword.setError(errorMsg);
    }

    @Override
    public void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProcessing() {
        pbLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProcessing() {
        pbLogin.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        StatusBarHelper.setStatusBar(this, getResources().getColor(android.R.color.holo_blue_light));
        super.setContentView(layoutResID);
    }
}
