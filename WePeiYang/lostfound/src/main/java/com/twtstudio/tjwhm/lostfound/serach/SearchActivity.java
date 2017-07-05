package com.twtstudio.tjwhm.lostfound.serach;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by tjwhm on 2017/7/2.
 **/

public class SearchActivity extends BaseActivity {

    InputMethodManager inputMethodManager;


    @BindView(R.id.search_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_searview)
    SearchView searchView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected Toolbar getToolbarView() {

        return toolbar;
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected int getToolbarMenu() {
        return 0;
    }

    @Override
    protected void setToolbarMenuClickEvent() {
        //  toolbar.setNavigationIcon(R.drawable.lost_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchActivity.this, "aaa", Toast.LENGTH_SHORT).show();
                hideInputKeyboard();
                finish();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchView.SearchAutoComplete tv = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        tv.setTextColor(Color.WHITE);
        tv.setHintTextColor(Color.WHITE);
        ImageView ivSearch1 = (ImageView) searchView.findViewById(R.id.search_button);
        searchView.onActionViewExpanded();
        ivSearch1.setImageResource(R.drawable.lost_search);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

//        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (inputMethodManager != null) {
//            View v = SearchActivity.this.getCurrentFocus();
//            if (v == null) {
//                return;
//            }
//
//            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//            searchView.clearFocus();
//        }
    }

    private void hideInputKeyboard() {
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = SearchActivity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }
}
