package course.labs.classroomquery.CollectPage;

import android.content.Context;
import android.util.Log;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.util.ArrayList;
import java.util.List;

import course.labs.classroomquery.API.apiClient;
import course.labs.classroomquery.API.apisubscriber;
import course.labs.classroomquery.API.onErrorListener;
import course.labs.classroomquery.API.onNextListener;
import course.labs.classroomquery.Model.ClassroomBean;
import course.labs.classroomquery.Model.CollectedRoom2;
import course.labs.classroomquery.homePage.homePagePresenter;

/**
 * Created by asus on 2017/1/29.
 */
public class CollectPagePresenter {
    private Context mContext;
    CollectPageController mController;
    private List<ClassroomBean>  mCclassrooms;
    public CollectPagePresenter(Context context,CollectPageController controller){
        this.mContext = context;
        this.mController = controller;
        mCclassrooms = new ArrayList<ClassroomBean>();

    }
    public void getClassrooms(){
        final List<ClassroomBean> allClassroomBean = new ArrayList<ClassroomBean>();
        onErrorListener mErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {
                //mAdapter.clear();

                //textView.setVisibility(View.VISIBLE);
                mController.onError();
            }
        };
        onNextListener<List<CollectedRoom2.CollectedRoom>> mNextListener = new onNextListener<List<CollectedRoom2.CollectedRoom>>() {
            @Override
            public void OnNext(List<CollectedRoom2.CollectedRoom> classrooms) {


                if(classrooms!=null) {
                   // textView.setVisibility(View.GONE);
                    for(CollectedRoom2.CollectedRoom mroom:classrooms){
                        mCclassrooms.add(new ClassroomBean(mroom));
                    }
                }
                else{
                    Log.i("网络","数据无返回");
                }
                mController.onClassroomsReceive(mCclassrooms);
            }
        };
        apisubscriber subscriber = new apisubscriber(mContext,mNextListener,mErrorListener);
        apiClient client = new apiClient();
        client.getAllCollectClassroom(this,subscriber,CommonPrefUtil.getToken(),new homePagePresenter().getWeek());



    }

}
