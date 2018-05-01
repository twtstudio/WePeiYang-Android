package com.twt.service.home.news.Model

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class NewsModel {


//    open fun PostParams(){
//        ServiceParams(Params()!!)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe (object : Subscriber<BaseBean>() {
//                    override fun onCompleted() {
//                        Completed()
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        FailedOperation(e)
//                    }
//
//                    override fun onNext(o: BaseBean) {
//                        // val message = o.message
//                        //mOnLoginListenr?.LoginSuccess()
//                        SuccessOperation(o)
//                    }
//                })
//    }
//
//    //取到RetrofitManager中的service
//    fun ServiceParams(params: HashMap<String, String>): Observable<BaseBean> {
//        return RetrofitManager.builder().service!!.getRegisterList(params)
//    }
//
//
//    //var mOnLoginListenr:LoginPresenter.onLoginListener?=null
//    var mOnLoginListener: LoginModel.OnLoginListener? = null
//    var mUserName: String? = null
//    var mPassWord: String? = null
//
//    //失败过后的操作
//    fun FailedOperation(e: Throwable?) {
//
//    }
//
//    //声明监听接口
//    fun LoadComplete(onLoginListener: LoginModel.OnLoginListener) {
//        if (mOnLoginListener == null) {
//            mOnLoginListener = onLoginListener
//        }
//    }
//
//    //登录成功过后的操作
//    fun SuccessOperation(o: BaseBean) {
//        // val message = o.message
//        if (o.success) {
//            mOnLoginListener!!.LoginSuccess(o)
//
//        } else {
//            mOnLoginListener!!.LoginFailed(o)
//        }
//    }
//
//    //需要传入的参数
//    fun Params(): HashMap<String, String>? {
//        ClearHashMap()
//        mParams!!.put("username", mUserName!!)
//        mParams!!.put("password", mPassWord!!)
//        return mParams
//    }
//
//    //进行的登录操作
//    fun Login(username: String, password: String) {
//        mUserName = username
//        mPassWord = password
//        PostParams()
//
//    }
//
//    interface OnLoginListener{
//        fun LoginSuccess(baseBean: NewsBean)
//        fun LoginFailed(baseBean: NewsBean)
//    }
}
