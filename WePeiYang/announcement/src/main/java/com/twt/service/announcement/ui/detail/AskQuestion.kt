package com.twt.service.announcement.ui.activity.detail

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
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.githang.statusbar.StatusBarCompat
import com.twt.service.announcement.R
import pub.devrel.easypermissions.EasyPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import java.io.*

class AskQuestion : AppCompatActivity() , View.OnClickListener{
    //输入的标题和问题内容
    private lateinit var title: EditText
    private lateinit var detail: EditText
    private var judgementOfRelease = false
    private lateinit var sortSpinner: Spinner
    private lateinit var releasePicAdapter: ReleasePicAdapter
    private var selectPicList = mutableListOf<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_question)

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        findViewById<Toolbar>(R.id.annoCommonToolbar).apply {
            this.title = "问题反馈"
            setSupportActionBar(this)
            supportActionBar?.apply {
                setHomeButtonEnabled(true)
                setDisplayHomeAsUpEnabled(true)
            }
            this.setNavigationOnClickListener {
                finish()
            }
        }
        title = findViewById(R.id.sort)
        detail = findViewById(R.id.edit_content)

    }

    //点击确认发布事件
    override fun onClick(p0: View?) {
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

    //得到用户所填内容


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
    fun openSelectPic() = Matisse.from(this@AskQuestion)
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
        val alertDialogBuilder = AlertDialog.Builder(this@AskQuestion)
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
