package xyz.rickygao.gpa2.spider

import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import xyz.rickygao.gpa2.spider.utils.Classes

class GpaSpider {
    fun spider():String{
        val classes = Classes()
        classes.init()
        classes.login(CommonPreferences.twtuname,CommonPreferences.password)
    }
}