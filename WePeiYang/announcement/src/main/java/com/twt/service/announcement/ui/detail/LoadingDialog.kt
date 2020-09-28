package com.twt.service.announcement.ui.detail

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.github.ybq.android.spinkit.SpinKitView
import com.github.ybq.android.spinkit.style.Wave
import com.twt.service.announcement.R

class LoadingDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        var loadingBar = findViewById(R.id.loadingBar) as SpinKitView
        loadingBar.setIndeterminateDrawable(Wave())
        super.onWindowFocusChanged(hasFocus)
    }

    fun showDialog(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): LoadingDialog {
        return showDialog(context, null, cancelable, cancelListener)
    }

    lateinit var dialog: LoadingDialog
    fun showDialog(context: Context, message: CharSequence?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?): LoadingDialog {
        dialog = LoadingDialog(context, R.style.LoadingDialog)
        dialog.setContentView(R.layout.layout_loadingdialog);
        var m = dialog.findViewById(R.id.message) as TextView
        if (!TextUtils.isEmpty(message)) {
            m.text = message
        } else {
            m.visibility = View.GONE
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(cancelable)
        dialog.setOnCancelListener(cancelListener)
        dialog.window.attributes.gravity = Gravity.CENTER
        val lp = dialog.window.attributes
        lp.dimAmount = 0.2f
        dialog.window.attributes = lp
        dialog.show()
        return dialog
    }

    fun dismissDialog() {
        if (dialog.isShowing)
            dialog.dismiss()
    }
}