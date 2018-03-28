package com.twtstudio.service.dishesreviews.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.support.v7.widget.ViewStubCompat


/**
 * Created by zhangyulong on 18-3-16.
 */
@SuppressLint("RestrictedApi")
/**
 * 一个实现懒加载的Fragment
 */
abstract class LazyFragment : Fragment() {
    private var mRootView: View?=null
    private var isViewCreated = false
    private lateinit var mViewStub: ViewStubCompat
    private var isUserVisible = false
    private var isLoaded = false
    //获取真正的数据视图
    protected abstract fun getResId(): Int

    //当视图真正加载时调用
    protected abstract fun onRealViewLoaded(view: View)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView != null) {
            isViewCreated = true;
            return mRootView;
        }

        val context = inflater.context
        val root = FrameLayout(context)
        mViewStub = ViewStubCompat(context, null)
        mViewStub.layoutResource = getResId()
        root.addView(mViewStub, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        root.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.MATCH_PARENT)

        mRootView = root
        if (isUserVisible) {
            realLoad()
        }

        isViewCreated = true
        return mRootView

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUserVisible = isVisibleToUser
        if (isUserVisible && isViewCreated) {
            realLoad()
        }
    }

    private fun realLoad() {
        if (isLoaded) {
            return
        }

        isLoaded = true
        onRealViewLoaded(mViewStub.inflate())
    }

    override fun onDestroyView() {
        isViewCreated = false
        super.onDestroyView()
    }
}