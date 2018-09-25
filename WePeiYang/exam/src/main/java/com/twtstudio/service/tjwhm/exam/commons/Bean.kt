package com.twtstudio.service.tjwhm.exam.commons

/**
 * Created by tjwhm@TWTStudio at 4:01 PM, 2018/9/1.
 * Happy coding!
 */

data class BaseBean<T>(
        val error_code: Int,
        val message: String,
        val data: T?
)