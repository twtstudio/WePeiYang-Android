package com.twtstudio.retrox.gpa.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.annimon.stream.Stream;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.messenger.Messenger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.gpa.BR;
import com.twtstudio.retrox.gpa.GpaBean;
import com.twtstudio.retrox.gpa.R;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by tjliqy on 2017/6/2.
 */

public class EvaluateListViewModel implements ViewModel {
    private RxAppCompatActivity mRxActivity;
    public final ObservableList<ViewModel> items = new ObservableArrayList<>();

    public static final String TOKEN="remove_item";
    public EvaluateListViewModel(RxAppCompatActivity rxActivity) {
        mRxActivity = rxActivity;

        Messenger.getDefault().register(rxActivity,TOKEN,String.class,(data) ->{
            for (int i = 0; i < items.size(); i++) {
                if (((EvaluateCourseViewModel)items.get(i)).getCourse().evaluate.lesson_id.equals(data)){
                    items.remove(i);
                }
            }
        });
    }


//    public final ObservableList<ViewModel> mViewModels = new ObservableArrayList<>();
    public final ItemBinding itemView = ItemBinding.of(BR.viewModel, R.layout.gpa_item_evaluate);

    void removeItem(int i){
        items.remove(i);
    }
    public void setCourses(ArrayList<GpaBean.Term.Course> unEvaluatedCourses){
        Stream.of(unEvaluatedCourses)
                .map(course -> new EvaluateCourseViewModel(course,mRxActivity))
                .forEach(items::add);
    }
}
