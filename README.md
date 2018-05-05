# WePeiYang v3.0

WePeiYang Redesign and Refactor

# 微北洋开发白皮书

> v1.0.0
> 2018-5-5 
> 微北洋Android

这份文档旨在讲述微北洋的基础架构的设计，以及其用法和新模块的开发流程规范。

### 模块参考

目前代码质量较高的质量有GPA(gpa2)，课程表模块(schedule2)。里面的代码涵盖了基础库的用法，Kotlin的高级使用方式，架构的抽象封装，自定义View之类，吃透两个模块的代码，基本上就没有什么可以担心了。
如果不知道从哪里做起，可以先从GPA的Model层看起，然后一步步追溯到View层，看处理方式。看代码的话，可以用两种方法：自顶向下和自下而上的方法，选用哪一种都OK。

### 基础库研究

内容量最大，在做自己新功能之前所需要了解的就是：`commons`库的用法，进一步也需要多研究研究`commons`的代码设计，阅读完代码一定会受益匪浅。

## 开发指南

> 开发指南从用法的角度来阐述微北洋的基础库

#### 网络请求

网络请求再微北洋中统一使用Retrofit来做，至于Retrofit的用法，比如说接口写法，自行查阅[官方文档](http://square.github.io/retrofit/)

我们需要根据后端组提供的API接口来写对应的interface
一般来讲一个请求的URL是由`BaseUrl + Path`组成。比如说`https://open.twtstudio.com/api/v1/auditClass/audit`这个网络请求，我们规定`BaseUrl = https://open.twtstudio.com/api/`,同时`Path = v1/auditClass/audit`。因此我们的接口就可以这样子写：

```kotlin
interface AuditApi {
    @GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

    @GET("v1/auditClass/popular")
    fun getPopluarAudit(): Deferred<CommonBody<List<AuditPopluar>>>

    @POST("v1/auditClass/audit")
    @FormUrlEncoded
    fun audit(@Field("user_number") userNumber: String, @Field("course_id") courseId: Int, @Field("info_ids") infoIds: String): Deferred<CommonBody<String>>

    @DELETE("v1/auditClass/audit")
    fun cancelAudit(@Query("user_number") userNumber: String, @Query("ids") ids: String): Deferred<CommonBody<String>>

    companion object : AuditApi by ServiceFactory() // 暂时不要管这一行的意思 忽略就好了
}
```

这基本上就是Retrofit的写法，**需要提及一点是接口方法的返回值**
比如说这几个例子中我们的返回值类型是`Deferred<T>`是协程的返回类型(可以`await`那种)，之所以可以用这类型返回值，是因为我们在Retrofit初始化的时候，加入了对应的`CallAdapter`

```kotlin
.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
.addCallAdapterFactory(CoroutineCallAdapterFactory())
//see code: com/twt/wepeiyang/commons/experimental/network/ServiceFactory.kt
```

不添加额外的CallAdapterFactory的话，我们在接口中只能写`Call<T>`的返回值，现在我们可以写`Deferred<T>`,`Observable<T>`的返回值。例如

```kotlin
	@GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Call<CommonBody<List<AuditCourse>>>

    @GET("v1/auditClass/popular")
    fun getPopluarAudit(): Observable<CommonBody<List<AuditPopluar>>>
```

#### 再说泛型包装

先从微北洋的Api返回结构说起，规范的Api返回结构是

```json
{
   "error_code": -1, // 这里是错误码，比如说记录Token过期，办公网绑定错误之类
   "message": "", // 这里是服务器返回的错误Message，比如说 ‘Token已过期’
   "data": <这里是你真正需要的数据>
}
```

对于这种返回格式，我们客户端也做了一套封装来对应

```kotlin 
/**
 * A common wrapper class of respond data from open.twtstudio.com/api.
 *
 * @see AuthService
 */
data class CommonBody<out T>(
        val error_code: Int,
        val message: String,
        val data: T?
)
//所有的微北洋Api规范请求都要用这个东西来做
//see code : com/twt/wepeiyang/commons/experimental/network/ServiceFactory.kt
```

**<强制使用>**因为你真正想要的数据在`data`字段里面，所有你只需要单独写`data`内容对于的`bean`类即可，然后用泛型包装。例如蹭课系统中我的一个请求返回的内容是

```json
{
   "error_code": -1,
   "message": "",
   "data": [
      {
         "course_id": 53,
         "course_name": "数据库原理（双语）",
         "college": "软件学院",
         "semester": "1",
         "year": "2018",
         "infos": [
            {
               "id": 417,
               "course_id": "53",
               "course_name": "数据库原理（双语）",
               "course_id_in_tju": "3665",
               "start_week": "9",
               "end_week": "16",
               "start_time": "1",
               "course_length": "2",
               "week_day": "3",
               "week_type": "3",
               "building": "45",
               "room": "B307",
               "teacher_type": "副教授",
               "teacher": "王征"
            }
         ]
      },
      {
         "course_id": 3,
         "course_name": "有机化学B",
         "college": "理学院",
         "semester": "1",
         "year": "2018",
         "infos": [
            {
               "id": 42,
               "course_id": "3",
               "course_name": "有机化学B",
               "course_id_in_tju": "3171",
               "start_week": "1",
               "end_week": "16",
               "start_time": "2",
               "course_length": "2",
               "week_day": "5",
               "week_type": "3",
               "building": "50",
               "room": "A124",
               "teacher_type": "讲师",
               "teacher": "王元欣"
            }
         ]
      }
   ]
}
```

返回的`data`字段是一个List，因此我们只需要去写这个List内对象的对应类即可

```kotlin
data class AuditCourse(val college: String,
                       @SerializedName("course_id") val courseId: Int,
                       @SerializedName("course_name") val courseName: String,
                       val year: String,
                       val semester: String,
                       val infos: List<InfoItem>)
```

同时在Retrofit的Api里面写

```kotlin
@GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

// 重点：
Deferred<CommonBody<List<AuditCourse>>> //这种情况是CommonBody里面包着List<T>
```

当然Data字段里面不是List就直接泛型包裹即可，比如说办公网课程表那边

```kotlin
    @GET("v1/classtable")
    fun getClassTable(): Deferred<CommonBody<Classtable>>
```

**所以给你讲了那么多，别给我瞎鸡儿写Bean类了，照着规范来**

#### 网络请求Interface怎么用

Retrofit对于网络请求的接口采用了动态代理的机制，commons库中对其进行了封装，具体用法：

- 在类外写

  ```kotlin
  val api: TjuCourseApi by ServiceFactory() // 可以写在一个object里面，单例化
  api.getClassTable()
  ```

- 可以在interface里写一个伴生对象（同时实现该接口，同时被代理）

  ```kotlin
  interface TjuCourseApi {

      @GET("v1/classtable")
      fun getClassTable(): Deferred<CommonBody<Classtable>>

      companion object : TjuCourseApi by ServiceFactory() // 伴生对象实现单例

  }

  TjuCourseApi.getClassTable() // 就可以调用
  ```

**推荐使用第二种**，对应的源码在`com/twt/wepeiyang/commons/experimental/network/ServiceFactory.kt`，如果你想要看懂这些的话，你需要学`Retrofit`,`Kotlin 代理`,`invoke() 运算符重载`

#### Async/await来处理网络请求以及异步任务

目前微北洋里面的异步任务都是使用Kotlin协程来写的。当然如果你实在不会用，你也可以使用

```kotlin
Thread {
    uiThread{
        
    }
}
```

**当然这样子我会Diss你**
作为一个合格的项目开发者，你应该使用Kotlin协程所提供的`async/await`来处理问题。在之前的文档中，我们的网络请求的返回值是`Deferred<T>`类型，至于这是个啥，请自己上网查谢谢。[附一个文章](https://zhuanlan.zhihu.com/p/30019105)
一般的做发是，在普通环境(非协程环境)使用`launch(UI){}`方法启动一个协程，在协程里面使用`async/await`来控制。

```kotlin
launch (exceptionHandler + UI) {
                val result = AuditApi.searchCourse(courseName).await()
                // do something 这里的代码会等待网络请求出结果才执行
        }
```

比如说我们添加一些逻辑

```kotlin
launch (exceptionHandler + UI) {
                val result: CommonBody<String> = AuditApi.searchCourse(courseName).await()
                // do something
                if (result.error_code == -1) textView.text = result.data 
        }
```

**协程里面的上下文**：通用的有`UI` `CommonPool`表示协程运行在哪个线程中，`CommonPool`是一个线程池，耗时操作要在这里完成，然后通过`await`来协调先后。示例一段伪代码（看个意思就行）

```kotlin
launch (exceptionHandler + UI) { // 主线程开启
                val result: CommonBody<String> = AuditApi.searchCourse(courseName).await()
                // do something
                if (result.error_code == -1) textView.text = result.data 
                val dbTask = async(CommonPool) { // 异步开启
                    ScheduleDb.queryAllCourses() // 耗时操作
                }
                val courseList = dbTask.await()
                courseList.foreach {
                    textView.text += it.courseName
                }
        }
```

**这一段看不懂的话，多补补协程。菜是原罪**

到这里，你已经会使用微北洋的基础框架来发起一个网络请求了。请不要另辟蹊径，**谢谢！**
继续阅读下去，你会学到：缓存框架的用法

#### 构建有缓存的响应式网络请求

有一部分的网络请求是需要通过添加缓存来提高用户体验的，比如说新闻列表，课程表之类。微北洋已经对这种常用的缓存进行了封装。 这套封装的架构，整体来讲是基于LiveData来做的，所以如果要彻底了解这些，需要些预备知识：[LiveData链接](https://developer.android.com/topic/libraries/architecture/livedata)

而如果仅仅是使用的话，就非常简单了。

```kotlin
// 这是创建的过程
val GpaLocalCache = Cache.hawk<GpaBean>("GPA") //本地缓存使用Hawk存储
// 远程数据的获取方法：通过Retrofit网络请求 -> 一个map操作只取data
val GpaRemoteCache = Cache.from(GpaService.Companion::get).map(CommonBody<GpaBean>::data) 
// 最后把它们放在一起，RefreshableLiveData来帮你处理剩下的缓存问题
val GpaLiveData = RefreshableLiveData.use(GpaLocalCache, GpaRemoteCache)
```

因为这套缓存系统是基于LiveData的，而LiveData又是响应式的可观测数据流，就很好用。比如说GPA里面使用：

```kotlin
GpaLiveData.bindNonNull(this) {
    // 每次GPALiveData的数据发生变化的时候，这个闭包里面代码就会被回调
    // 回调的时机就是 获得缓存/获得网络数据刷新/手动刷新
    it.stat.total.let {
        scoreTv.text = it.score.toString()
        gpaTv.text = it.gpa.toString()
        creditTv.text = it.credit.toString()
    }

    it.data.asSequence().map(Term::stat).map {
        GpaLineChartView.DataWithDetail(it.score, """
                加权：${it.score}
                绩点：${it.gpa}
                学分：${it.credit}
                """.trimIndent())
    }.toList().let {
        gpaLineCv.dataWithDetail = it
    }

    // attempt to refresh chart view while new data coming
    selectedTermIndex = selectedTermIndex
}
```

如果你可以看懂RefreshableLiveData的源码，你可以看到它的自动刷新时机，Activity/Fragment在Active的时候启动（你可以粗略的理解成`oncreate`或者`onresume`），你也可以手动刷新，甚至加上刷新回调。

```kotlin
fun Context.simpleCallback(success: String? = "加载成功", error: String? = "发生错误", refreshing: String? = "加载中"): suspend (RefreshState<*>) -> Unit =
        with(this.asReference()) {
            {
                when (it) {
                    is RefreshState.Success -> if (success != null) Toasty.success(this(), success).show()
                    is RefreshState.Failure -> if (error != null) Toasty.error(this(), "$error ${it.throwable.message}！${it.javaClass.name}").show()
                    is RefreshState.Refreshing -> if (refreshing != null) Toasty.normal(this(), "$refreshing...").show()
                }
            }
        }

refreshBtn = findViewById<ImageButton>(R.id.btn_refresh).apply {
    setOnClickListener {
        GpaLiveData.refresh(REMOTE, callback = simpleCallback()) // 核心刷新方法
    }
}
```

手动调用`refresh`方法即可手动刷新

```kotlin
auditPopluarLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)
// 后面的部分表示，从哪里刷新 Remote就是远程(服务器)
```

目前的封装已经可以应对大部分常用的网络请求，在缓存逻辑复杂的时候，你也可以通过继承RefreshableLiveData来做这个事情。例如：

```kotlin
// see code : com/twt/service/schedule2/model/audit/AuditApi.kt
val auditCourseLiveData = object : RefreshableLiveData<List<AuditCourse>, CacheIndicator>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<List<AuditCourse>>) {
        super.observe(owner, observer)
        AuditCourseManager.getAuditListLive().observe(owner, observer)
    }

    override fun refresh(vararg indicators: CacheIndicator, callback: suspend (RefreshState<CacheIndicator>) -> Unit) {
        if (indicators == CacheIndicator.REMOTE) {
            async(CommonPool) {
                try {
                    AuditCourseManager.refreshAuditClasstable()
                    callback(RefreshState.Success(CacheIndicator.REMOTE))
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback(RefreshState.Failure(e))
                }
            }
        }
    }

    override fun onActive() {
        refresh(CacheIndicator.REMOTE)
    }

    override fun cancel() {
        // no need to impl
    }

}
```

#### 组织结构

微北洋的模块开发中，不会强制要求采用MVP架构或者MVVM架构之类。在架构的选择上鼓励有效的创新，服从个人喜好。如果你对于项目结构有些拿捏不定的话，可以参考`gpa2`模块或者`schedule2`模块，里面做的还算不错。

一般来讲，Model层是一定要分开的，就是说，如果是网络请求，数据库操作，或者缓存封装之类的话，要单独写开。比如说，把网络请求的interface，网络请求缓存的包装，写在一个kt文件中。把各种bean类的定义（**要用data class**），写在一个kt文件中。把数据库操作写在一个kt文件中。

建议最后暴露给上层（Activity/Presenter）使用的时候，做二次封装，比如说`AuditCourseManager.kt`，建议不要让上层直接操作数据库网络请求之类，尤其是数据库

参考：课程表模块

```kotlin
Schedule2
├── extensions
│   ├── ClassTableExtensions.kt
│   ├── ScheduleNetworkExtensions.kt
│   ├── StringExtensions.kt
│   └── UIExtensions.kt
├── model
│   ├── AbsClasstableProvider.kt
│   ├── CommonClassTable.kt
│   ├── MergedClassTableProvider.kt
│   ├── ScheduleDb.kt
│   ├── SchedulePref.kt
│   ├── TableData.kt
│   ├── audit // 蹭课
│   │   ├── AuditApi.kt // 蹭课Api接口相关
│   │   ├── AuditCourseManager.kt // 暴露给外面的，对蹭课Model做二次封装
│   │   ├── AuditData.kt // 蹭课的数据bean类 / 数据库Dao
│   │   └── AuditPopluar.kt // bean类
│   ├── custom
│   │   ├── CustomCourse.kt
│   │   ├── CustomCourseManager.kt // 暴露给外面的，对自定义Model做二次封装
│   │   └── CustomCourseProvider.kt
│   ├── school
│   │   └── TjuCourse.kt 
│   └── total
│       └── TotalCourseManager.kt // 暴露给外面的，对综合的课程Model做二次封装
└── view
    ├── adapter
    │   ├── CommonItem.kt
    │   └── ItemAdapter.kt
    ├── audit
    │   ├── AuditActivity.kt
    │   ├── AuditComponents.kt
    │   └── search
    │       ├── AnimationUtil.java
    │       └── SearchResultActivity.kt
    ├── custom
    │   ├── AddCustomCourseActivity.kt
    │   ├── CustomComponents.kt
    │   └── CustomSettingBottomFragment.kt
    ├── detail
    │   ├── CourseDetailAdapter.kt
    │   ├── CourseDetailBottomFragment.kt
    │   ├── DetailComponents.kt
    │   ├── MultiCourseDetailFragment.kt
    │   └── WrapContentViewPager.java
    ├── schedule
    │   ├── CourseRefreshBottomFragment.kt
    │   ├── RefreshComponents.kt
    │   ├── ScheduleActivity.kt
    │   ├── ScheduleAdapter.kt
    │   └── ScheduleDecoration.kt
    ├── theme
    │   ├── ColorCircleView.kt
    │   └── SpreadChainLayout.kt
    └── week
        ├── WeekSelectAdapter.kt
        ├── WeekSquareView.kt
        └── WeekTestActivity.kt

16 directories, 44 files

```

### 资源命名

**一定要加前缀**
**一定要加前缀**
**一定要加前缀**

比如说GPA模块中的一个`layout`

```
layout
├── gpa2_activity_evaluate.xml
├── gpa2_activity_gpa.xml
├── gpa2_component_sort.xml
├── gpa2_component_term.xml
├── gpa2_component_total.xml
├── gpa2_item_course.xml
└── gpa2_tv_selected_term.xml
```

Drawable，其他资源文件同理

### 依赖

- 不在特别需要的时候，不建议随意使用第三方框架。

- 不在特别需要的时候，不适用注解处理器框架，实在需要的话，找组长评估后考虑批准/或被diss。

- 不允许使用第三方Gradle插件，实在需要的话，找组长评估后考虑批准/或被diss。

- 使用第三方库的时候，未批准不允许暴露给其他模块，使用`implementation`。

- 对于微型第三方库（只有一个或者几个文件那种），可以拷贝代码将来直接使用（要保留代码注释头部的开源证书），而不是依赖模块。

- Support库使用统一依赖，使用方式参考`gpa2`/`schedule2`模块的`build.gradle`

  ```groovy
      [':auth', ':bike', ':commons', ':gpa2', ':schedule', ':tjulibrary', ':tjunet', ':party', ':schedule2'].each {
          implementation project(it)
      }
      implementation 'com.twt.service:fragmentation:1.1.0'

      [*supportLibraries, 'constraint-layout', 'multidex', *archLibraries, 'kotlin-stdlib', 'butterknife', 'anko'].each {
          implementation dependenciesMap[it]
      }
      ['lifecycle-compiler', 'butterknife-compiler'].each {
          annotationProcessor dependenciesMap[it]
      }
  ```


# 期望/计划

- Recyclerview DSL使用文档
- 普通DSL使用文档


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