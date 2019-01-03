package com.twt.service.schedule2.model.exam

import java.text.SimpleDateFormat
import java.util.*

data class ExamTableBean(
        val id: String,
        val name: String,
        val type: String,
        val date: String,
        val arrange: String,
        val location: String,
        val seat: String,
        val state: String,
        val ext: String
) {
    /**
     * 需要Try Catch
     */
    fun parseToDatePair(): Pair<Date, Date> {
        val times = arrange.split("~")
        val dates = times.map {
            "$date $it"
        }
        val dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("zh_CN"))
        val results = dates.map {
            dataFormat.parse(it)
        }
        if (results.size == 2) {
            return results[0] to results[1]
        } else throw IllegalStateException("考试时间状态异常")
    }

    fun calETA(): Pair<Long, Long> {
        return try {
            val (start, end) = parseToDatePair()
            val now = Date()
            val diff = start.time - now.time
            val days = diff / (1000 * 60 * 60 * 24)

            val hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            //            val minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60)
            days to hours
        } catch (e: Exception) {
            e.printStackTrace()
            0.toLong() to 0
        }
    }
}
