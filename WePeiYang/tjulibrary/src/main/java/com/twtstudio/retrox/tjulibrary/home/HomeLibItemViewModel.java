package com.twtstudio.retrox.tjulibrary.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.tapadoo.alerter.Alerter;
import com.twtstudio.retrox.tjulibrary.R;
import com.twtstudio.retrox.tjulibrary.provider.Book;
import com.twtstudio.retrox.tjulibrary.provider.RenewResult;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;
import com.twtstudio.retrox.tjulibrary.BR;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import retrofit2.adapter.rxjava.HttpException;


/**
 * Created by retrox on 2017/2/21.
 */

public class HomeLibItemViewModel implements ViewModel {

    private Context mContext;

    public TjuLibProvider libProvider;

    //卡片提示信息的内容
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

    public final ReplyCommand renewMyBooks = new ReplyCommand(() -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setIcon(R.drawable.lib_library)
                .setTitle("确定要一键续借?")
                .setMessage("每本书最多续借两次，续借可延期至今天后一个月，珍惜机会...")
                .setPositiveButton("我就要续借", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renewBooks();
                    }
                })
                .setNegativeButton("算了算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    });

    /**
     * 超出3本书的缩略 分页加载
     */
    public final ReplyCommand loadMore = new ReplyCommand(this::loadMoreBooks);
    private final List<ViewModel> moreBookContainer = new ArrayList<>();
    public ObservableBoolean isExpanded = new ObservableBoolean(false);
    public final ObservableField<String> loadMoreBtnMsg = new ObservableField<>("暂无更多书");
    public final ObservableBoolean loadMoreBtnClickable = new ObservableBoolean(false);
    private String cacheStr = "";

    //对应barcode和book做查询
    public final HashMap<String, Book> bookHashMap = new HashMap<>();

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
        //先把当前的那个button的字符串存下来
        if (!loadMoreBtnMsg.get().equals("收缩")) {
            cacheStr = loadMoreBtnMsg.get();
        }
        if (moreBookContainer.size() != 0 && !isExpanded.get()) {
            viewModels.addAll(moreBookContainer);
            isExpanded.set(true);
            loadMoreBtnMsg.set("收缩");
        } else if (moreBookContainer.size() != 0 && isExpanded.get()) {
            /**
             * 这个封装真是尼玛骚
             * 需要我进行add操作时候才会刷新列表
             * 是因为ObserveableList的源码不支持removeall的提示
             */
//            viewModels.removeAll(moreBookContainer);
            for (ViewModel viewModel : moreBookContainer) {
                viewModels.remove(viewModel);
            }

            List<ViewModel> container = new ArrayList<>();
            for (ViewModel viewModel : viewModels) {
                container.add(viewModel);
            }

            isExpanded.set(false);
            loadMoreBtnMsg.set(cacheStr);
        }
    }

    public void refreshInfo() {
        state.set(1);
        libProvider.getUserInfo(info -> {
            state.set(0);
            //添加当前书列表
            if (null == info.books || info.books.size() == 0) {
                haveBooks.set(false);
                message.set("还没有从图书馆借书呢");
            } else {
                message.set("您一共借了" + info.books.size() + "本书");
            }
            viewModels.clear();
            moreBookContainer.clear();

            if (info.books != null) {
                for (Book book : info.books) {
                    bookHashMap.put(book.barcode, book);
                }
                //分页加载
                if (info.books.size() <= 3) {
                    for (Book book : info.books) {
                        viewModels.add(new BookItemViewModel(mContext, book));
                    }
                    loadMoreBtnMsg.set("无更多书显示");
                    loadMoreBtnClickable.set(false);
                } else {
                    for (int i = 0; i < info.books.size(); i++) {
                        if (i < 3) {
                            viewModels.add(new BookItemViewModel(mContext, info.books.get(i)));
                        } else {
                            moreBookContainer.add(new BookItemViewModel(mContext, info.books.get(i)));
                        }
                    }
                    loadMoreBtnClickable.set(true);
                    loadMoreBtnMsg.set("显示更多(" + (info.books.size() - 3) + ")");
                }

            }

        },throwable -> {
            //错误处理时候的卡片显示状况
            if (throwable instanceof HttpException) {
                HttpException exception = (HttpException) throwable;
                try {
                    String errorJson = exception.response().errorBody().string();
                    JSONObject errJsonObject = new JSONObject(errorJson);
                    int errcode = errJsonObject.getInt("error_code");
                    String errmessage = errJsonObject.getString("message");
                    state.set(2);
                    this.message.set(errmessage);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }else if (throwable instanceof SocketTimeoutException){
                state.set(2);
                this.message.set("网络超时...很绝望");
            }else {
                state.set(2);
                this.message.set("粗线错误了啊,我也很绝望我能怎么办...");
            }
        });
    }

    public void renewBooks() {
        state.set(1);
        libProvider.renewAllBooks(renewResults -> {
            state.set(0);
            message.set("续借操作完成");
            StringBuilder stringBuilder = new StringBuilder();
            for (RenewResult renewResult : renewResults) {
                if (renewResult.error == 1) {
                    state.set(2);
                    stringBuilder.append(bookHashMap.get(renewResult.barcode).title).append(" : ").append("续借次数超过两次，请归还重新借阅\n");
                }
            }
            stringBuilder.append("续借完成\n");
            String s = stringBuilder.toString();
            refreshInfo();
            //调用alerter
            Alerter.create((Activity) mContext)
                    .setTitle("续借操作完成")
                    .setText(s)
                    .show();
        });
    }


}
