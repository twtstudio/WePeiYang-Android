package xyz.rickygao.gpa2.spider.utils

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import es.dmoral.toasty.Toasty

class TjuLoginDialogReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("myTest", "广播测试")
        context?.let {
            AlertDialog.Builder(it)
                    .setPositiveButton("positive") { dialog, which ->
                        dialog.dismiss()
                    }.setNegativeButton("negative") { dialog, which ->
                        dialog.dismiss()

                    }.show()
//            Toasty.normal(it, "广播测试").show()

        }

    }
}