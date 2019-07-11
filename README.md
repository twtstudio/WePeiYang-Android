## 微北洋开发白皮书

目录

- [微北洋开发白皮书](#微北洋开发白皮书)    
- [开发指南](#开发指南)        
    - [分模块开发](#分模块开发)        
    - [应用依赖关系](#应用依赖关系)        
    - [应用内框架](#应用内框架)    
        - [网络请求](#网络请求)            
        - [泛型包装](#泛型包装)            
        - [网络请求 Interface 怎么用](#网络请求-interface-怎么用)            
        - [Async/await来处理网络请求以及异步任务](#asyncawait来处理网络请求以及异步任务)            
        - [构建有缓存的响应式网络请求](#构建有缓存的响应式网络请求)            
        - [DSL & RecyclerViewDSL](#dsl--recyclerviewdsl)
- [开发规范](#开发规范)        
    - [架构](#架构)        
    - [依赖规范](#依赖规范)        
    - [命名规范](#命名规范)        
    - [字体使用](#字体使用)        
    - [StringSpanDSL使用](#stringspandsl使用)

### 开发指南

目前代码质量较高的模块有 `gpa2`、`schedule2`。里面的代码涵盖了应用内框架的用法，Kotlin 的高级使用方式，架构的抽象封装，自定义 View 等。
如果不知道从哪里做起，可以先从 gpa2 的 Model 层看起，然后一步步追溯到 View 层，看处理方式。

看代码可以用两种方法：自顶向下和自下而上。

#### 分模块开发

微北洋使用多模块方式进行开发，其中：

- `commons` 模块为基础库模块，包含网络请求、缓存、RecyclerViewDSL 等应用内框架
- `app` 模块为主模块，也就是一进到微北洋的模块
- 其他模块为微北洋各个功能的分模块，如 `lostfound` 为失物招领模块，`gpa2` 为 GPA 模块

`app` 模块为 application，其余模块以 library 的形式 (体现在各模块的 build.gradle 文件中第一行 `apply plugin` 的不同) 给 `app` 模块留出入口，以插拔的方式应用在微北洋里。

#### 应用依赖关系

多个模块需要使用的依赖放在 `commons` 模块里，使用 api 关键字添加依赖，以暴露给其他模块。

`app` 模块依赖包括 `commons` 模块在内的其他所有模块，其他模块依赖  `commons` 模块，以获取应用内框架的依赖和公共依赖。

#### 应用内框架

应用内框架集中在 `commons` 模块中

##### 网络请求

微北洋中网络请求统一使用 [Retrofit](http://square.github.io/retrofit/)

我们需要根据后端组提供的 API 接口来写对应的 interface。
一般来讲一个请求的 URL 是由 `BaseUrl + Path` 组成。比如说`https://open.twtstudio.com/api/v1/auditClass/audit` 这个网络请求，我们规定 `BaseUrl = https://open.twtstudio.com/api/`，同时 `Path = v1/auditClass/audit`。因此我们的接口就可以这样子写：

~~~kotlin
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
~~~

这基本上就是Retrofit的写法，**需要提及一点是接口方法的返回值**
比如说这几个例子中我们的返回值类型是`Deferred<T>`是协程的返回类型(可以`await`那种)，之所以可以用这类型返回值，是因为我们在Retrofit初始化的时候，加入了对应的`CallAdapter`

```kotlin
.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
.addCallAdapterFactory(CoroutineCallAdapterFactory())
//see code: com/twt/wepeiyang/commons/experimental/network/ServiceFactory.kt
```

不添加额外的 CallAdapterFactory 的话，我们在接口中只能写 `Call<T>` 的返回值，现在我们可以写`Deferred<T>`,`Observable<T>`的返回值。例如

```kotlin
@GET("v1/auditClass/audit")
fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Call<CommonBody<List<AuditCourse>>>

@GET("v1/auditClass/popular")
fun getPopluarAudit(): Observable<CommonBody<List<AuditPopluar>>>

```

##### 泛型包装

  先从微北洋的 Api 返回结构说起，规范的Api返回结构是
```json
  {
   "error_code": -1,
   "message": "", 
   "data": <>
  }
```
其中，error_code 为错误码，一般规定 -1 为成功响应，其他数字代表不同的错误，比如 token 过期，办公网绑定错误；message 含有具体的错误信息；data 中为有效数据。

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

因为你真正想要的数据在`data`字段里面，所有你只需要单独写`data`内容对于的`bean`类即可，然后用泛型包装。例如蹭课系统中我的一个请求返回的内容是

```json
  {
   "error_code": -1,
   "message": "",
   "data":
      {
         "course_id": 53,
         "course_name": "数据库原理（双语）",
         "college": "软件学院",
         "semester": "1",
         "year": "2018",
         
     }
  }
```
返回的`data`字段是一个List，因此我们只需要去写这个 List 内对象的对应类即可

```kotlin
  data class AuditCourse(val college: String,
                       val courseId: Int,
                       val courseName: String,
                       val year: String,
                       val semester: String)
```
同时在 Retrofit 的 API 里面写

```kotlin
  @GET("v1/auditClass/audit")
    fun getMyAudit(@Query("user_number") userNumber: String = CommonPreferences.studentid): Deferred<CommonBody<List<AuditCourse>>>

    Deferred<CommonBody<List<AuditCourse>>> //这种情况是CommonBody里面包着List<T>
```
当然Data字段里面不是List就直接泛型包裹即可，比如说办公网课程表那边
    
```kotlin
    @GET("v1/classtable")
    fun getClassTable(): Deferred<CommonBody<Classtable>>
```

##### 网络请求 Interface 怎么用

  Retrofit 对于网络请求的接口采用了动态代理的机制，commons 库中对其进行了封装，具体用法：

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

推荐使用第二种，对应的源码在`com/twt/wepeiyang/commons/experimental/network/ServiceFactory.kt`，如果你想要看懂这些的话，你需要学`Retrofit`,`Kotlin 代理`,`invoke() 运算符重载`

##### async/await 来处理网络请求以及异步任务

目前微北洋里面的异步任务都是使用Kotlin协程来写的。

作为一个合格的项目开发者，你应该使用Kotlin协程所提供的`async/await`来处理问题。在之前的文档中，我们的网络请求的返回值是`Deferred<T>`类型，至于这是个啥，请自己上网查谢谢。[附一个文章](https://zhuanlan.zhihu.com/p/30019105)
一般的做发是，在普通环境(非协程环境)使用`GlobalScope.launch(Dispatchers.Main){}`方法启动一个协程，在协程里面使用`async/await`来控制。

```kotlin
  GlobalScope.launch (exceptionHandler + Dispatchers.Main) {
          val result = AuditApi.searchCourse(courseName).await()
          // do something 这里的代码会等待网络请求出结果才执行
  }
```
比如说我们添加一些逻辑

```kotlin
  GlobalScope.launch (exceptionHandler + Dispatchers.Main) {
          val result: CommonBody<String> = AuditApi.searchCourse(courseName).await()
          // do something
          if (result.error_code == -1) textView.text = result.data 
  }
```
协程里面的上下文：通用的有`Dispatchers.Main` `Dispatchers.Default`表示协程运行在哪个线程中，`Dispatchers.Default`是一个线程池，耗时操作要在这里完成，然后通过`await`来协调先后。示例一段伪代码（看个意思就行）
```kotlin
  GlobalScope.launch (exceptionHandler + Dispatchers.Main) { // 主线程开启
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
到这里，你已经会使用微北洋的基础框架来发起一个网络请求了。请不要另辟蹊径，谢谢！
继续阅读下去，你会学到：缓存框架的用法

##### 构建有缓存的响应式网络请求

有一部分的网络请求是需要通过添加缓存来提高用户体验的，比如说新闻列表，课程表之类。微北洋已经对这种常用的缓存进行了封装。 这套封装的架构，整体来讲是基于LiveData来做的，所以如果要彻底了解这些，需要些预备知识：[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

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
如果你可以看懂RefreshableLiveData的源码，你可以看到它的自动刷新时机，Activity/Fragment在Active的时候启动（你可以粗略的理解成`onCreate`或者`onResume`），你也可以手动刷新，甚至加上刷新回调。

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
auditPopluarLiveData.refresh(CacheIndicator.LOCAL, CacheIndicator.REMOTE)// 后面的部分表示，从哪里刷新 Remote就是远程(服务器)
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

##### DSL & RecyclerViewDSL 

可直接参考框架作者的其他文章

- [构建 RecyclerViewDSL](https://www.kotliner.cn/2018/06/recyclerviewdsl/)
- [DSL in Action](https://www.kotliner.cn/2018/04/dsl-in-action/)

### 开发规范

#### 架构

微北洋的模块开发中，不会强制要求采用 MVP 架构或者 MVVM 架构之类。在架构的选择上鼓励有效的创新，服从个人喜好。如果你对于项目结构有些拿捏不定的话，可以参考 `gpa2` 模块或者 `schedule2` 模块，里面做的还算不错。

一般来讲，Model 层是一定要分开的，就是说，如果是网络请求，数据库操作，或者缓存封装之类的话，要单独写开。比如说，把网络请求的 interface，网络请求缓存的包装，写在一个 kt 文件中。把各种 bean 类的定义（要用 data class），写在一个 kt 文件中。把数据库操作写在一个 kt 文件中。

建议最后暴露给上层（Activity / Presenter）使用的时候，做二次封装，比如说`AuditCourseManager.kt`，建议不要让上层直接操作数据库网络请求之类，尤其是数据库

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

#### 依赖规范
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

#### 命名规范

- drawable 包下文件

  drawable 包下的资源一般分为图片(pic)、图标(ic)、Android 样式(如 shape)，命名方式为

  模块前缀\_资源类型\_使用场景抽象单词\_描述(均为小写字母)

  如：

  auth_pic_login_background.png

  exam_ic_user_settings.png

  lf2_shape_im_status_bar.xml

- layout 包下文件

  layout 包下一般为各种布局，如 activity, fragment, item, popup window 等，命名方式为： 

  资源类型\_使用场景抽象单词\_描述(均为小写字母，描述为可选)，如：

  auth_activity_login.xml

  yp2_item_contact.xml

  gpa2_popup_user_confirm.xml

- colors.xml 中颜色资源

  (资源前缀)Color(使用控件类型)(颜色)，使用小驼峰命名法，如：

  scheduleColorTextBlue

  libColorToolbarYellow

- 各 layout.xml 中控件 id

  控件各大写字母\_使用场景抽象单词\_描述(均为小写字母)，如

  tv_login_title — 这是一个 TextView

  et_login_username — 这是一个 EditText

  rv_contact — 这是一个 RecyclerView

- .kt 文件中 View

  将上条中的“使用场景抽象单词”去掉(无描述单词的可以不去掉)，改写成小驼峰命名法，如

  tvTitle, etUsername, rvContact


#### 字体使用
随着微北洋的迭代以及设计方案的成熟，对应的字体使用也应该更加规范。字体的使用分为中文字体与英文字体。 因为中文字体过于庞大，因此我们仅仅使用英文和数字的自定义字体。

对于英文和数字，我们采用 `montserrat` 字体。对应的资源文件存放，可以在 `commons/res/fonts` 中看到。里面目前有四个文件 `montserrat.xml         montserrat_bold.ttf    montserrat_light.ttf   montserrat_regular.ttf` ，其中的xml文件表示FontFamily，里面包含对多个字重对应字体的定义，还有三个单独的ttf文件。使用这部分字体可以从xml中或者java代码中来操作。下面是一个示例：
``` xml
    <TextView
        android:id="@+id/tv_big_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        android:textColor="#444444"
        android:textSize="16sp"
        android:fontFamily="@font/montserrat"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:text="TJU-WLAN" />

    <!-- 或者单独指定某个字体 因为fontfamily可能存在bug -->

    <TextView
        android:id="@+id/tv_big_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        android:textColor="#444444"
        android:textSize="16sp"
        android:fontFamily="@font/montserrat_regular"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:text="TJU-WLAN" />
```
xml 中也可以使用 ` android:textStyle="bold" ` 这种来手动指定字重。

Java代码： 
``` kotlin 
val typeface = ResourcesCompat.getFont(CommonContext.application.applicationContext, R.font.montserrat_regular)

val typeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
```
然后在配合字重来做字体效果。比如说这种：`StyleSpan(Typeface.BOLD)`

#### StringSpanDSL使用
目前 StringSpanDSL 写在了 Schedule2 模块中  `com/twt/service/schedule2/extensions/StringExtensions.kt` ，之后会根据应用场景考虑放在 Commons 里面。
一段示例：
```kotlin 
val roomCluster = course.arrange[0].room.split("楼")
val divider = "\n \n"
val creditDisplayed = "- " + course.credit + " -"
val courseNameSpanSize = if (course.coursename.length > 6)13 else 15
val stringSpan = spannable {
    absSize(10, creditDisplayed) +
            absSize(3, divider) + // 一种Span
            span(course.coursename, listOf(StyleSpan(Typeface.BOLD), AbsoluteSizeSpan(courseNameSpanSize, true))) + //一个字符串设置多种Span的情况
            absSize(4, divider) +
            absSize(10, roomCluster.joinToString(separator = "-"))
}
```

