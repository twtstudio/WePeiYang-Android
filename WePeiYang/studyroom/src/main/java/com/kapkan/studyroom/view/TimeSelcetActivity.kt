package com.kapkan.studyroom.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.example.studyroom.R
import kotlinx.android.synthetic.main.activity_timeselect.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

class TimeSelcetActivity : AppCompatActivity(){

    var select:BooleanArray = BooleanArray(12)
    val list:ArrayList<LinearLayout> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeselect)
        select = intent.getBooleanArrayExtra("class")
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init(){

        list.add(class1n)
        list.add(class2n)
        list.add(class3n)
        list.add(class4n)
        list.add(class5n)
        list.add(class6n)
        list.add(class7n)
        list.add(class8n)
        list.add(class9n)
        list.add(class10n)
        list.add(class11n)
        list.add(class12n)


        study__back.onClick {
            val intent = Intent(this@TimeSelcetActivity,StudyActivity::class.java)
            intent.putExtra("class",select)
            setResult(Activity.RESULT_OK,intent)
            onBackPressed()
        }

        class1n.onClick {
            if (!select[0]){
                select[0] = true
                class1n.background = getDrawable(R.drawable.classroom_selected)
                class1text.textColor = getColor(R.color.white)
            }else {
                class1n.background = getDrawable(R.drawable.classroom_unselect)
                class1text.textColor = getColor(R.color.selected)
                select[0] = false
            }
        }
        class2n.onClick {
            if (!select[1]){
                select[1] = true
                class2n.background = getDrawable(R.drawable.classroom_selected)
                class2text.textColor = getColor(R.color.white)
            }else {
                class2n.background = getDrawable(R.drawable.classroom_unselect)
                class2text.textColor = getColor(R.color.selected)
                select[1] = false
            }
        }

        class3n.onClick {
            if (!select[2]){
                select[2] = true
                class3n.background = getDrawable(R.drawable.classroom_selected)
                class3text.textColor = getColor(R.color.white)
            }else {
                class3n.background = getDrawable(R.drawable.classroom_unselect)
                class3text.textColor = getColor(R.color.selected)
                select[2] = false
            }
        }

        class4n.onClick {
            if (!select[3]){
                select[3] = true
                class4n.background = getDrawable(R.drawable.classroom_selected)
                class4text.textColor = getColor(R.color.white)
            }else {
                class4n.background = getDrawable(R.drawable.classroom_unselect)
                class4text.textColor = getColor(R.color.selected)
                select[3] = false
            }
        }

        class5n.onClick {
            if (!select[4]){
                select[4] = true
                class5n.background = getDrawable(R.drawable.classroom_selected)
                class5text.textColor = getColor(R.color.white)
            }else {
                class5n.background = getDrawable(R.drawable.classroom_unselect)
                class5text.textColor = getColor(R.color.selected)
                select[4] = false
            }
        }
        class6n.onClick {
            if (!select[5]){
                select[5] = true
                class6n.background = getDrawable(R.drawable.classroom_selected)
                class6text.textColor = getColor(R.color.white)
            }else {
                class6n.background = getDrawable(R.drawable.classroom_unselect)
                class6text.textColor = getColor(R.color.selected)
                select[5] = false
            }
        }

        class7n.onClick {
            if (!select[6]){
                select[6] = true
                class7n.background = getDrawable(R.drawable.classroom_selected)
                class7text.textColor = getColor(R.color.white)
            }else {
                class7n.background = getDrawable(R.drawable.classroom_unselect)
                class7text.textColor = getColor(R.color.selected)
                select[6] = false
            }
        }

        class8n.onClick {
            if (!select[7]){
                select[7] = true
                class8n.background = getDrawable(R.drawable.classroom_selected)
                class8text.textColor = getColor(R.color.white)
            }else {
                class8n.background = getDrawable(R.drawable.classroom_unselect)
                class8text.textColor = getColor(R.color.selected)
                select[7] = false
            }
        }

        class9n.onClick {
            if (!select[8]){
                select[8] = true
                class9n.background = getDrawable(R.drawable.classroom_selected)
                class9text.textColor = getColor(R.color.white)
            }else {
                class9n.background = getDrawable(R.drawable.classroom_unselect)
                class9text.textColor = getColor(R.color.selected)
                select[8] = false
            }
        }

        class10n.onClick {
            if (!select[9]){
                select[9] = true
                class10n.background = getDrawable(R.drawable.classroom_selected)
                class10text.textColor = getColor(R.color.white)
            }else {
                class10n.background = getDrawable(R.drawable.classroom_unselect)
                class10text.textColor = getColor(R.color.selected)
                select[9] = false
            }
        }

        class11n.onClick {
            if (!select[10]){
                select[10] = true
                class11n.background = getDrawable(R.drawable.classroom_selected)
                class11text.textColor = getColor(R.color.white)
            }else {
                class11n.background = getDrawable(R.drawable.classroom_unselect)
                class11text.textColor = getColor(R.color.selected)
                select[10] = false
            }
        }

        class12n.onClick {
            if (!select[11]){
                select[11] = true
                class12n.background = getDrawable(R.drawable.classroom_selected)
                class12text.textColor = getColor(R.color.white)
            }else {
                class12n.background = getDrawable(R.drawable.classroom_unselect)
                class12text.textColor = getColor(R.color.selected)
                select[11] = false
            }
        }

        classroom_confirm.onClick {
            val intent = Intent(this@TimeSelcetActivity,StudyActivity::class.java)
            intent.putExtra("class",select)
            setResult(Activity.RESULT_OK,intent)
            onBackPressed()
        }

        for(i in 0 .. 11){
            if (select[i]){
                select[i] = false
                list[i].callOnClick()
            }
        }
    }
}