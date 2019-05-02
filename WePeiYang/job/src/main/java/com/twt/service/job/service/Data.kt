package com.twt.service.job.service

import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.experimental.preference.hawk

const val ARG_KIND = "arg_kind"

const val JOB_MESSAGE_TYPE = 0 //招聘信息
const val JOB_FAIR_TYPE = 1 // 招聘会
const val NOTICE_TYPE = 0 // 公告
const val DYNAMIC_TYPE = 1 // 动态

const val JOB_MESSAGE = "招聘信息"
const val JOB_FAIR = "招聘会"
const val NOTICE = "公告"
const val DYNAMIC = "动态"

// activity 跳转传递的参数的 key
const val KEY_KIND = "key_kind"
const val KEY_ID = "key_id"

const val SEARCH_HISTORY = "key_sh"

val listsOfHome = mutableListOf(JOB_MESSAGE, JOB_FAIR, NOTICE, DYNAMIC)

// 分别存四个碎片的最大页数
var pagesOfMsg: Int by hawk(JOB_MESSAGE, 1)
var pagesOfFair: Int by hawk(JOB_FAIR, 1)
var pagesOfNotice: Int by hawk(NOTICE, 1)
var pagesOfDynamic: Int by hawk(DYNAMIC, 1)

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
        val attach1: String?,
        val attach1_name: String?,
        val attach2: String?,
        val attach2_name: String?,
        val attach3: String?,
        val attach3_name: String?,
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
        val attach1: String?,
        val attach1_name: String?,
        val attach2: String?,
        val attach2_name: String?,
        val attach3: String?,
        val attach3_name: String?,
        val click: Int,
        val content: String,
        val date: String,
        val id: String,
        val title: String,
        val type: String
)

data class NoticeAfter(
        val attachs: Map<String, String>,
        val click: Int,
        val content: String,
        val date: String,
        val title: String,
        val hasAttach: Boolean
)

data class SearchData(
        val info: List<InfoOrMeeting>,
        val meeting: List<InfoOrMeeting>
)

data class InfoOrMeeting(
        val click: String,
        val date: String,
        val held_date: String,
        val held_time: String,
        val id: String,
        val place: String,
        val title: String
)

