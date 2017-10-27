package com.twtstudio.retrox.tjulibrary.homeitem

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.network.RetrofitProvider
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.extension.bind
import com.twtstudio.retrox.tjulibrary.provider.Info
import com.twtstudio.retrox.tjulibrary.provider.LibApi
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Created by retrox on 26/10/2017.
 */
class HomeLibItemComponent(private val lifecycleOwner: LifecycleOwner, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val context = itemView.context
    private val stateImage: ImageView = itemView.findViewById(R.id.ic_home_lib_state)
    private val stateProgressBar: ProgressBar = itemView.findViewById(R.id.progress_home_lib_state)
    private val stateMessage: TextView = itemView.findViewById(R.id.tv_home_lib_state)
    private val bookContainer: LinearLayout = itemView.findViewById(R.id.ll_home_lib_books)
    private val refreshBtn: Button = itemView.findViewById(R.id.btn_home_lib_refresh)
    private val renewBooksBtn: Button = itemView.findViewById(R.id.btn_home_lib_renew)
    private val loadMoreBooksBtn: Button = itemView.findViewById(R.id.btn_home_lib_more)

    private val loadMoreBtnText = MutableLiveData<String>()
    private val loadingState = MutableLiveData<Int>()
    private val message = MutableLiveData<String>()
    private var isExpanded = false


    private val bookItemViewContainer = mutableListOf<View>()
    private val libApi = RetrofitProvider.getRetrofit().create(LibApi::class.java)


    fun render(): View = itemView

    init {
        //这里bind一下 解个耦
        message.bind(lifecycleOwner) { message ->
            stateMessage.text = message
        }

        loadingState.bind(lifecycleOwner) { state ->
            when (state) {
                PROGRESSING -> {
                    stateImage.visibility = View.INVISIBLE
                    stateProgressBar.visibility = View.VISIBLE
                    message.value = "正在刷新"

                }
                OK -> {
                    stateImage.visibility = View.VISIBLE
                    stateProgressBar.visibility = View.INVISIBLE
                    Glide.with(context).load(R.drawable.lib_ok).into(stateImage)

                }
                WARNING -> {
                    stateImage.visibility = View.VISIBLE
                    stateProgressBar.visibility = View.INVISIBLE
                    Glide.with(context).load(R.drawable.lib_warning).into(stateImage)


                }
            }
        }

        loadMoreBtnText.bind(lifecycleOwner) {
            loadMoreBooksBtn.text = it
            if (it == NO_MORE_BOOKS) {
                loadMoreBooksBtn.isEnabled = false
            }
        }
    }

    fun onBind() {
        refreshBtn.setOnClickListener {
            refresh()
        }
        refresh()
        renewBooksBtn.setOnClickListener {
            bookContainer.removeAllViews()
        }
        loadMoreBooksBtn.setOnClickListener { view: View ->
            if (isExpanded) {
                (bookContainer.childCount-1 downTo 0 )
                        .filter { it >= 3 }
                        .forEach { bookContainer.removeViewAt(it) }
                loadMoreBtnText.value = "显示剩余(${bookItemViewContainer.size - 3})"
                isExpanded = false
            } else {
                (0 until bookItemViewContainer.size)
                        .filter { it >= 3 }
                        .forEach { bookContainer.addView(bookItemViewContainer[it]) }
                loadMoreBtnText.value = "折叠显示"
                isExpanded = true
            }
        }
    }

    private fun refresh() {
        async(UI) {
            loadingState.value = PROGRESSING
            val data: Info? = bg { libApi.libUserInfo.map { it.data }.toBlocking().first() }.await()

            loadingState.value = OK
            val size = data?.books?.size ?: 0
            if (size != 0) {
                message.value = "一共借了${size}本书"
            } else {
                message.value = "还没有从图书馆借书呢"
                renewBooksBtn.isClickable = false
                return@async
            }
            bookContainer.removeAllViews()
            bookItemViewContainer.clear()

            val inflater = LayoutInflater.from(this@HomeLibItemComponent.context)

            data?.books?.forEach {
                val view = inflater.inflate(R.layout.item_common_book, bookContainer, false)
                val bookItem = BookItemComponent(lifecycleOwner = lifecycleOwner, itemView = view)
                bookItem.bindBook(it)
                bookItemViewContainer.add(view)
            }

            if (bookItemViewContainer.size <= 3) {
                bookItemViewContainer.forEach {
                    bookContainer.addView(it)
                }
                loadMoreBtnText.value = NO_MORE_BOOKS // 无更多书显示，用常量限制，可以观测该值然后设置按钮可用性
            } else {
                bookItemViewContainer.forEachIndexed { index, view ->
                    if (index < 3) {
                        bookContainer.addView(view)
                    }
                }
                loadMoreBtnText.value = "显示剩余(${bookItemViewContainer.size - 3})"
            }

        }.invokeOnCompletion { throwable: Throwable? ->
            //错误处理时候的卡片显示状况
            throwable?.let {
                when (throwable) {
                    is HttpException -> {
                        try {
                            val errorJson = throwable.response().errorBody()!!.string()
                            val errJsonObject = JSONObject(errorJson)
                            val errcode = errJsonObject.getInt("error_code")
                            val errmessage = errJsonObject.getString("message")
                            loadingState.value = WARNING
                            message.value = errmessage
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                    is SocketTimeoutException -> {
                        loadingState.value = WARNING
                        this.message.value = "网络超时...很绝望"
                    }
                    else -> {
                        loadingState.value = WARNING
                        this.message.value = "粗线蜜汁错误"
                    }
                }
            }
        }
    }

    private companion object {
        const val OK = 0
        const val PROGRESSING = 1
        const val WARNING = 2
        const val NO_MORE_BOOKS = "无更多书显示"
    }


}