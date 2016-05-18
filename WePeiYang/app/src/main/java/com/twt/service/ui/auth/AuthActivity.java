package com.twt.service.ui.auth;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twt.service.R;
import com.twt.service.common.ui.PActivity;
import com.twt.service.router.RouterSchema;
import com.twt.service.support.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.campusapp.router.Router;

/**
 * Created by huangyong on 16/5/18.
 */
public class AuthActivity extends PActivity implements AuthViewController {

    public static final String AUTH_TWT = "twt";
    public static final String AUTH_TJU = "tju";
    public static final String ATUH_LIB = "lib";

    protected static final List<String> AUTH_TYPE = new ArrayList<>();

    static {
        AUTH_TYPE.add(AUTH_TWT);
        AUTH_TYPE.add(AUTH_TJU);
        AUTH_TYPE.add(ATUH_LIB);
    }

    @InjectView(R.id.auth_account)
    EditText mUsernameView;
    @InjectView(R.id.auth_password)
    EditText mPasswordView;
    @InjectView(R.id.auth_submit)
    Button mSubmitButton;
    @InjectView(R.id.auth_later)
    View mLoginLater;

    protected String mType;

    @Override
    protected AuthPresenter getPresenter() {
        if (mPresenter == null) {
            return new AuthPresenter(this, this);
        }
        return (AuthPresenter) mPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_auth;
    }

    @Override
    protected void preInitView() {
        super.preInitView();
        try {
            mType = getIntent().getStringExtra("type");
            if (!AUTH_TYPE.contains(mType)) {
                finish();
            }
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    protected void initView() {
        if (AUTH_TWT.equals(mType)) {
            mUsernameView.setHint(ResourceHelper.getString(R.string.hint_login_account));
            mPasswordView.setHint(ResourceHelper.getString(R.string.hint_login_password));
            mSubmitButton.setText(ResourceHelper.getString(R.string.login_btn_text));
            mLoginLater.setVisibility(View.VISIBLE);
        } else if (AUTH_TJU.equals(mType)) {
            // TODO
        } else if (ATUH_LIB.equals(mType)) {
            // TODO
        }
    }

    @OnClick({R.id.auth_submit, R.id.auth_later})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.auth_submit) {
            String username = mUsernameView.getText().toString();
            String password = mPasswordView.getText().toString();
            if (getPresenter().validate(username, password)) {
                getPresenter().submit(username, password);
            }
        } else if (id == R.id.auth_later) {
            Router.open(RouterSchema.HOME);
            finish();
        }
    }

    public String getType() {
        return mType;
    }

    @Override
    public void showUsernameError(String message) {
        mUsernameView.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        mPasswordView.setError(message);
    }
}
