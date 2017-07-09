package com.example.caokun.fellowsearch.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.caokun.fellowsearch.R;
import com.example.caokun.fellowsearch.common.ui.PActivity;
import com.example.caokun.fellowsearch.model.FellowfindController;
import com.example.caokun.fellowsearch.model.Institute;
import com.example.caokun.fellowsearch.model.Major;
import com.example.caokun.fellowsearch.model.Province;
import com.example.caokun.fellowsearch.model.RefreshEvent;
import com.example.caokun.fellowsearch.model.Senior;
import com.example.caokun.fellowsearch.presenter.FellowPresenter;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/fellowSearch/main")
public class MainActivity extends PActivity<FellowPresenter> implements FellowfindController{
    AutoCompleteTextView editProvince;
    AutoCompleteTextView editInstitute;
    AutoCompleteTextView editMajor;
    AutoCompleteTextView editSenior;
    Toolbar mToolbar;
    Button button;
    ImageView fellow_icon;
    ImageView provinceview;
            ImageView instituteview;
            ImageView majorview;
            ImageView seniorview;
    String  user_province;
    String user_institute;
    String  user_major;
    String  user_senior;
    List<String> provinces=new ArrayList<>();
    List<String> seniors=new ArrayList<>();
    List<String> institutes=new ArrayList<>();
    List<String> majors=new ArrayList<>();
    @Override
    protected int getLayout(){
        return R.layout.activity_fellow_main;
    }
    @Override
    protected void actionStart(Context context){
        editProvince=(AutoCompleteTextView)findViewById(R.id.textprovince);
        editInstitute=(AutoCompleteTextView)findViewById(R.id.textInstitute);
        editMajor=(AutoCompleteTextView)findViewById(R.id.textMajor);
        editSenior=(AutoCompleteTextView)findViewById(R.id.textSenior);
        mToolbar=(Toolbar)findViewById(R.id.main_toolbar);
        button=(Button)findViewById(R.id.finder);
        fellow_icon=(ImageView)findViewById(R.id.fellow_icon);
        provinceview=(ImageView)findViewById(R.id.provinceView);
        instituteview=(ImageView)findViewById(R.id.instituteView);
        majorview=(ImageView)findViewById(R.id.majorView);
        seniorview=(ImageView)findViewById(R.id.seniorView);
    }
    @Override
    protected int getStatusbarColor() {
        return R.color.main_primary;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar.setTitle("老乡查询");

        return mToolbar;
    }

    @Override
    protected void preInitView(){
        fellow_icon.setImageResource(R.drawable.fellow_icon);
         provinceview.setImageResource(R.drawable.fellow_province);
        instituteview.setImageResource(R.drawable.fellow_institute);
        majorview.setImageResource(R.drawable.fellow_major);
        seniorview.setImageResource(R.drawable.fellow_senior);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onEvent(RefreshEvent event){
        Log.d("event", "onEvent: ok.......");
    }

    @Override
    protected void initView() {
        mPresenter.getProvince();
        editProvince.addTextChangedListener(provincewatcher);
        editInstitute.addTextChangedListener(institutewatcher);
        editMajor.addTextChangedListener(majorwatcher);
        editSenior.addTextChangedListener(seniorwatcher);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FellowFindActivity.class);
                intent.putExtra("province",user_province);
                intent.putExtra("institute",user_institute);
                intent.putExtra("major",user_major);
                intent.putExtra("senior",user_senior);
                startActivity(intent);
            }
        });

    }
//    public void setautoadapter(){
//
//
//
//
//    }


    public  void onActionStart(Context context){
    }
    @Override
    protected FellowPresenter getPresenter() {
        return new FellowPresenter(this, this);
    }

    private TextWatcher provincewatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
//            editProvince.clearFocus();//取消焦点
//            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(
//                                    .getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);//关闭输入法
            //隐藏键盘 没写好。。。
                   user_province=editable.toString();
            if (user_province.contains("\n")) {
                user_province = user_province.replaceAll("\n", "");
                editProvince.setText(user_province);
            }
            seniors.clear();
            institutes.clear();
            mPresenter.getSenior(user_province);
            mPresenter.getInstitute(user_province);
        }
    };
    private TextWatcher institutewatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            user_institute=editable.toString();
            if (user_institute.contains("\n")) {
                user_institute = user_institute.replaceAll("\n", "");
                editInstitute.setText(user_institute);
            }
//            majors.clear();
            mPresenter.getMajor(user_province,user_institute);
        }
    };
    private TextWatcher majorwatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            user_major=editable.toString();
            if (user_major.contains("\n")) {
                user_major = user_major.replaceAll("\n", "");
                editMajor.setText(user_major);
            }

        }
    }; private TextWatcher seniorwatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            user_senior=editable.toString();
            if (user_senior.contains("\n")) {
                user_senior = user_senior.replaceAll("\n", "");
                editSenior.setText(user_senior);
            }

        }
    };
    @Override
    public void bindAllProvince(List<Province> provinces) {
        for(int i=0;i<provinces.size();i++) {
            this.provinces.add(provinces.get(i).provincename);
        }
        ArrayAdapter<String> provinceadapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,this.provinces);
        editProvince.setAdapter(provinceadapter);
    }

    @Override
    public void bindAllMajor(List<Major> majors) {
        for(int i=0;i<majors.size();i++) {
            this.majors.add(majors.get(i).majorname);
        }
        ArrayAdapter<String> majoradapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,this.majors);
        editMajor.setAdapter(majoradapter);
    }

    @Override
    public void bindAllInstitute(List<Institute> institutes) {
        for(int i=0;i<institutes.size();i++) {
            this.institutes.add(institutes.get(i).collegename);
        }
        ArrayAdapter<String> instituteadapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,this.institutes);
        editInstitute.setAdapter(instituteadapter);

    }

    @Override
    public void bindAllSenior(List<Senior> seniors) {
        for(int i=0;i<seniors.size();i++) {
             this.seniors.add(seniors.get(i).seniorhigh);
        }
        ArrayAdapter<String> senioeradapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,this.seniors);
        editSenior.setAdapter(senioeradapter);
    }

}
