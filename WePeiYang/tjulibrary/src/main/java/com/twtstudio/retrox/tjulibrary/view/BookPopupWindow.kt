package com.twtstudio.retrox.tjulibrary.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.os.Vibrator
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.blur.BlurPopupWindow
import com.twt.wepeiyang.commons.ui.spanned
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.home.LibraryViewModel
import com.twtstudio.retrox.tjulibrary.provider.Book
import kotlinx.android.synthetic.main.popup_library_book.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.layoutInflater

@SuppressLint("ViewConstructor")
class BookPopupWindow(val book: Book, mContext: Context) : BlurPopupWindow(mContext), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry
    lateinit var view: View
    override fun createContentView(parent: ViewGroup): View = parent.context.layoutInflater
            .inflate(R.layout.popup_library_book, parent, false).apply {
                layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER
                    horizontalMargin = dip(50)
                }
            }.also { view = it }

    @SuppressLint("MissingPermission")
    override fun onShow() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        //想要给弹窗加入动态的功能 绑定到LiveData上
        LibraryViewModel.infoLiveData.bindNonNull(this) {
            vibrator.vibrate(30L)
            val liveBook = it.books.find { it.barcode == book.barcode } ?: book
            view.apply {
                tv_book_name.text = liveBook.title
                tv_book_author.text = liveBook.author
                tv_location_name.text = liveBook.local
                tv_location_detail.text = liveBook.callno
                tv_book_in_time.text = liveBook.loanTime
                tv_book_out_time.text = if (!liveBook.isOverTime) "${liveBook.returnTime} (剩余${liveBook.timeLeft()}天)" else "${liveBook.returnTime} (<span style=\"color:#FF5D64\";>已过期</span>)".spanned
                btn_book_borrow.setOnClickListener {
                    LibraryViewModel.renewBook(it.context, liveBook)
                }
            }
        }

    }

    override fun onDismiss() {
        super.onDismiss()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun createShowAnimator(): Animator {
        val alphaAnim = super.createShowAnimator()
        val animSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(alphaAnim)
        return animSet
    }

    override fun createDismissAnimator(): Animator {
        val animSet = AnimatorSet()
        val contentAnim = ObjectAnimator.ofFloat(mContentLayout, "alpha", mContentLayout.alpha, 0f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(contentAnim)
        return animSet
    }

    override fun getAnimationDuration(): Long = 200L

    override fun isDismissOnTouchBackground(): Boolean = true

    init {
        setOnClickListener {
            dismiss()
        }
    }

}