# WePeiYang v3.0

WePeiYang Redesign and Refactor



## 注意事项

> 如果你依然要在旧版本上开发，checkout到 v2.2.1 branch
>
> 新版本模块化开发，checkout到 v3.0 branch （强烈建议，避免你在上线前遇到的架构不兼容问题）
>
> 模块化开发的初期可以做在一个新的app里面，依赖commons库，向外提供必要的provider数据接口即可
>
> 注意资源命名方式：eg. Gpa模块里面的资源需要加前缀:   "gpa_"  
>
> eg. gpa_item_chart     gpa_activity_main



## 模块化

> **每个功能模块独立开发，共同依赖基础库commons**
>
> **自己开发的模块内可以使用自己喜欢的架构和依赖，没有架构也可以，开心就好**
>
> **每个模块统一提供Provider模式的对外数据接口**
>
> **使用RxJava的Action1接口封装，更好的适配lamdba表达式**
>
> 实例代码：

```java
/**
 * Created by retrox on 2017/1/17.
 * 每个模块的对外暴露的数据提供接口，同级无依赖的module无法调用，考虑router统一调用，暂不做实现
 * 上层依赖模块可以用普通方法调用
 * 包含一个数据的回调，使用RxJava自带的Action接口实现
 * 兼容lambda表达式
 * 内部也可以用
 */

public class GpaProvider {

    public static final String TOKEN_GPA_LOAD_FINISHED = "token_gpa_load_finished";

    //绑定生命周期用
    private RxAppCompatActivity mActivity;

    private Action1<GpaBean> action;

    private GpaProvider(RxAppCompatActivity activity) {
        mActivity = activity;
    }

    /**
     * default: not refresh the cache
     */
    public void getData() {
        getData(false);
    }

    /**
     * get from cache or network
     * @param update get from network and update the cache?
     */
    public void getData(boolean update) {
        GpaCacheProvider gpaCacheProvider = CacheProvider.getRxCache()
                .using(GpaCacheProvider.class);

        gpaCacheProvider.getGpaAuto(RetrofitProvider.getRetrofit()
                .create(GpaApi.class)
                .getGpa(), new DynamicKey("gpa"), new EvictDynamicKey(update))
                .subscribeOn(Schedulers.io())
                .doOnNext(gpaBeanReply -> Logger.d(gpaBeanReply.toString()))
                .map(Reply::getData)
                .map(MyGpaBean::getData)
                .compose(mActivity.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gpaBean -> {
                    //提供模块内的刷新服务，因为数据的bus是不能跨module的
                    Messenger.getDefault().send(gpaBean, TOKEN_GPA_LOAD_FINISHED);
                    if (action != null) {
                        action.call(gpaBean);
                    }
                }, new RxErrorHandler(mActivity));

    }

    public static GpaProvider init(RxAppCompatActivity rxActivity) {
        return new GpaProvider(rxActivity);
    }

    public GpaProvider registerAction(Action1<GpaBean> action) {
        this.action = action;
        return this;
    }

}

```

> 调用示例（lambda）

```java
GpaProvider.init(mContext)
                .registerAction(observableGpa::set)
                .getData();
```

> 数据提供接口设计：
>
> 不必提供模块内所有数据，只需提供首页feed流中需要的数据还要其他的概括性数据

> 模块间的相互调用：
>
> 上层模块依赖与下层模块，可以直接调用，activity跳转直接使用Intent即可
>
> 下层调用上层或者同级模块相互跳转，需要使用反射

```java
                    Class clazz = null;
                    try {
                        clazz = Class.forName("com.twtstudio.retrox.wepeiyangrd.home.HomeActivity");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(mActivity, clazz);
                    mActivity.startActivity(intent);
```

> **暂不引入Router框架**
>
> 将来可能会使用。

