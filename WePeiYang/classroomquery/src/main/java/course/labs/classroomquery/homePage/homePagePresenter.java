package course.labs.classroomquery.homePage;

import android.content.Context;
import android.util.Log;

import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import course.labs.classroomquery.API.apiClient;
import course.labs.classroomquery.API.apisubscriber;
import course.labs.classroomquery.API.onErrorListener;
import course.labs.classroomquery.API.onNextListener;
import course.labs.classroomquery.Model.ClassroomBean;
import course.labs.classroomquery.Model.FreeRoom2;
import course.labs.classroomquery.common.RecyclerAdapter;

/**
 * Created by asus on 2017/1/29.
 */
public class homePagePresenter {
    private Set<Integer> condition1_buiding;
    private Set<Integer> condition2_time;
    private RecyclerAdapter mAdapter;
    private Context mContext;
    private homePageController mController;
    private Set<ClassroomBean> curClassroomBeen;
    private Set<ClassroomBean> nextClassroomBeen;
    private boolean checkState3[];
    private final int TERM_START_YEAR = 2017;
    private final int TERM_START_MONTH = 2;
    private final int TERM_START_DAY = 20;
    public homePagePresenter(){

    }
   public homePagePresenter(Context context,homePageController controller){
       this.mContext = context;
        mController = controller;
        condition1_buiding = new HashSet<Integer>();
        condition2_time = new HashSet<Integer>();
       curClassroomBeen = new HashSet<>();
       nextClassroomBeen = new HashSet<>();
   }
    public void getCurClassrooms(int build,int time){
        this.condition1_buiding = condition1_buiding;
        this.condition2_time = condition2_time;
        this.checkState3 = checkState3;

        onErrorListener mErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {

                mController.onError(build);

            }
        };
        onNextListener<List<FreeRoom2.FreeRoom>> mNextListener = new onNextListener<List<FreeRoom2.FreeRoom>>() {
            @Override
            public void OnNext(List<FreeRoom2.FreeRoom> classrooms) {



                if(classrooms!=null) {
                   synchronized (this) {
                       Log.i("Presenter", "返回数据不为空");
                       curClassroomBeen.clear();
                       for (FreeRoom2.FreeRoom mroom : classrooms) {
                           if(!mroom.getState().equals("上课中")) {
                               ClassroomBean classroomBean = new ClassroomBean(mroom);
                               classroomBean.setBuilding(build);
                               curClassroomBeen.add(classroomBean);
                           }
                       }
                       mController.onNowClassroomReceived(curClassroomBeen);
                   }
                }
                else{
                    mController.onNowClassroomReceived(curClassroomBeen);
                    Log.i("网络","数据无返回");
                }
            }
        };





           apisubscriber subscriber = new apisubscriber(mContext, mNextListener, mErrorListener);


           (new apiClient()).getFreeClassroom(this, subscriber, build, getWeek(), time, CommonPrefUtil.getToken());




    }
  /*  public void getNextClassrooms(int build, int time){
        this.condition1_buiding = condition1_buiding;
        this.condition2_time = condition2_time;
        onErrorListener mErrorListener = new onErrorListener() {
            @Override
            public void OnError(Throwable e) {
                //mAdapter.clear();
                //swipeRefreshLayout.setRefreshing(false);
                mController.onError();
                //wrongTextview.setVisibility(View.VISIBLE);
            }
        };
        onNextListener<List<FreeRoom>> mNextListener2 = new onNextListener<List<FreeRoom>>() {
            @Override
            public void OnNext(List<FreeRoom> classrooms) {
                if(nextClassroomBeen ==null){
                    Log.i("curClassroom","数据无");
                    return;
                }
                if(classrooms!=null) {
                    for(FreeRoom mroom:classrooms){
                        nextClassroomBeen.add(new ClassroomBean(mroom));
                    }
                }
                else{
                    Log.i("网络","数据无返回");
                }
            }
        };


                nextClassroomBeen.clear();


                apisubscriber subscriber2 = new apisubscriber(mContext,mNextListener2,mErrorListener);


                (new apiClient()).getFreeClassroom(this,subscriber2,build,getWeek(),getDay(),time+2);



    }*/

    public int getWeek(){
        final Calendar c= Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTimeInMillis(System.currentTimeMillis());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        int totalDays = 0;
        System.out.println("时间"+year+"/"+month+"/"+day);
        if(year>TERM_START_YEAR){
            totalDays+=getMonthDay(TERM_START_MONTH)-TERM_START_DAY+1;
            for(int i = TERM_START_MONTH+1;i<=12;i++){
                totalDays+=getMonthDay(i);
            }
            for(int i = 1;i<month;i++) {
                totalDays += getMonthDay(i);
            }
            totalDays+=day;
            if(totalDays%7==0&&totalDays>0){
                return totalDays/7;
            }
            else{
                return totalDays/7+1;
            }
        }
        if(year==TERM_START_YEAR&&(month>TERM_START_MONTH||(month==TERM_START_MONTH&&day>=TERM_START_DAY))){
            totalDays+=getMonthDay(TERM_START_MONTH)-TERM_START_DAY+1;
            for(int i =TERM_START_MONTH+1;i < month;i++){
                totalDays+=getMonthDay(i);
            }
            totalDays+=day;
            if(totalDays%7==0&&totalDays>0){
                return totalDays/7;
            }
            else{
                return totalDays/7+1;
            }
        }
        return  9;
    }
    public int getDay(){
        final Calendar c= Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTimeInMillis(System.currentTimeMillis());
        return  c.get(Calendar.DAY_OF_WEEK)-1;
    }
    public int getMonthDay(int month){
        Date date = new Date(System.currentTimeMillis());
        int year = date.getYear();
        switch (month){
            case 1:return 31;
            case 2:if((year%4==0&&year%100!=0)||year%400==0){
                    return 29;
                   }
                   else{
                      return 28;
                   }
            case 3:return 31;
            case 4:return 30;
            case 5:return 31;
            case 6:return 30;
            case 7:return  31;
            case 8:return  31;
            case 9:return  30;
            case 10:return  31;
            case 11:return  30;
            case 12:return  31;
        }
        return  0;
    }
    /*for(ClassroomBean classroom:curClassroomBeen){
        if(nextClassroomBeen.contains(classroom)){
            classroom.setWillBeOccupied(false);
        }
        else{
            classroom.setWillBeOccupied(true);
        }
    }
    shaixuan();
    mController.onClassroomReceived(curClassroomBeen);*/
}
