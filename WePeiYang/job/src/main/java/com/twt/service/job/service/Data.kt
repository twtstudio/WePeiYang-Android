package com.twt.service.job.service


const val ARG_KIND = "arg_kind"

const val JOB_MESSAGE_TYPE = 0 //招聘信息
const val JOB_FAIR_TYPE = 1 // 招聘会
const val NOTICE_TYPE = 0 // 公告
const val DYNAMIC_TYPE = 1 // 动态

const val JOB_MESSAGE = "job_message" //招聘信息
const val JOB_FAIR = "job_fair" // 招聘会
const val NOTICE = "notice" // 公告
const val DYNAMIC = "dynamic" // 动态

const val KEY_TYPE = "type"
const val KEY_ID = "id"

const val VIP = "vip"
const val TOP = "top"
const val COMMON = "common"

object funs {
    // 根据类型判断对应 type
    fun getType(string: String): Int = when (string) {
        JOB_MESSAGE, NOTICE -> 0
        else -> 1
    }

    fun convert(commonL: List<HomeDataL>): List<HomeDataR> {
        val commomRs: MutableList<HomeDataR> = mutableListOf()
        repeat(commonL.size) { i ->
            commonL[i - 1].apply {
                commomRs.add(HomeDataR(click,date, id, important, title) )
            }
        }
        return commomRs
    }
}

data class GeneralL(
        val common: List<HomeDataL>,
        val important: List<HomeDataL>,
        val page: String,
        val page_count: Int,
        val type: String
)

data class HomeDataL(
        val click: String,
        val date: String,
        val held_date: String,
        val held_time: String,
        val id: String,
        val important: String,
        val place: String,
        val title: String
)

data class GeneralR(
        val common: List<HomeDataR>,
        val current_page: String,// 当前页数
        val important: List<HomeDataR>,
        val page_count: Int,
        val rotation: List<HomeDataR>,// 工作动态的轮换的3条，公告没有
        val type: String
)

data class HomeDataR(
        val click: String,
        val date: String,
        val id: String,
        val important: String,
        val title: String
)

data class Recruit(
        val attach1: String,
        val attach1_name: String,
        val attach2: String,
        val attach2_name: String,
        val attach3: String,
        val attach3_name: String,
        val click: Int,
        val content: String,
        val corporation: String,
        val date: String,
        val deadline: String,
        val held_date: String,
        val held_time: String,
        val id: String,
        val place: String,
        val request: String,
        val title: String,
        val type: String
)

//工作动态或者公告的某一条的数据详情
data class Notice(
        val attach1: String,
        val attach1_name: String,
        val attach2: String,
        val attach2_name: String,
        val attach3: String,
        val attach3_name: String,
        val click: Int,
        val content: String,
        val date: String,
        val id: String,
        val title: String,
        val type: String
)

data class SearchData(
        val info: List<Info>,
        val meeting: List<Meeting>
)

data class Info(
        val click: String,
        val date: String,
        val held_date: Any,
        val held_time: Any,
        val id: String,
        val place: Any,
        val title: String
)

data class Meeting(
        val click: String,
        val date: String,
        val held_date: String,
        val held_time: String,
        val id: String,
        val place: String,
        val title: String
)
