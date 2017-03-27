package course.labs.classroomquery.homePage;

import java.util.Set;

import course.labs.classroomquery.Model.ClassroomBean;

/**
 * Created by asus on 2017/1/29.
 */
public interface homePageController {
    void onNowClassroomReceived(Set<ClassroomBean> classroomBeen);

    void onError(int build);
}
