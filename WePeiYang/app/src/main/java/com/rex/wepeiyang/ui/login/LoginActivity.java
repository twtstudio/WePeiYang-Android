package com.rex.wepeiyang.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.rex.wepeiyang.R;
import com.rex.wepeiyang.support.StatusBarHelper;
import com.rex.wepeiyang.ui.BaseActivity;
import com.rex.wepeiyang.ui.main.MainActivity;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        loginPresenter = new LoginPresenterImpl(this);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.validateLogin(etLoginAccount.getText().toString(), etLoginPassword.getText().toString());
            }
        });
    }


    @Override
    public void startMainActivity() {
        MainActivity.actionStart(this);
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
    public void setContentView(int layoutResID) {
        StatusBarHelper.setStatusBar(this, getResources().getColor(android.R.color.holo_blue_light));
        super.setContentView(layoutResID);
    }
}
