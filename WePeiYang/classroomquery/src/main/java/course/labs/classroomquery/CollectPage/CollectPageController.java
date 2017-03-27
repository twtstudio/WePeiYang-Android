package course.labs.classroomquery.CollectPage;

import java.util.List;

import course.labs.classroomquery.Model.ClassroomBean;

/**
 * Created by asus on 2017/1/29.
 */
public interface CollectPageController {
    void onClassroomsReceive(List<ClassroomBean> classroomBeen);

    void onError();
}
