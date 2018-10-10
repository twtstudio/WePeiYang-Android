package com.twt.service.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.twtstudio.retrox.schedule.model.ClassTable;

import java.util.ArrayList;

/**
 * Created by retrox on 26/03/2017.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        Hawk.put("scheduleCache",list);
        Logger.d(Hawk.get("scheduleCache2"));
        return new WidgetListFactory(this.getApplicationContext(), Hawk.get("scheduleCache2"));
    }
}
