package course.labs.classroomquery;

/**
 * Created by Administrator on 2017/2/7.
 * @deprecated
 */

public class ClassroomProvider {
    /* public static final String TOKEN_CLASSROOM_LOAD_FINISHED = "token_classroom_load_finished";
     private RxAppCompatActivity mActivity;
     private Action1<ClassroomBean> action;
     public ClassroomProvider(RxAppCompatActivity activity){
         mActivity  = activity;
     }
    public void getDate(){
        Observable<Notification<CollectedRoom>> gpaObservable = RetrofitProvider.getRetrofit()
                                                                                .create(api.class)
                                                                                .getFreeClassroom()
                                                                                .subscribeOn(Schedulers.io())
                                                                                .compose(mActivity.bindToLifecycle())
                                                                                .map(ApiResponse::getData)
                                                                                 .materialize().share();
        gpaObservable.filter(Notification::isOnNext).map(Notification::getValue)
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                     .subscribe(classroomBean->{
                                                         Messenger.getDefault().send(classroomBean,TOKEN_CLASSROOM_LOAD_FINISHED);
                                                         if (action != null) {
                                                             action.call(new ClassroomBean(classroomBean));
                                                         }
                                                     });


        ApiErrorHandler handler = new ApiErrorHandler(mActivity);

        handler.handleError(gpaObservable.filter(Notification::isOnError)
                .map(Notification::getThrowable));
    }
    public static GpaProvider init(RxAppCompatActivity rxActivity){
        return new GpaProvider(rxActivity);
    }

    public GpaProvider registerAction(Action1<GpaBean> action){
        this.action = action;
        return this;
    }*/

}
