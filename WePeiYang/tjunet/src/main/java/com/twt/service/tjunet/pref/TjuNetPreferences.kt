package com.twt.service.tjunet.pref

import com.twt.wepeiyang.commons.experimental.pref.CommonPreferences
import com.twt.wepeiyang.commons.experimental.pref.hawk

/**
 * Created by retrox on 2018/3/13.
 */
object TjuNetPreferences {

    var username by hawk("tjunet_username", CommonPreferences.studentid)

    var password by hawk("tjunet_password", "")

    var savePassword by hawk("tjunet_save_password", true)

}