package com.twt.service.announcement.ui.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import com.twt.service.announcement.service.*
import com.twt.service.announcement.ui.activity.detail.ReleasePicAdapter
import com.twt.service.announcement.ui.activity.detail.noSelectPic
import com.twt.service.announcement.ui.main.MyLinearLayoutManager
import com.twt.service.announcement.ui.main.TagBottomItem
import com.twt.service.announcement.ui.main.TagsDetailItem
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.experimental.preference.CommonPreferences
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.*

class AskQuestionActivity : AppCompatActivity() {

    //输入的标题和问题内容
    private var idd: Int = 0
    private lateinit var title: EditText
    private lateinit var detail: EditText
    private lateinit var releasePicAdapter: ReleasePicAdapter
    private lateinit var progressDialog: ProgressDialog
    private lateinit var picRecyclerView: RecyclerView // 添加多图的recycler
    private val picRecyclerViewManager = LinearLayoutManager(this)
    private var selectPicList = mutableListOf<Any>() //选择上传的图片
    private lateinit var tagPathRecyclerView: RecyclerView
    private lateinit var tagListRecyclerView: RecyclerView
    private val tagTree by lazy { mutableMapOf<Int, List<Item>>() }
    private val pathTags by lazy { ItemManager() }
    private val listTags by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_ask_question_activity)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<ImageView>(R.id.anno_back).setOnClickListener {
            showExitDialog()
        }
        title = findViewById(R.id.edit_title)
        detail = findViewById(R.id.edit_content)

        detail.apply {
            setOnEditorActionListener { _, actionId, event ->
                var flag = true
                if (actionId == EditorInfo.IME_ACTION_SEND || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    AskQuestion(title.text.trim().toString(), detail.text.trim().toString())

                } else {
                    flag = false
                }
                flag
            }
        }
        initRecyclerView()
        initTagTree()
        selectPicList.add(noSelectPic) // supply a null list
        releasePicAdapter = ReleasePicAdapter(selectPicList, this, this)

        picRecyclerView = findViewById<RecyclerView>(R.id.anno_release_pic).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = picRecyclerViewManager
                adapter = releasePicAdapter
            }
        }

        findViewById<Button>(R.id.anno_release_button).apply {
            this.setOnClickListener {
                onClick()
            }
        }

    }


    private fun initRecyclerView() {
        tagPathRecyclerView = findViewById<RecyclerView>(R.id.path_rec2).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(pathTags)
            itemAnimator = SlideInDownAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.changeDuration = 300
                it.moveDuration = 500
            }

        }

        tagListRecyclerView = findViewById<RecyclerView>(R.id.detail_rec2).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(listTags).also {
                it.setHasStableIds(true)
            }
            itemAnimator = SlideInDownAnimator()
            itemAnimator?.let {
                it.addDuration = 300
                it.removeDuration = 800
                it.moveDuration = 300
                it.changeDuration = 500
            }
        }
    }

    private fun initTagTree() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            AnnoService.getTagTree().awaitAndHandle {
//                it.printStackTrace()
                Toast.makeText(this@AskQuestionActivity, "出了点问题", Toast.LENGTH_SHORT).show()
            }?.data?.let {
                pathTags.clear()
                pathTags.add(firstItem)
                Log.d("tag_tree", it.toString())
                bindTagPathWithDetailTag(it)
            }
        }
    }


    private fun AskQuestion(detailTitle: String, description: String) {

        title.setText(detailTitle)
        detail.setText(description)

    }

    //根据question_id可以上传图片(useId+Img文件）

    //点击确认发布事件,绑定在button上
    private fun onClick() {
        val titleString = title.text.toString()
        val detailString = detail.text.toString()

        //传入图片
        val picListOfUri = mutableListOf<Uri?>()
        val picListOfString = mutableListOf<String?>()

        selectPicList.forEach {
            if (it is Uri) {
                picListOfUri.add(it)
            }
            if (it is String) {
                picListOfString.add(it)
            }
        }

        Log.d("myID", AnnoPreference.myId.toString())

        var file1: File
        Log.d("dfsdfsf",picListOfUri.toString())
        val listOfFile = mutableListOf<File?>()
        try {
            if (!judgeNull(picListOfUri)) {
                for (i in picListOfUri) {
                    if (i != null) {
                        file1 = File.createTempFile("pic", ".jpg")
                        val outputFile = file1.path
                        //val outputfile=getFile(zipThePic(handleImageOnKitKat(i)),outputFile)
                        listOfFile.add(getFile(zipThePic(handleImageOnKitKat(i)), outputFile))

                    }
                }
                val pics = listOfFile.map {
                    val requestBody = RequestBody.create(MediaType.parse("image/jpg"), it)
                    MultipartBody.Part.createFormData("newImg", it?.name, requestBody)
                }

                //添加带图片的问题

                AnnoPreference.myId?.let { id ->
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        AnnoService.addQuestion(mapOf("user_id" to id, "name" to titleString, "description" to detailString, "tagList" to listOf<Int>(idd))).awaitAndHandle(

                        )?.data?.let {
                            addPicture(id, pics, it.question_id)
                            Toast.makeText(this@AskQuestionActivity,"上传成功",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            } else {
                //添加不带图片的问题
                AnnoPreference.myId?.let { id ->
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        AnnoService.addQuestion(mapOf("user_id" to id, "name" to titleString, "description" to detailString, "tagList" to listOf<Int>(idd))).awaitAndHandle {
                            failCallBack("上传失败")
                        }?.let {
                            Log.d("itttttt",it.toString())
                           if (it.ErrorCode == 0) {
                                successCallBack(it.data!!)
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            //Log.d("Dsdsdd",e.message)
        }
    }

    suspend fun addPicture(userId: Int, picPaths: List<MultipartBody.Part>, quesId: Int): List<String> =
            mutableListOf<String>().apply {
                picPaths.map { pic ->
                    AnnoService.postPictures(user_id = userId, newImg = pic, question_id = quesId).awaitAndHandle {
                        println("addPicture error:" + it.message)
                        failCallBack("上传失败")
                    }?.data?.url?.let {
                        this.add(it)
                    }
                }
            }

    // request = 2 代表来自于系统相册，requestCode ！= 0 代表选择图片成功
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = if (requestCode == 2 && resultCode != 0) {
        val selectedPic = Matisse.obtainResult(data) //将选择的图片加入到上传的列表中
        releasePicAdapter.changePic(selectedPic[0]) //加载选择的图片
    } else {
        // do something
    }

    override fun onBackPressed() = showExitDialog()

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
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    private fun getFile(b: ByteArray, outputFile: String): File? {
        var stream: BufferedOutputStream? = null
        var file: File? = null
        try {
            file = File(outputFile)
            val fstream = FileOutputStream(file)
            stream = BufferedOutputStream(fstream)
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
    fun openSelectPic() = Matisse.from(this@AskQuestionActivity)
            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
            .countable(true)
            .maxSelectable(1)
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.Matisse_Zhihu)
            .forResult(2)

    // release界面中长按图片的dialog，可以删除或添加图片
    fun setPicEdit() {
        val list = arrayOf<CharSequence>("更改图片", "删除图片", "取消")
        val alertDialogBuilder = AlertDialog.Builder(this@AskQuestionActivity)
        alertDialogBuilder.setItems(list) { dialog, item ->
            when (item) {
                0 -> checkPermAndOpenPic()
                1 -> releasePicAdapter.removePic()
                2 -> dialog.dismiss()
            }
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    private fun judgeNull(list: List<Uri?>): Boolean {
        list.forEach {
            if (it != null) {
                return false
            }
        }
        return true
    }

    fun checkPermAndOpenPic() {
        // 检查存储权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            EasyPermissions.requestPermissions(this, "需要外部存储来提供必要的缓存", 0, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            openSelectPic()
        }
    }

    //分类部分的tag
    private fun bindTagPathWithDetailTag(_data: List<Tag>): List<Item> =
            mutableListOf<Item>().apply {
                val index = pathTags.itemListSnapshot.size
                index.takeIf { it == 1 }?.apply {
                    firstItem.onclick = {
                        tagTree.get(index)?.let { listTags.refreshAll(it) }
                        (0 until pathTags.itemListSnapshot.size - 1).forEach { _ ->
                            pathTags.removeAt(pathTags.size - 1)
                        }
                    }
                }

                tagTree[index] = _data.map { child ->
                    TagsDetailItem(child.name, child.id) {
                        idd = child.id
                        if (child.children.isNotEmpty()) {
                            pathTags.add(TagBottomItem(child.name, index) {
                                tagTree[index + 1]?.let {
                                    listTags.refreshAll(it)
                                }
                                (0 until pathTags.itemListSnapshot.size - index - 1).forEach { _ ->
                                    pathTags.removeAt(pathTags.size - 1)
                                    Log.e("delete tag", "de")
                                }
                            })
                            listTags.refreshAll(bindTagPathWithDetailTag(child.children))

                        } else {
                            // 到最后一层标签后，打印当前路径或其他操作
                            val path = pathTags.itemListSnapshot.map {
                                (it as TagBottomItem).content
                            }.toMutableList().apply {
                                this.add(child.id.toString())
                            }
                            pathTags.add(TagBottomItem(child.name,index){
                                tagTree[index + 1]?.let {
                                    listTags.refreshAll(it)
                                }
                                (0 until pathTags.itemListSnapshot.size - index - 1).forEach { _ ->
                                    pathTags.removeAt(pathTags.size - 1)
                                }
                                }
                            )
                            if((pathTags.itemListSnapshot[pathTags.size-2] as TagBottomItem).content==child.name)
                                pathTags.removeAt(pathTags.itemListSnapshot.lastIndex)
                            Log.d("tag_path", path.toString())
                        }

                    }
                }.also {
                    addAll(it)
                }
            }

    fun successCallBack(data: AddQuestion) {
        Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show()
        finish()
    }

    fun failCallBack(message: String) {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        Toasty.error(this, message, Toast.LENGTH_SHORT).show()
    }

    // 退出编辑发布或丢失页面的dialog
    private fun showExitDialog() = AlertDialog.Builder(this@AskQuestionActivity)
            .setTitle("放弃编辑吗～")
            .setCancelable(false)
            .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
            .setNegativeButton("确定") { _, _ -> this@AskQuestionActivity.finish() }
            .create()
            .show()
}