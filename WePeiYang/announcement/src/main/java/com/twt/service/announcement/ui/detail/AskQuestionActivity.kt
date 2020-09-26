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
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.githang.statusbar.StatusBarCompat
import com.github.clans.fab.FloatingActionMenu
import com.twt.service.announcement.R
import com.twt.service.announcement.service.AnnoService
import com.twt.service.announcement.service.CommonBody
import com.twt.service.announcement.service.Tag
import com.twt.service.announcement.service.UserId
import com.twt.service.announcement.ui.activity.detail.ReleasePicAdapter
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
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.*

class AskQuestionActivity : AppCompatActivity() {

    //输入的标题和问题内容
    private var idd:Int=0
    private lateinit var title: EditText
    private lateinit var detail: EditText
    private var judgementOfRelease = false
    private lateinit var sortSpinner: Spinner
    private lateinit var releasePicAdapter: ReleasePicAdapter
    private var selectPicList = mutableListOf<Any>()
    private lateinit var tagPathRecyclerView: RecyclerView
    private lateinit var tagListRecyclerView: RecyclerView
    private val tagTree by lazy { mutableMapOf<Int, List<Item>>() }
    private val pathTags by lazy { ItemManager() }
    private val listTags by lazy { ItemManager() }
    private val firstItem by lazy { TagBottomItem("天津大学", 0) {} }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_question_activity)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<ImageView>(R.id.anno_back).setOnClickListener {
            onBackPressed()
        }
        title = findViewById(R.id.edit_title)
        detail = findViewById(R.id.edit_content)

        detail.apply{
            setOnEditorActionListener { _, actionId, event ->
                var flag = true
                if (actionId == EditorInfo.IME_ACTION_SEND || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    AskQuestion(title.text.trim().toString(),detail.text.trim().toString())

                } else {
                    flag = false
                }
                flag
            }
        }
      initRecyclerView()
      initTagTree()

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

    private fun AskQuestion(detailTitle:String,description:String){

        title.setText(detailTitle)
        detail.setText(description)

    }
    //先通过hawk获得学生的学号和姓名，获取user_id
    private fun getUseId(): Int {
        var userrId: Int = 0
        GlobalScope.launch (Dispatchers.Main+ QuietCoroutineExceptionHandler ){
            AnnoService.getUserIDByName(CommonPreferences.studentid, CommonPreferences.realName).awaitAndHandle(

            )?.data?.let{
                userrId = it.user_id
            }
        }
        return userrId
    }
    //然后上传问题获得question_id
    private fun getQuestionid():Int{
        var questionId:Int=0
        GlobalScope.launch (Dispatchers.Main+ QuietCoroutineExceptionHandler){
            AnnoService.addQuestion(mapOf("user_id" to getUseId(),"name" to title,"description" to detail, "tagList" to listOf<Int>(idd))).awaitAndHandle(

            )?.data?.let{
                questionId=it.question_id
            }
        }
        return questionId
    }
    //根据question_id可以上传图片(useId+Img文件）

    //点击确认发布事件
     private fun onClick(p0: View?) {
        //然后上传问题获得question_id

        val questionId= AnnoService.addQuestion(mapOf("user_id" to 1,"name" to title,"description" to detail, "tagList" to listOf<Int>(1)))
        // AnnoService.postPictures(getUsId(),file,questionId)
        GlobalScope.launch (Dispatchers.Main+ QuietCoroutineExceptionHandler){
           // AnnoService.postPictures(getUseId(),file,getQuestionid())//将图片压缩成文件上传
        }


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
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // request = 2 代表来自于系统相册，requestCode ！= 0 代表选择图片成功
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = if (requestCode == 2 && resultCode != 0) {
        val selectedPic = Matisse.obtainResult(data) //将选择的图片加入到上传的列表中
        releasePicAdapter.changePic(selectedPic[0]) //加载选择的图片
    } else {
        // do something
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
                        if (child.children.isNotEmpty()) {
                            pathTags.add(TagBottomItem(child.name, index) {
                                tagTree[index + 1]?.let { listTags.refreshAll(it) }
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
                            Log.e("tag_path", path.toString())
                        }
                        idd=child.id
                    }
                }.also {
                    addAll(it)
                }
            }

}

//校验输入类型
/*
@Throws(PatternSyntaxException::class)
fun stringFilter(str: String?): String? {
    // 仅仅同意输入字母、数字和汉字
    val regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]"
    val p: Pattern = Pattern.compile(regEx)
    val m: Matcher = p.matcher(str)
    return m.replaceAll("").trim()
}


private fun stringFilter():InputFilter{
    // 仅仅同意输入字母、数字和汉字
    val regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]"
    fun filter(source: CharSequence, start: Int, end: Int,
               dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val destCount: Float = (dest.toString().length()
                + getChineseCount(dest.toString()))
        val sourceCount: Float = (source.toString().length
                + getChineseCount(source.toString()))
        return if (destCount + sourceCount > 10) {
            Log.e("log", "已经达到字数限制范围")
            ""
        } else {
            source
        }
    }

}



private fun inputTextListener(): TextWatcher? {
    return object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val length = s.length
            titleEditNum.setText("$length/$titleNum")
            questionEditNum.setText("$length/${questionNum}u")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int,
                                       after: Int) {
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}
*/

