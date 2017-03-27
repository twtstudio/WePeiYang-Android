package course.labs.classroomquery;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Administrator on 2017/2/19.
 * @deprecated 模块不应该加application
 *
 */

public class ClassroomApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
    }
}
