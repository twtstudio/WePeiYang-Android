package com.twt.service.party.ui.submit.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.twt.service.R;
import com.twt.service.party.interactor.SubmitInteractorImpl;
import com.twt.service.party.ui.BaseActivity;
import com.twt.service.support.PrefUtils;

import butterknife.InjectView;

/**
 * Created by dell on 2016/7/19.
 */
public class SubmitDetailActivity extends BaseActivity implements SubmitDetailView{

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.et_submit_title)
    EditText title;
    @InjectView(R.id.et_submit_content)
    EditText content;

    private static final String TYPE = "submit_type";
    private static final String TEXT = "submit_text";

    private String submitType;
    private String submitText;

    private SubmitPresenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_submit_detail;
    }

    @Override
    public void preInitView() {
        Intent intent = getIntent();
        submitType = intent.getStringExtra(TYPE);
        submitText = intent.getStringExtra(TEXT);
        presenter = new SubmitPresenterImpl(this,new SubmitInteractorImpl());
    }

    @Override
    public void initView() {
        title.setText(PrefUtils.getPrefSubmitTitle());
        content.setText(PrefUtils.getPrefSubmitContent());
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle(R.string.submit_party);
        return toolbar;
    }

    @Override
    public int getMenu() {
        return R.menu.menu_party_yes;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_submit_yes:
//                if("".equals(title.getText().toString().trim()) || "".equals(content.getText().toString().trim())){
//                    Toast.makeText(SubmitDetailActivity.this, "请输入标题和内容", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    setDialog("确认要" + submitText +"吗？",0);
//                }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onMenuClickActions(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_submit_yes:
                if("".equals(title.getText().toString().trim()) || "".equals(content.getText().toString().trim())){
                    Toast.makeText(SubmitDetailActivity.this, "请输入标题和内容", Toast.LENGTH_SHORT).show();
                }
                else {
                    setDialog("确认要" + submitText +"吗？",0);
                }
        }
        return true;
    }

    public static void actionStart(Context context, String type, String submitText){
        Intent intent = new Intent(context, SubmitDetailActivity.class);
        intent.putExtra(TYPE,type);
        intent.putExtra(TEXT,submitText);
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        if(!"".equals(title.getText().toString().trim())){
            PrefUtils.setPrefSubmitTitle(title.getText().toString().trim());
        }else {
            PrefUtils.removePrefSubmitTitle();
        }
        if(!"".equals(content.getText().toString().trim())){
            PrefUtils.setPrefSubmitContent(content.getText().toString().trim());
        }else {
            PrefUtils.removePrefSubmitContent();
        }
    }

    @Override
    public void onClickPositiveButton(int id) {
        super.onClickPositiveButton(id);
        presenter.submit(title.getText().toString(),content.getText().toString(),submitType);
    }

    @Override
    public void onSuccess(String msg) {
        toastMsg(msg);
        title.setText("");
        content.setText("");
        finish();
    }

    @Override
    public void toastMsg(String msg) {
        Toast.makeText(SubmitDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
