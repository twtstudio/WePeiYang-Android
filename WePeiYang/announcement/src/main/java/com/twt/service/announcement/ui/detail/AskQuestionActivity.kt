package com.twt.service.announcement.ui.detail

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
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.githang.statusbar.StatusBarCompat
import com.ms.square.android.expandabletextview.ExpandableTextView
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoPreference
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.Tag
import com.twt.service.announcement.ui.activity.detail.ReleasePicAdapter
import com.twt.service.announcement.ui.activity.detail.noSelectPic
import com.twt.service.announcement.ui.main.MyLinearLayoutManager
import com.twt.service.announcement.ui.main.TagBottomItem
import com.twt.service.announcement.ui.main.TagsDetailItem
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.awaitAndHandle
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.*

class AskQuestionActivity : AppCompatActivity(), LoadingDialogManager {

    override val loadingDialog by lazy { LoadingDialog(this) }

    //输入的标题和问题内容
    private lateinit var title: EditText
    private lateinit var detail: EditText

    private lateinit var releasePicAdapter: ReleasePicAdapter

    private lateinit var departmentDescription: ExpandableTextView

    private lateinit var picRecyclerView: RecyclerView // 添加多图的recycler
    private val picRecyclerViewManager = LinearLayoutManager(this)
    private var selectPicList = mutableListOf<Any>() //选择上传的图片
    private lateinit var tagPathRecyclerView: RecyclerView
    private lateinit var tagListRecyclerView: RecyclerView
    private val tagTree by lazy { mutableMapOf<Int, List<Item>>() }
    private val pathTags by lazy { ItemManager() }
    private val listTags by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }
    private var selectedTagId = -1        //选择的tag的ID
    private var selectedIndex = -1       //选择的tag的Index
    private val tagList = mutableListOf<Tag>()
    private var selectedCampus = 0
    private lateinit var selectedCampusGroup:RadioGroup
    private lateinit var selectedCampus1Button:RadioButton
    private lateinit var selectedCampus2Button:RadioButton
    private lateinit var publishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_ask_question_activity)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<ImageView>(R.id.anno_back).setOnClickListener {
            showExitDialog()
        }

        selectedCampusGroup = findViewById(R.id.radio_campus)
        selectedCampus1Button = findViewById(R.id.radio_Weijinlu)
        selectedCampus2Button = findViewById(R.id.radio_Beiyangyuan)
        selectedCampusGroup.setOnCheckedChangeListener{_,checkedId ->
        when(checkedId){
            R.id.radio_Weijinlu ->{
                selectedCampus = 1
            }
            R.id.radio_Beiyangyuan ->{
                selectedCampus = 2
                Log.d("yankaqiu",selectedCampus.toString())
            }
        }
    }
        title = findViewById(R.id.edit_title)
        detail = findViewById(R.id.edit_content)
        departmentDescription = findViewById(R.id.expand_text_view)
        departmentDescription.setText("部门介绍")
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

        publishButton = findViewById<Button>(R.id.anno_release_button).apply {
            this.setOnClickListener {
                //点击button时显示progressDialog
                isEnabled = false
                if (selectedTagId != -1) {
                    if (title.text.isNotEmpty() && detail.text.isNotEmpty()) {
                        confirmAgain {
                            showLoadingDialog(this@AskQuestionActivity)
                            onClick()
                        }
                    } else if (title.text.isEmpty() && detail.text.isNotEmpty()) {
                        showDialog("请为问题添加标题")
                    } else if (detail.text.isEmpty() && title.text.isNotEmpty()) {
                        showDialog("请为问题添加描述")
                    } else {
                        showDialog("请为问题添加标题和描述")
                    }
                } else {
                    showDialog("请在分类栏至少选择一个标签！\n 如不确定问题所属标签，或没找到您问题所属的标签，请选择\" 其他\" ")
                }
                //hideLoadingDialog()
            }
        }

    }


    private fun showDialog(text: String) {
        AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage(text)
                .setPositiveButton("好的") { dialog, _ ->
                    dialog?.dismiss()
                }.show()
        publishButton.isEnabled = true
    }

    private fun initRecyclerView() {
        tagListRecyclerView = findViewById<RecyclerView>(R.id.detail_rec2).apply {
            layoutManager = MyLinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = ItemAdapter(listTags).also {
                it.setHasStableIds(true)
            }
        }
    }


    private fun initTagTree() {
        GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
            AnnoService.getTagTree().awaitAndHandle {
                it.printStackTrace()
                Toast.makeText(this@AskQuestionActivity, "出了点问题", Toast.LENGTH_SHORT).show()
            }?.data?.let {
                pathTags.clear()
                pathTags.add(firstItem)
                Log.d("tag_tree", it.toString())
                tagList.addAll(it[0].children)
                listTags.refreshAll(bindTagPathWithDetailTag(it[0].children))
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

        var file1: File
        val listOfFile = mutableListOf<File?>()
        try {
            if (!judgeNull(picListOfUri)) {
                for (i in picListOfUri) {
                    if (i != null) {
                        file1 = File.createTempFile("pic", ".jpg")
                        val outputFile = file1.path

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
                        AnnoService.addQuestion(mapOf("user_id" to id, "name" to titleString, "description" to detailString, "tagList" to listOf<Int>(selectedTagId),"campus" to selectedCampus)).awaitAndHandle {
                            hideLoadingDialog()
                            failCallBack("上传失败")
                            Log.d("addQuestion error:", it.message)
                        }?.let {
                            if (it.ErrorCode == 0) {
                                it.data?.question_id?.let {
                                    addPicture(id, pics, it)
                                    //判断拿到数据后，不显示dialog（hide)
                                    hideLoadingDialog()
                                    successCallBack()
                                }
                            } else {
                                hideLoadingDialog()
                                failCallBack("上传失败")
                            }
                        }
                    }
                }
            } else {
                //添加不带图片的问题
                AnnoPreference.myId?.let { id ->
                    GlobalScope.launch(Dispatchers.Main + QuietCoroutineExceptionHandler) {
                        AnnoService.addQuestion(mapOf("user_id" to id, "name" to titleString, "description" to detailString, "tagList" to listOf<Int>(selectedTagId),"campus" to selectedCampus)).awaitAndHandle {
                            hideLoadingDialog()
                            failCallBack("上传失败")
                            Log.d("addQuestion error:", it.message)
                        }?.let {
                            if (it.ErrorCode == 0) {
                                hideLoadingDialog()
                                successCallBack()
                            } else {
                                hideLoadingDialog()
                                failCallBack("上传失败")
                            }
                            //判断拿到数据后，不显示dialog（hide)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun confirmAgain(todo: () -> Unit) {
        AlertDialog.Builder(this)
                .setTitle("再次确认是否提交")
                .setPositiveButton("好的") { dialog, _ ->
                    dialog?.dismiss()
                    todo.invoke()
                }
                .setNegativeButton("再看看") { dialog, _ ->
                    dialog?.dismiss()
                    publishButton.isEnabled = true
                }
                .show()
    }

    suspend fun addPicture(userId: Int, picPaths: List<MultipartBody.Part>, quesId: Int): List<String> =
            mutableListOf<String>().apply {
                picPaths.map { pic ->
                    AnnoService.postPictures(user_id = userId, newImg = pic, question_id = quesId).awaitAndHandle {
                        println("addPicture error:" + it.message)
                        hideLoadingDialog()
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

    //选校区的tag
    private fun CampusTag(campusId:Int){

    }

    //分类部分的tag
    private fun bindTagPathWithDetailTag(_data: List<Tag>): List<Item> =
            mutableListOf<Item>().apply {
                val index = pathTags.itemListSnapshot.size
                index.takeIf { it == 1 }?.apply {
                    tagTree[index]?.let { listTags.refreshAll(it) }
                    tagListRecyclerView.visibility = View.VISIBLE

                }

                tagTree[index] = _data.map { child ->
                    TagsDetailItem(child.name, child.id, child.id == selectedTagId) {
                        if (selectedTagId == child.id) {
                            selectedTagId = -1
                        } else {
                            selectedTagId = child.id

                        }

                            selectedIndex = tagList.indexOf(child)
                            Log.d("tag_p", selectedIndex.toString())
                            Log.d("tag_p", tagList[selectedIndex].name.toString())
                            Log.d("tag_p", tagList[selectedIndex].description)
                            if(selectedIndex!=-1) {
                                departmentDescription.setText(tagList[selectedIndex].description)
                            }


                        listTags.removeAll(listTags)
                        listTags.refreshAll(bindTagPathWithDetailTag(_data))
//                             到最后一层标签后，打印当前路径或其他操作
                        val path = pathTags.itemListSnapshot.map {
                            (it as TagBottomItem).content
                        }.toMutableList().apply {
                            this.add(child.id.toString())

                        }

                        pathTags.add(TagBottomItem(child.name, index) {
                            tagTree[index + 1]?.let {
                                listTags.refreshAll(it)
                            }
                            (0 until pathTags.itemListSnapshot.size - index - 1).forEach { _ ->
                                pathTags.removeAt(pathTags.size - 1)
                            }
                        }
                        )
                        if ((pathTags.itemListSnapshot[pathTags.size - 2] as TagBottomItem).content == child.name)
                            pathTags.removeAt(pathTags.itemListSnapshot.lastIndex)
//                            tagListRecyclerView.visibility = View.GONE
                        Log.d("tag_path", path.toString())

//                        }

                    }
                }.also {
                    addAll(it)
                }
            }

    fun successCallBack() {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun failCallBack(message: String) {
        Toasty.error(this, message, Toast.LENGTH_SHORT).show()
        publishButton.isEnabled = true
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