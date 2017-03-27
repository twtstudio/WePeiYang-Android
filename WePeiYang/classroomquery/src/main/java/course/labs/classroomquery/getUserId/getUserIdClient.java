package course.labs.classroomquery.getUserId;

import com.orhanobut.logger.Logger;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import course.labs.classroomquery.API.APIReaponse;

import course.labs.classroomquery.Model.CollectedRoom2;
import course.labs.classroomquery.Model.FreeRoom2;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by Administrator on 2017/2/26.
 */

public class getUserIdClient {
    private Retrofit mRetrofit;



    protected Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();



    private getIdtApi mService;
    public getUserIdClient(){
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

                .baseUrl("http://open.twtstudio.com/api/")

                .client(client)

                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                .addConverterFactory(GsonConverterFactory.create())

                .build();



        mService = mRetrofit.create(getIdtApi.class);
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

   public void getUserId(Object tag,Subscriber subscriber,String token){
       Subscription subscription = mService.getUserId(token)
                                             .subscribeOn(Schedulers.io())
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe(subscriber);
       addSubscription(tag,subscription);
   }
}
