package com.twtstudio.retrox.tjulibrary.home;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.tjulibrary.R;
import com.twtstudio.retrox.tjulibrary.provider.Book;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twtstudio.retrox.tjulibrary.BR;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.functions.Action0;


/**
 * Created by retrox on 2017/2/21.
 */

public class HomeLibItemViewModel implements ViewModel {

    private Context mContext;

    public TjuLibProvider libProvider;

    public final ObservableField<String> message = new ObservableField<>("正在刷新");

    public final ObservableBoolean isProgressing = new ObservableBoolean(true);

    public final ObservableBoolean haveBooks = new ObservableBoolean(true);

    // ok:0 progressing:1 warning:2
    public final ObservableInt state = new ObservableInt(1);

    //指示刷新或者，其他的图标
    public final ObservableField<Drawable> obDrawable = new ObservableField<>();

    public final ObservableArrayList<ViewModel> viewModels = new ObservableArrayList<>();

    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_common_book);

    public final ReplyCommand refreshClick = new ReplyCommand(this::refreshInfo);

    /**
     * 超出3本书的缩略
     */
    public final ReplyCommand loadMore = new ReplyCommand(this::loadMoreBooks);
    private final List<ViewModel> moreBookContainer = new ArrayList<>();
    public ObservableBoolean isExpanded = new ObservableBoolean(false);

    private Drawable okImage;
    private Drawable warningImage;

    public HomeLibItemViewModel(Context mContext) {
        this.mContext = mContext;
        libProvider = new TjuLibProvider(mContext);
        initDrawable();
        init();
        obDrawable.set(okImage);
    }

    private void initDrawable() {
        okImage = ContextCompat.getDrawable(mContext, R.drawable.lib_ok);
        warningImage = ContextCompat.getDrawable(mContext, R.drawable.lib_warning);
    }

    private void init() {
        state.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (state.get() == 0) {
                    isProgressing.set(false);
                    obDrawable.set(okImage);
                } else if (state.get() == 2) {
                    isProgressing.set(false);
                    obDrawable.set(warningImage);
                } else {
                    isProgressing.set(true);
                    message.set("正在刷新");
                }
            }
        });

        refreshInfo();
    }

    public void loadMoreBooks() {
        if (moreBookContainer.size() != 0 && !isExpanded.get()) {
            viewModels.addAll(moreBookContainer);
            isExpanded.set(true);
        }
    }

    public void refreshInfo() {
        state.set(1);
        libProvider.getUserInfo(info -> {
            state.set(0);
            message.set("您一共借了"+info.books.size()+"本书");
            //添加当前书列表
            if (null == info.books || info.books.size() == 0) {
                haveBooks.set(false);
                message.set("还没有从图书馆借书呢");
            }
            viewModels.clear();
            if (info.books.size() <= 3) {
                for (Book book : info.books) {
                    viewModels.add(new BookItemViewModel(mContext, book));
                }
            } else {
                for (int i = 0; i < info.books.size(); i++) {
                    if (i < 3) {
                        viewModels.add(new BookItemViewModel(mContext, info.books.get(i)));
                    } else {
                        moreBookContainer.add(new BookItemViewModel(mContext, info.books.get(i)));
                    }
                }
            }

        });
    }


}
