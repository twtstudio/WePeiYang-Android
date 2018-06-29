package com.twt.wepeiyang.commons.ui

import android.os.Build
import android.text.Html
import android.text.Spanned

val String.spanned: Spanned
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }