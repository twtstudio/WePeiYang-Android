package com.twt.service.ui.library.search;
import android.os.Bundle;

import com.twt.service.ui.BaseFragment;

/**
 * Created by sunjuntao on 15/11/15.
 */
public class SearchFragment extends BaseFragment implements SearchView {
    public static SearchFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
