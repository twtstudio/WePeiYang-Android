package com.twt.service.announcement.ui.detail

import android.app.Dialog
import android.content.Context

interface LoadingDialogManager {
    val loadingDialog: LoadingDialog

    fun showLoadingDialog(context: Context) {
        loadingDialog.showDialog(context, "加载中", true, null)
    }

    fun hideLoadingDialog() {
        loadingDialog.dismissDialog()
    }

}