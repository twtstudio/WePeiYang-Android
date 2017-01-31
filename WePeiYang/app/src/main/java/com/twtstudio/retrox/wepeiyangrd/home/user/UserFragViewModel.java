package com.twtstudio.retrox.wepeiyangrd.home.user;

import android.databinding.ObservableArrayList;

import com.kelin.mvvmlight.base.ViewModel;
import com.twtstudio.retrox.wepeiyangrd.BR;
import com.twtstudio.retrox.wepeiyangrd.R;
import com.twtstudio.retrox.wepeiyangrd.base.BaseActivity;

import me.tatarka.bindingcollectionadapter.BaseItemViewSelector;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by retrox on 2017/1/14.
 */

public class UserFragViewModel implements ViewModel {

    private BaseActivity mActivity;

    public ObservableArrayList list = new ObservableArrayList();

    public final ItemViewSelector<ViewModel> itemView = new BaseItemViewSelector<ViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ViewModel item) {
            if (position == 0) {
                itemView.set(BR.viewModel, R.layout.item_user_avatar);
            } else if (1 <= position && position <= 3) {
                itemView.set(BR.viewModel,R.layout.item_user_commons);
            }else {
                itemView.set(BR.viewModel,R.layout.item_user_settings);
            }
        }

        @Override
        public int viewTypeCount() {
            return 3;
        }
    };

    public UserFragViewModel(BaseActivity activity) {
        mActivity = activity;
        init();
    }

    private void init(){
        list.add(new AvatarItemViewModel());
        list.add(new CommonItemViewModel(mActivity,CommonItemViewModel.MESSAGE));
        list.add(new CommonItemViewModel(mActivity,CommonItemViewModel.COLLECTION));
        list.add(new CommonItemViewModel(mActivity,CommonItemViewModel.RECORD));
        list.add(new PrefItemViewModel(mActivity,PrefItemViewModel.NIGHTMODE));
    }
}
