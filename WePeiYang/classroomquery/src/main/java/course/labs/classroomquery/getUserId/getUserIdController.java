package course.labs.classroomquery.getUserId;

import course.labs.classroomquery.Model.userId;

/**
 * Created by Administrator on 2017/2/26.
 */

public interface getUserIdController {
    public void onNext(userId id);
    public void onError();
}
