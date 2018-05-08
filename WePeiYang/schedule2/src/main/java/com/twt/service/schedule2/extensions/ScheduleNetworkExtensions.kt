package com.twt.service.schedule2.extensions

import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.cache.RefreshState

typealias RefreshCallback = (RefreshState<CacheIndicator>) -> Unit