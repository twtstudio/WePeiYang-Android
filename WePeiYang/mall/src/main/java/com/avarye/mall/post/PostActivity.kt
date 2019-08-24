package com.avarye.mall.post

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import com.avarye.mall.R
import com.avarye.mall.service.*
import com.twt.wepeiyang.commons.experimental.cache.CacheIndicator
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_post.*
import kotlinx.android.synthetic.main.mall_item_spinner_category.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*
import kotlin.math.roundToInt

class PostActivity : AppCompatActivity() {

    private lateinit var postImgAdapter: PostImgAdapter
    private val postImgManager = LinearLayoutManager(this)
    private val viewModel = ViewModel()
    private var token = ""
    private var selectPicList = mutableListOf<Any>()
    private var status = ""
    private var category = ""
    private var categoryMain = ""
    private var iid = ""
    private var iidTemp = ""
    private var flagSale = false
    private var flagNeed = false
    private var type = 0
    private lateinit var map: Map<String, Any>
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_post)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)
        type = intent.getIntExtra("type", 3)
        tb_main.apply {
            title = when (type) {
                1 -> getString(R.string.mallStringPostSale)
                2 -> getString(R.string.mallStringPostNeed)
                else -> "薛定谔的页面"
            }
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { showExitDialog() }
        }
        progressBar = pb_post
        progressBar.visibility = View.GONE

        when (type) {
            1 -> {
                tv_post_img.visibility = View.VISIBLE
                rv_post_img.visibility = View.VISIBLE
                rl_post_bargain.visibility = View.VISIBLE
                tv_post_price.text = "价格"
            }
            2 -> {
                tv_post_img.visibility = View.GONE
                rv_post_img.visibility = View.GONE
                rl_post_bargain.visibility = View.GONE
                tv_post_price.text = "期望价格"
            }
        }

        loginLiveData.bindNonNull(this) {
            token = it.token
        }

        menuLiveData.refresh(CacheIndicator.REMOTE)
        initSpinnerStatus()
        initSpinnerCategory()
        sp_post_status.setSelection(0)
        sp_category_main.setSelection(0)
        sp_category.setSelection(0)

        selectPicList.add(NoSelectPic) // supply a null list
        postImgAdapter = PostImgAdapter(selectPicList, this, this)
        postImgManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_post_img.apply {
            adapter = postImgAdapter
            layoutManager = postImgManager
        }

        tv_post_button.setOnClickListener {
            tv_post_button.isClickable = false
            map = dataToMap()//更新数据
            if (flagSale || flagNeed) {
                progressBar.visibility = View.VISIBLE
                post()
//                this.finish()
            } else {
                Toasty.info(this, "请填写完整").show()
                tv_post_button.isClickable = true
            }
        }
    }

    private fun initSpinnerStatus() {
        val spinnerListStatus = mutableListOf<String>()
        spinnerListStatus.apply {
            add("请选择")
            add("全新")
            add("99新")
            add("9成新")
            add("8成新")
            add("7成新")
            add("6成新")
            add("5成新")
            add("旧")
        }

        val adapter = ArrayAdapter(this, R.layout.mall_item_spinner_text, spinnerListStatus)
        adapter.setDropDownViewResource(R.layout.mall_item_spinner_dropdown)
        sp_post_status.apply {
            this.adapter = adapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    status = MallManager.setStatus(spinnerListStatus[p2]).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    private fun initSpinnerCategory() {
        val spinnerListMain = mutableListOf<String>()
        val spinnerListCategory = mutableListOf<String>()
        val menuList = mutableListOf<Menu>()

        spinnerListMain.add("请选择")
        spinnerListCategory.add("请选择")
        menuLiveData.bindNonNull(this@PostActivity) {
            menuList.clear()
            menuList.addAll(it)
            spinnerListMain.apply {
                clear()
                add("请选择")
                it.forEach { menu ->
                    this.add(menu.name)
                }
            }
        }

        val adapterMain = ArrayAdapter(this, R.layout.mall_item_spinner_text, spinnerListMain)
        adapterMain.setDropDownViewResource(R.layout.mall_item_spinner_dropdown)
        val adapterCategory = ArrayAdapter(this, R.layout.mall_item_spinner_text, spinnerListCategory)
        adapterCategory.setDropDownViewResource(R.layout.mall_item_spinner_dropdown)

        sp_category_main.apply {
            adapter = adapterMain
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position != 0) {
                        categoryMain = menuList[position - 1].id
                        category = ""
                    }

                    //另一个spinner
                    spinnerListCategory.apply {
                        clear()
                        add("请选择")
                        if (position != 0) {
                            menuList[position - 1].smalllist.forEach { menu ->
                                this.add(menu.name)
                            }
                        }
                    }

                    sp_category.apply {
                        adapter = adapterCategory
                        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                if (p2 != 0) {
                                    category = menuList[position - 1].smalllist[p2 - 1].id
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) = Unit
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit

            }
        }
    }

    private fun post() = when (type) {
        1 -> viewModel.postSale(map, token)
        2 -> viewModel.postNeed(map, token)
        else -> Unit
    }

    private fun dataToMap(): Map<String, Any> {
        val name = et_post_name.text.toString()
        val detail = et_post_detail.text.toString()
        val campus = when (rg_post_campus.checkedRadioButtonId) {
            rb_post_campus1.id -> 1
            rb_post_campus2.id -> 2
            else -> 1
        }.toString()
        val location = et_post_location.text.toString()
        val price = et_post_price.text.toString()
        val bargain = when (rg_post_bargain.checkedRadioButtonId) {
            rb_post_bargain0.id -> 0
            rb_post_bargain1.id -> 1
            rb_post_bargain2.id -> 2
            else -> 0
        }.toString()
        val exchange = et_post_exchange.text.toString()
        val phone = et_post_phone.text.toString()
        val qq = et_post_qq.text.toString()
        val weChat = et_post_wechat.text.toString()

        flagSale = name.isNotBlank() && detail.isNotBlank() && location.isNotBlank() && price.isNotBlank()
                && category.isNotBlank() && categoryMain.isNotBlank() && (phone + qq + weChat).isNotBlank()
                && status.isNotBlank() && bargain.isNotBlank()

        flagNeed = name.isNotBlank() && detail.isNotBlank() && location.isNotBlank() && price.isNotBlank()
                && category.isNotBlank() && categoryMain.isNotBlank() && (phone + qq + weChat).isNotBlank()

        val map = HashMap<String, Any>()
        map["name"] = name
        map["detail"] = detail
        map["campus"] = campus
        map["location"] = location
        map["price"] = price
        map["bargain"] = bargain
        map["category"] = category
        map["categoryMain"] = categoryMain
        map["status"] = status
        map["exchange"] = exchange
        map["iid"] = "4000"
        map["phone"] = phone
        map["email"] = weChat
        map["qq"] = qq

        return map
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
            if (requestCode == 2 && resultCode != 0) {
                val selectedPic = Matisse.obtainResult(data)

                postImg()
                imgIdLiveData.bindNonNull(this) {
                    if (it != iidTemp) {
                        iidTemp = it
                        iid += "$it,"
                        postImgAdapter.changePic(selectedPic[0])
                    }
                }

            } else {
            }

    private fun postImg() {
        selectPicList.last().apply {
            if (this is Uri) {
                val file: File = File.createTempFile("pic", ".jpg")
                val outputFile = file.path
                viewModel.postImg(getFile(zipThePic(handleImageOnKitKat(this)), outputFile)!!, token)
            }
        }
    }

    // 相册图片路径
    private fun handleImageOnKitKat(uri: Uri?): String? {
        var imagePath: String? = null

        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri?.authority) {
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri?.scheme, ignoreCase = true)) {
            imagePath = getImagePath(uri, null)
        }
        return imagePath
    }

    private fun getImagePath(uri: Uri?, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun zipThePic(filePath: String?): ByteArray {
        val options = BitmapFactory.Options()
        options.apply {
            inJustDecodeBounds = true
            inSampleSize = calculateInSampleSize(options, 480, 800)
            inJustDecodeBounds = false
        }
        val bitmap = BitmapFactory.decodeFile(filePath, options)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)

        return baos.toByteArray()
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    private fun getFile(b: ByteArray, outputFile: String): File? {
        var stream: BufferedOutputStream? = null
        var file: File? = null
        try {
            file = File(outputFile)
            val fStream = FileOutputStream(file)
            stream = BufferedOutputStream(fStream)
            stream.write(b)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
        return file
    }

    // 用第三方库打开相册
    @SuppressLint("ResourceType")
    fun openSelectPic() = Matisse.from(this@PostActivity)
            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
            .countable(true)
            .maxSelectable(1)
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.Matisse_Dracula)
            .forResult(2)

    fun setPicEdit() {
        val list = arrayOf<CharSequence>("更改图片", "删除图片", "取消")
        val alertDialogBuilder = AlertDialog.Builder(this@PostActivity)
        alertDialogBuilder.setItems(list) { dialog, item ->
            when (item) {
                0 -> checkPermAndOpenPic()
                1 -> postImgAdapter.removePic()
                2 -> dialog.dismiss()
            }
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    fun checkPermAndOpenPic() {
        // 检查存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            EasyPermissions.requestPermissions(this, "需要外部存储来提供必要的缓存", 0,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            openSelectPic()
        }
    }

    private fun showExitDialog() = AlertDialog.Builder(this)
            .setTitle("退出后无法保存呦")

            .setCancelable(false)
            .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("确定") { _, _ -> this.finish() }
            .create()
            .show()

    override fun onBackPressed() = showExitDialog()

}
