package com.twtstudio.service.dishesreviews.extensions

import com.twt.wepeiyang.commons.experimental.preference.hawk

object DishPreferences {
    var isNewCampus by hawk("is_new_campus", true)
}