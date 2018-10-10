package com.twt.service.schedule2.view.custom

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import com.twt.service.schedule2.R
import com.twt.service.schedule2.model.SchedulePref
import com.twt.service.schedule2.model.total.TotalCourseManager
import com.twt.service.schedule2.view.audit.AuditActivity
import com.twt.service.schedule2.view.schedule.ScheduleActivity
import com.twt.service.schedule2.view.theme.spreadChainLayout
import com.twt.wepeiyang.commons.experimental.color.getColorCompat
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.experimental.theme.CustomTheme
import com.twt.wepeiyang.commons.ui.view.colorCircleView
import es.dmoral.toasty.Toasty
import io.multimoon.colorful.Colorful
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.support.v4.nestedScrollView

class CustomSettingBottomFragment : BottomSheetDialogFragment() {
    companion object {
        private const val TAG_SHARE_BS_DIALOG_FRAGMENT = "CustomSettingBottomFragment"
        private val cacheFragment = CustomSettingBottomFragment()

        fun showCustomSettingsBottomSheet(activity: AppCompatActivity) {
            val fragmentManager = activity.supportFragmentManager
            var fragment = fragmentManager.findFragmentByTag(TAG_SHARE_BS_DIALOG_FRAGMENT) as? CustomSettingBottomFragment
            if (fragment == null) {
                fragment = cacheFragment
            }
            if (fragment.isAdded) return
            fragment.show(fragmentManager, TAG_SHARE_BS_DIALOG_FRAGMENT)
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        if (dialog == null) return
        val context = dialog.context
        val view = context.nestedScrollView {
            verticalLayout {
                constraintLayout {
                    backgroundColor = getColorCompat(R.color.colorPrimary)

                    val titleText = textView {
                        text = "课程表设置"
                        id = View.generateViewId()
                        textSize = 20f
                        textColor = Color.WHITE
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                        topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                        bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                        margin = dip(16)
                    }

                }

                indicator("自定义课程/蹭课")

                constraintLayout {
                    backgroundColor = Color.WHITE

                    textView {
                        text = "进入自定义课程设置"
                        textSize = 14f
                        textColor = Color.BLACK
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = PARENT_ID
                        topToTop = PARENT_ID
                        bottomToBottom = PARENT_ID
                        leftMargin = dip(16)
                    }
                }.lparams(width = matchParent, height = dip(48)).apply {
                    setOnClickListener {
                        val intent = Intent(activity, AuditActivity::class.java)
                        activity?.startActivity(intent)
                    }
                }

                indicator("分享课程")

                constraintLayout {
                    backgroundColor = Color.WHITE

                    textView {
                        text = "点击分享自己的课程表"
                        textSize = 14f
                        textColor = Color.BLACK
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = PARENT_ID
                        topToTop = PARENT_ID
                        bottomToBottom = PARENT_ID
                        leftMargin = dip(16)
                    }
                }.lparams(width = matchParent, height = dip(48)).apply {
                    setOnClickListener {
                        val activity = activity as? ScheduleActivity
                        activity?.shareSchedule()
                    }
                }

                indicator("课程表界面设置")

                constraintLayout {
                    backgroundColor = Color.WHITE

                    textView {
                        text = "自动隐藏周六日"
                        textSize = 14f
                        textColor = Color.BLACK
                    }.lparams(width = wrapContent, height = wrapContent) {
                        startToStart = PARENT_ID
                        topToTop = PARENT_ID
                        bottomToBottom = PARENT_ID
                        leftMargin = dip(16)
                    }

                    switch {
                        isChecked = SchedulePref.autoCollapseSchedule
                        lollipop {
                            thumbDrawable.setColorFilter(getColorCompat(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
                            trackDrawable.setColorFilter(getColorCompat(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
                        }
                        onCheckedChange { _, isChecked ->
                            SchedulePref.autoCollapseSchedule = isChecked
                            post {
                                if (CommonPreferences.realName.contains("舒")) {
                                    Toasty.success(dialog.context, "给傲娇的舒子同学递课表").show() // 彩蛋
                                }
                                TotalCourseManager.invalidate()
                            }
                        }
                    }.lparams {
                        topToTop = PARENT_ID
                        bottomToBottom = PARENT_ID
                        endToEnd = PARENT_ID
                        rightMargin = dip(16)
                    }
                }.lparams(width = matchParent, height = dip(48))

                indicator("主题设置(课程表试点)")

                spreadChainLayout {
                    listOf(
                            "Coal" to Color.parseColor("#363636"),
                            "Nordic" to Color.parseColor("#738d91"),
                            "Rose" to Color.parseColor("#ae837b"),
                            "Matcha" to Color.parseColor("#748165"),
                            "Gold" to Color.parseColor("#7c6a41")
                    ).forEachIndexed { index, (name, color) ->

                        verticalLayout {
                            colorCircleView {
                                this.color = color
                            }.lparams {
                                width = dip(24)
                                height = dip(24)
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                            textView {
                                text = name
                                textColor = color
                                textSize = 12f
                                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                            }.lparams(width = wrapContent, height = wrapContent) {
                                topMargin = dip(6)
                            }
                        }.lparams {
                            width = wrapContent
                            height = wrapContent
                            horizontalPadding = dip(16)
                        }.setOnClickListener {
                            val theme = CustomTheme.themeList[index]
                            Log.e(TAG, "custom theme: $theme")
                            val activity = this@CustomSettingBottomFragment.activity
                            Colorful().edit()
                                    .setPrimaryColor(theme)
                                    .setAccentColor(theme)
                                    .apply(context) {
                                        activity?.recreate()
                                    }
                        }


                    }
                }.lparams(width = matchParent, height = wrapContent) {
                    topMargin = dip(12)
                    bottomMargin = dip(12)
                }

            }
        }

        dialog.setContentView(view)
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun _LinearLayout.indicator(indicatorText: String) = frameLayout {
        textView {
            text = indicatorText
            textColor = getColorCompat(R.color.colorPrimary)
            textSize = 12f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }.lparams(width = matchParent, height = wrapContent) {
            leftMargin = dip(8)
            topMargin = dip(8)
        }
    }.lparams(width = matchParent, height = wrapContent)

    fun lollipop(block: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            block()
        }
    }

}