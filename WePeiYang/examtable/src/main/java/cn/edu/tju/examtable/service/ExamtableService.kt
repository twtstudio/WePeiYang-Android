package cn.edu.tju.examtable.service

import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.network.CommonBody
import com.twt.wepeiyang.commons.experimental.network.ServiceFactory
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.http.*
import java.lang.Exception
import java.nio.channels.Selector

interface ExamtableService {

    @GET("v1/examtable")
    fun getTable(): Deferred<CommonBody<List<ExamBean>>>

    companion object : ExamtableService by ServiceFactory()
}
