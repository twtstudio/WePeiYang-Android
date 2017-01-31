package com.twtstudio.retrox.wepeiyangrd.home.user;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.twtstudio.retrox.wepeiyangrd.R;

/**
 * Created by retrox on 2017/1/14.
 */

public class CommonItemViewModel implements ViewModel {

    public static final int MESSAGE = 0;
    public static final int COLLECTION = 1;
    public static final int RECORD = 2;

    private Context mContext;

    private int commandId = 0;

    public final ObservableInt num = new ObservableInt(0);

    //the image resources of this item
    public final ObservableInt imageRes = new ObservableInt(0);

    //the title of this item
    public final ObservableField<String> title = new ObservableField<>();

    public final ReplyCommand clickCommand = new ReplyCommand(this::clickItem);

    public ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableInt textColor = new ObservableInt(0);
    }

    public CommonItemViewModel(Context context, int commandId) {
        mContext = context;
        this.commandId = commandId;
        init();
    }

    private void init(){
        if (commandId == MESSAGE){
            imageRes.set(R.drawable.ic_message);
            title.set("我的消息");
        }else if (commandId == COLLECTION){
            imageRes.set(R.drawable.ic_collection);
            title.set("我的收藏");
        }else if (commandId == RECORD){
            imageRes.set(R.drawable.ic_browsing_history);
            title.set("历史记录");
        }
        excuteNetWorkData();
    }

    private void clickItem(){
        switch (commandId){
            case MESSAGE:
                // TODO: 2017/1/14 jump to message
                break;
            case COLLECTION:
                // TODO: 2017/1/14 jump to collcetion
                break;
            case RECORD:
                // TODO: 2017/1/14 jump to record
                break;
        }
    }

    private void excuteNetWorkData(){
        // TODO: 2017/1/14 rxjava send api for the numbers of each item
    }

}
