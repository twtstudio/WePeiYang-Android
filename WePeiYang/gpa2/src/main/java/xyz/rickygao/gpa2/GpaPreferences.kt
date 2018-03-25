package xyz.rickygao.gpa2

import com.twt.wepeiyang.commons.experimental.preference.hawk

/**
 * Created by rickygao on 2018/3/14.
 */
object GpaPreferences {
    var gpaToken by hawk("gpa_token", "")
}