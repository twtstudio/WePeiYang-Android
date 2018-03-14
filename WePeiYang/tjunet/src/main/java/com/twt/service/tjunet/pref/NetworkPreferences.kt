package com.twt.service.tjunet.pref

import com.twt.wepeiyang.commons.experimental.CommonPreferences
import com.twt.wepeiyang.commons.experimental.hawk

/**
 * Created by retrox on 2018/3/13.
 */
object NetworkPreferences {

    var username by hawk("tjunet_username", CommonPreferences.studentid)

    var password by hawk("tjunet_password", "")

    var savePassword by hawk("tjunet_savePassword", true)

}