> 一些不错的参考资料：
>
> [**Android架构思考(模块化、多进程)**](http://blog.spinytech.com/2016/12/28/android_modularization/)
>
> [**Android业务组件化开发实践**](http://kymjs.com/code/2016/10/18/01/)



## 基础库 Commons

> **注意：引用基础库的时候，确保项目module的gradle开启了databinding，否则会出现编译错误**
>
> 功能：
>
> 封装了auth模块，直接使用其中的activity进行跳转
>
> 封装网络层，RetrofitProvider封装了签名验证机制
>
> 封装了Rx风格的三级缓存框架
>
> 网络异常处理的ErrorHandler（这个用不用随你喽）
>
> 网络层的具体情况要看后台的情况，目前的封装只针对于规范API，后台不规范的话可在模块内自己配置RetrofitClient和签名拦截器
>
> 封装APP类，通过反射在非app模块获取Application示例
>
> 封装公共的Prefences模块，存放Token及一些公共数据



> 网络封装使用示例：
>
> 传入改模块的API接口类即可

```java
 Observable<Notification<GpaBean>> gpaObservable =
                RetrofitProvider.getRetrofit()
                        .create(GpaApi.class)
                        .getGpa()
                        .subscribeOn(Schedulers.io())
                        .compose(mActivity.bindToLifecycle())
                        .map(ApiResponse::getData)
                        //......then do something you like
```



## 微北洋3.0 全新设计 MVVM篇

-   架构：MVVM + RxJava

### 依赖：

 > fragmentation —— 处理fragment的各种坑
 >
 > mvvmkit — — 处理databinding的支持框架，并且进行功能的添加和修改
 >
 > Rxlifecycle —— 处理rx绑定时候的生命周期

### 推荐阅读

> Library：[MvvmLight框架地址](https://github.com/Kelin-Hong/MVVMLight)
>
> 中文文档：[**MVVM Light Toolkit使用指南**](http://www.jianshu.com/p/43ea7a531700)
>
> **引申阅读：**[**如何构建Android MVVM应用程序**](http://www.jianshu.com/p/2fc41a310f79)

### 架构设计--使用场景：

1. adapter模式：

   > layout是绑定的layout，具体寻找学习资料
   >
   > 父目录ViewModel —— layout
   >
   > 适配器itemViewModel —— layout

   ```java
   public class ToolItemViewModel implements ViewModel {

       private Context mContext;

       private Class<? extends BaseActivity> targetAct;

       public final ObservableInt iconRes = new ObservableInt();

       public final ObservableField<String> title = new ObservableField<>();

       public final ReplyCommand clickCommand = new ReplyCommand(this::jump);

       public ToolItemViewModel(Context context, int iconres, String title, Class<? extends BaseActivity> activityClass) {
           mContext = context;
           this.iconRes.set(iconres);
           this.title.set(title);
           this.targetAct = activityClass;
       }

       private void jump(){
           Intent intent = new Intent(mContext,targetAct);
           mContext.startActivity(intent);
       }

   }
   ```

   > Item 所绑定的layout代码：

   ```Xml
   <layout xmlns:android="http://schemas.android.com/apk/res/android"
           xmlns:bind="http://schemas.android.com/apk/res-auto">
       <data>
           <variable
               name="viewModel"
               type="com.twtstudio.retrox.wepeiyangrd.home.tools.ToolItemViewModel"/>
       </data>
       <LinearLayout
           android:layout_margin="5dp"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:orientation="vertical"
           bind:clickCommand="@{viewModel.clickCommand}">

           <ImageView
               android:layout_width="match_parent"
               android:layout_height="70dp"
               bind:placeholderImageRes="@{viewModel.iconRes}"
               android:layout_gravity="center_horizontal"
               />
           <TextView
               android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:layout_gravity="center_horizontal"
               android:layout_marginTop="3dp"
               android:text="@{viewModel.title}"/>

       </LinearLayout>
   </layout>

   ```

   > 绑定：运用开源库  **[BindingCollectionAdapter](https://github.com/evant/binding-collection-adapter)** 可以查阅这里的文档

   > **根布局的viewmodel：**

   ```java
   public class ToolsFragViewModel implements ViewModel {
       private Context mContext;

       public final ObservableArrayList<ToolItemViewModel> itemList = new ObservableArrayList<>();

       public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_tool);

       public ToolsFragViewModel(Context context) {
           mContext = context;
           init();
       }

       private void init(){
           itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_schedule,"课程表", MainActivity.class));
           itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_gpa,"GPA", MainActivity.class));
           itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_bike,"哲学车", MainActivity.class));
           itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_party,"党建", MainActivity.class));
           itemList.add(new ToolItemViewModel(mContext,R.drawable.ic_main_read,"图书馆", MainActivity.class));
           // TODO: 2017/1/15 修改跳转的activity
       }
   }

   ```

   > RecyclerView的绑定

   ```xml
   <android.support.v7.widget.RecyclerView
               android:id="@+id/recyclerView"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               bind:layoutManager="@{LayoutManagers.linear()}"
               bind:itemView="@{viewmodel.itemView}"
               bind:items="@{viewmodel.list}"/>
   ```



2. 普通视图模式：

   > 布局viewmodel — — layout 即可

3. 事件绑定

   > xml的控件里面绑定ReplyCommand ——— 事件绑定
   >
   > 推荐java的viewmodel中使用lambda表达式写

   ```java
   public final ReplyCommand<Integer> onLoadMoreCommand =  new ReplyCommand<>((itemCount) -> { 
    int page=itemCount/LIMIT+1; 
    loadData(page.LIMIT)
   });
   ```

   > Sample: RecyclerView 绑定加载更多的命令（已封装）

   ```xml
   <android.support.v7.widget.RecyclerView 
   android:layout_width="match_parent"  
   android:layout_height="match_parent"  
   bind:onLoadMoreCommand="@{viewModel.loadMoreCommand}"/>
   ```

   > SwipeRefreshLayout的下拉刷新触发的指令

   ```xml
   <android.support.v4.widget.SwipeRefreshLayout
           android:id="@+id/swipe_refresh_layout"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:layout_gravity="fill_vertical"
           app:layout_behavior="@string/appbar_scrolling_view_behavior"
           app:onRefreshCommand="@{viewModel.onRefreshCommand}"
           app:setRefreshing="@{viewModel.viewStyle.isRefreshing}">
   ```

   > 点击的触发指令

   ```xml
   <RelativeLayout
           xmlns:android="http://schemas.android.com/apk/res/android"
           android:layout_width="match_parent"
           android:layout_height="60dp"
           android:padding="12dp"
           bind:clickCommand="@{viewModel.clickCommand}">
   ```

4. ViewModel层的数据获取设计（Rx）

   > 使用Rxjava处理复杂逻辑

   ```java
   public class OneInfoViewModel implements ViewModel {

       /**
        * 用于绑定fragment的生命周期，还有context的提供
        */
       private BaseFragment mFragment;

       /**
        * fields 与UI绑定的可变数据源
        */
       public final ObservableField<String> imageUrl = new ObservableField<>();

       public final ObservableField<String> content = new ObservableField<>();

       public final ObservableField<String> author = new ObservableField<>();

       public static final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

       public OneInfoViewModel(BaseFragment fragment) {
           mFragment = fragment;
           getData();
       }

       /**
        * Rx 风格的数据请求
        */
       private void getData() {
           Observable<Notification<OneInfoBean>> oneInfoOb =
                   Observable.just(Calendar.getInstance())
                           .subscribeOn(Schedulers.io())
                           .map(Calendar::getTime)
                           .map(dateFormate::format)
                           .flatMap(s -> ApiClient.getService().getOneHpInfo(s))
                           .compose(mFragment.bindToLifecycle())
                           .materialize().share();

           oneInfoOb.filter(Notification::isOnNext)
                   .map(Notification::getValue)
                   .map(oneInfoBean -> oneInfoBean.hpEntity)
                   .doOnNext(hpEntityBean -> {
                       imageUrl.set(hpEntityBean.strOriginalImgUrl);
                       content.set(hpEntityBean.strContent);
                       author.set(hpEntityBean.strAuthor);
                   })
                   .subscribe();

       }
   }

   ```

5. ViewModel层的单独测试，viewmodel对view是解耦的，所以可以单独测试viewmodel的功能模块

   > Sample : ViewModel Rxjava Network Request Test —— >_<

   ```java
   private void test1(){
           BaseFragment fragment = new BaseFragment();
           OneInfoViewModel viewModel = new OneInfoViewModel(fragment);
       }
   ```

   ​



## 友盟分享使用

具体调用方法参见：[友盟官方文档](http://dev.umeng.com/social/android/share-detail)



### 注意事项：

1. **最后在分享所在的Activity里复写onActivityResult方法,注意不可在fragment中实现，如果在fragment中调用分享，在fragment依赖的Activity中实现，如果不实现onActivityResult方法，会导致分享或回调无法正常进行**

```java
@Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

```

2. 在实例化ShareAction时setCallBack()方法推荐传入Common包中的**ShareListener**

   1. 不需要自定义回调

   ```java
   new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
        .withMedia(web)
        .setCallback(new ShareListener(MainActivity.this))
        .share();
   ```

   2. 自定义回调时（ShareListener的构造方法可以传入UMShareListener作为第二个参数）

   ```java
   new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
           .withMedia(web)
           .setCallback(new ShareListener(MainActivity.this, new UMShareListener() {
               @Override
               public void onStart(SHARE_MEDIA share_media) {

               }

               @Override
               public void onResult(SHARE_MEDIA share_media) {

               }

               @Override
               public void onError(SHARE_MEDIA share_media, Throwable throwable) {

               }

               @Override
               public void onCancel(SHARE_MEDIA share_media) {

               }
           }))
           .share();
   ```

3. QQ分享，不允许只分享文字信息。



## 在单独开发某个模块时

需要引入Commons模块并对工程进行一些配置

[根据官方文档配置](http://dev.umeng.com/social/android/quick-integration#3)【只需要看3.1.4 —— 3.1.6】

发起分享时与平时相同。