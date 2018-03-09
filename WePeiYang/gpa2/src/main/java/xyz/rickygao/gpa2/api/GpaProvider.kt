package xyz.rickygao.gpa2.api

import android.arch.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import com.orhanobut.logger.Logger
import com.twt.wepeiyang.commons.experimental.ConsumableMessage
import com.twt.wepeiyang.commons.utils.CommonPrefUtil
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Created by rickygao on 2017/11/9.
 */
object GpaProvider {
    val gpaLiveData = MutableLiveData<GpaBean>()
    val successLiveData = MutableLiveData<ConsumableMessage<String>>()
    val errorLiveData = MutableLiveData<ConsumableMessage<String>>()

    private const val HAWK_KEY_GPA = "GPA"
    fun updateGpaLiveData(useCache: Boolean = true, silent: Boolean = false) {
        async(UI) {
            val remote = bg {
                RealGpaService.get().execute().body()?.data
            }

            if (useCache) {
                bg {
                    Hawk.get<GpaBean?>(HAWK_KEY_GPA, null)
                }.await()?.let {
                    gpaLiveData.value = it
                    if (!silent) successLiveData.value = ConsumableMessage("从缓存中拿到了你的 GPA")
                }
            }

            remote.await()?.let {
                if (it.updated_at != gpaLiveData.value?.updated_at) {
                    gpaLiveData.value = it
                    if (!silent) successLiveData.value = ConsumableMessage("你的 GPA 有更新喔")
                    Hawk.put<GpaBean>(HAWK_KEY_GPA, it)
                } else {
                    if (!silent) successLiveData.value = ConsumableMessage("你的 GPA 已经是最新的了")
                }

                CommonPrefUtil.setGpaToken(it.session)
            }

        }.invokeOnCompletion {
            it?.let {
                Logger.e(it, "Gpa")
                errorLiveData.value = ConsumableMessage("好像出了什么问题，${it.javaClass} ${it.message}")
            }
        }
    }

    fun postEvaluate(evaluate: Evaluate, q1: Int, q2: Int, q3: Int, q4: Int, q5: Int, note: String) {
        async(UI) {
            val remote = bg {

                val token = CommonPrefUtil.getGpaToken()

                val params = sortedMapOf(
                        "token" to token,
                        "lesson_id" to evaluate.lesson_id,
                        "union_id" to evaluate.union_id,
                        "course_id" to evaluate.course_id,
                        "term" to evaluate.term,
                        "q1" to q1.toString(),
                        "q2" to q2.toString(),
                        "q3" to q3.toString(),
                        "q4" to q4.toString(),
                        "q5" to q5.toString(),
                        "note" to note
                ).apply {
                    //                    put("t", Calendar.getInstance().timeInMillis.toString())
//                    val paramsString = JniUtils.getInstance().appKey +
//                            entries.map { it.key + it.value }.reduce(String::plus) +
//                            JniUtils.getInstance().appSecret
//                    val sign = String(Hex.encodeHex(DigestUtils.sha1(paramsString))).toUpperCase()
//                    put("sign", sign)
//                    put("app_key", ServiceFactory.APP_KEY)
                }

                RealGpaService.evaluate(params).execute()
            }

            remote.await()?.let {
                successLiveData.value = ConsumableMessage("评价成功，快回去刷新看看新的 GPA 吧")
            }
        }.invokeOnCompletion {
            it?.let {
                errorLiveData.value = ConsumableMessage("好像出了什么问题，${it.message}")
            }
        }
    }

}