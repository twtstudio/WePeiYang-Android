package course.labs.classroomquery.API;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import course.labs.classroomquery.Model.CollectedRoom2;
import course.labs.classroomquery.Model.FreeRoom2;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by asus on 2017/1/23.
 */
public class apiClient{
    protected Retrofit mRetrofit;



    protected Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();



    private api mService;
    public apiClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            if (message.startsWith("{")){
                Logger.json(message);
            }else {
                Platform.get().log(INFO, message, null);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);




        OkHttpClient client = new OkHttpClient.Builder()

                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)

                .connectTimeout(30, TimeUnit.SECONDS)

                .build();





        mRetrofit = new Retrofit.Builder()

                .baseUrl("http://open.twtstudio.com/api/v1/")

                .client(client)

                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                .addConverterFactory(GsonConverterFactory.create())

                .build();



        mService = mRetrofit.create(api.class);
    }
    /**

     * 取消订阅关系，类似与cancel网络请求，操作在presenter的destroy方法里面被调用

     *

     * @param tag 传入presenter的实例

     */
    public void unSubscribe(Object tag) {

        if (mSubscriptionsMap.containsKey(tag)) {

            CompositeSubscription subscriptions = mSubscriptionsMap.get(tag);

            subscriptions.unsubscribe();

            mSubscriptionsMap.remove(tag);

        }

    }
    /**

     * 添加订阅关系，同时rxjava自动发起网络请求Su

     *

     * @param tag          presenter的实例

     * @param subscription 创建好的订阅关系

     */

    protected void addSubscription(Object tag, Subscription subscription) {

        if (tag == null) {

            return;

        }

        CompositeSubscription subscriptions;

        if (mSubscriptionsMap.containsKey(tag)) {

            subscriptions = mSubscriptionsMap.get(tag);

        } else {

            subscriptions = new CompositeSubscription();

        }

        subscriptions.add(subscription);

        mSubscriptionsMap.put(tag, subscriptions);

    }
    public void getAllCollectClassroom(Object tag,Subscriber subscriber,String token,int week){
        //TODO:用学号传参

        Subscription subscription = mService.getAllCollectedClassroom(CommonPrefUtil.getStudentNumber(),week)
                                               .map(new Func1<CollectedRoom2, List<CollectedRoom2.CollectedRoom>>() {
                                                  @Override

                                                  public List<CollectedRoom2.CollectedRoom> call(CollectedRoom2 room){
                                                      return  room.getData();
                                                  }
                                              })
                                              .subscribeOn(Schedulers.io())

                                              .observeOn(AndroidSchedulers.mainThread())
                                              .subscribe(subscriber);
        addSubscription(tag,subscription);
    }
    public void getFreeClassroom(Object tag, Subscriber subscriber, int building, int week, int time,String token){
        if(CommonPrefUtil.getToken()==null){
            System.out.println("Token is null");
        }
        else{
            //System.out.println("Token is not null");
            System.out.println("Token是"+CommonPrefUtil.getToken());
        }
        System.out.println("请求信息"+building+":"+week+":"+time+":");
        System.out.println("学号是"+CommonPrefUtil.getStudentNumber());
        //TODO:用学号传参
        Subscription subscription = mService.getFreeClassroom(building,week,time,CommonPrefUtil.getStudentNumber())
                                              .map(new Func1<FreeRoom2, List<FreeRoom2.FreeRoom>>() {
                                                  @Override
                                                  public List<FreeRoom2.FreeRoom> call(FreeRoom2 room){
                                                      return  room.getData();
                                                  }
                                              })
                                              .subscribeOn(Schedulers.io())

                                              .observeOn(AndroidSchedulers.mainThread())
                                              .unsubscribeOn(Schedulers.io())
                                              .subscribe(subscriber);

        addSubscription(tag, subscription);
    }
    public void collect(Object tag, Subscriber subscriber,String building,String token){
        //TODO:用学号传参
        if(subscriber!=null){
            Subscription subscription = mService.collect(building,CommonPrefUtil.getStudentNumber())

                                                 .subscribeOn(Schedulers.io())
                                                 .observeOn(AndroidSchedulers.mainThread())
                                                 .unsubscribeOn(Schedulers.io())
                                                 .subscribe(subscriber);
            addSubscription(tag,subscription);
        }
    }
    public void cancelCollect(Object tag,Subscriber subscriber,String token,String building){
        //TODO:用学号传参
        if(subscriber!=null){
            Subscription subscription = mService.cancelCollect(CommonPrefUtil.getStudentNumber(),building)
                                                  .map(new Func1<APIReaponse, Integer>() {
                                                      @Override
                                                      public Integer call(APIReaponse response){
                                                          if(response.getData() instanceof Integer) {
                                                              return  (Integer)response.getData();
                                                          }
                                                          else{
                                                              return 0;
                                                          }
                                                      }
                                                  })

                                                  .subscribeOn(Schedulers.io())
                                                  .observeOn(AndroidSchedulers.mainThread())
                                                   .unsubscribeOn(Schedulers.io())
                                                  .subscribe(subscriber);
            addSubscription(tag,subscription);
        }
    }
}
