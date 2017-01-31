package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.gpa.GpaBean;

/**
 * Created by retrox on 2017/1/30.
 */

public class TermBriefViewModel implements ViewModel {

    public final ObservableField<String> termName = new ObservableField<>();

    public final ObservableField<String> termScore = new ObservableField<>();

    public final ObservableField<String> termCredit = new ObservableField<>();

    public final ObservableField<String> termGpa = new ObservableField<>();

    public TermBriefViewModel(GpaBean.Term term) {
        termName.set(term.name);
        termScore.set(String.valueOf(term.stat.score));
        termCredit.set(String.valueOf(term.stat.credit));
        termGpa.set(String.valueOf(term.stat.gpa));
    }
}
