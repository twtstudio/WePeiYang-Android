package com.twtstudio.service.dishesreviews.evaluate

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.dish.model.DishesFoodProvider
import com.twtstudio.service.dishesreviews.evaluate.model.EvaluateProvider
import com.twtstudio.service.dishesreviews.extensions.displayImage
import com.twtstudio.service.dishesreviews.extensions.displayImageFromLocal
import com.twtstudio.service.dishesreviews.extensions.getRealFilePath
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.dishes_reviews_activity_evaluate.*
import kotlinx.android.synthetic.main.dishes_reviews_item_evaluate_comment.*
import kotlinx.android.synthetic.main.dishes_reviews_item_evaluate_score.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class EvaluateActivity : AppCompatActivity() {
    private val REQUEST_CODE_CHOOSE = 200
    private lateinit var toolbar: Toolbar
    private lateinit var tvTitle: TextView
    private lateinit var imgDish: ImageView
    private lateinit var tvDishName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var ratingBar: RatingBar
    private var foodId = 0
    private var singlSelectedCTVs = SingleSelector()
    private val postMap = mutableMapOf<String, RequestBody>()
    private val imageSelectedList = mutableListOf<Uri>()
    private lateinit var commentImgs: Array<ImageView>
    private var maxSelectable = 4
    private val partList = mutableListOf<MultipartBody.Part>()
    private val mediaType = MediaType.parse("multipart/form-data")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_evaluate)
        EasyPermissions.requestPermissions(this, "微北洋需要外部存储来提供必要的缓存", 0, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        foodId = intent.getIntExtra("FoodId", 0)
        postMap.put("foodId", RequestBody.create(mediaType, foodId.toString()))
        commentImgs = arrayOf(iv_image1, iv_image2, iv_image3, iv_image4)
        for (i in 1..commentImgs.size - 1) {
            commentImgs.get(i).apply {
                visibility = View.GONE
                isClickable = false
            }
        }

        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        setDisplayHomeAsUpEnabled(true)
        tvTitle = findViewById<TextView>(R.id.tv_toolbar_title).apply {
            text = "点评"
        }
        imgDish = findViewById(R.id.img_dish)
        tvDishName = findViewById(R.id.tv_dish_name)
        tvLocation = findViewById(R.id.tv_location)
        ratingBar = findViewById(R.id.ratingbar_evaluate)
        DishesFoodProvider.getDishesFood(foodId, this).bindNonNull(this) {
            imgDish.displayImage(this, it.foodInfo.food_picture_address, ImageView.ScaleType.CENTER_CROP)
            tvDishName.text = it.foodInfo.food_name
            tvLocation.text = it.foodInfo.canteen_name + it.foodInfo.food_floor + "层" + it.foodInfo.food_window + "窗口"
        }
        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            postMap.put("foodScore", RequestBody.create(mediaType, fl.toInt().toString()))
        }
        singlSelectedCTVs.apply {
            add(findViewById(R.id.tv_time_breakfast))
            add(findViewById(R.id.tv_time_lunch))
            add(findViewById(R.id.tv_time_dinner))
            setListener {}
        }

        tv_tag_1.setOnClickListener {
            (it as CheckedTextView).toggle()
            postMap.put("attitude", RequestBody.create(mediaType, "1"))
        }
        tv_tag_2.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
        tv_tag_3.setOnClickListener {
            (it as CheckedTextView).toggle()
        }
        button_submit.setOnClickListener {
            if (cb_anonymous.isChecked)
                postMap.put("commentIsAnonymous", RequestBody.create(mediaType, "1"))
            else
                postMap.put("commentIsAnonymous", RequestBody.create(mediaType, "0"))
            postMap.put("commentContent", RequestBody.create(mediaType, edt_comment.text.toString()))
            EvaluateProvider.postEvaluate(postMap, partList, this@EvaluateActivity).bindNonNull(this@EvaluateActivity) {
                Toast.makeText(this@EvaluateActivity, "评论成功", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        for (i in commentImgs.indices)
            commentImgs.get(i).setOnClickListener {
                if (imageSelectedList.size <= 4)
                    maxSelectable = 4 - imageSelectedList.size
                openImageSelector()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            imageSelectedList.addAll(Matisse.obtainResult(data))
            for (i in imageSelectedList.indices) {
                commentImgs.get(i).apply {
                    displayImageFromLocal(this@EvaluateActivity, imageSelectedList.get(i).toString())
                    visibility = View.VISIBLE
                    isClickable = false
                };
                val file = File(getRealFilePath(this, imageSelectedList.get(i)))
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                partList.add(MultipartBody.Part.createFormData("commentPicture${i + 1}", file.getName(), requestFile))
            }
            if (imageSelectedList.size < 4)
                commentImgs.get(imageSelectedList.size).apply {
                    visibility = View.VISIBLE
                    isClickable = true
                }
        }
    }
    private fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(showHomeAsUp)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
    }

    private fun openImageSelector() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                .captureStrategy(CaptureStrategy(true, "com.twt.service.fileProvider")) // 拍照的图片路径
                .maxSelectable(maxSelectable)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE)
    }
}