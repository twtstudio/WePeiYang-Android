package com.example.lostfond2.release

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.example.lostfond2.SuccessActivity
import com.example.lostfond2.service.DetailData
import com.example.lostfond2.service.MyListDataOrSearchBean
import com.example.lostfond2.service.PermissionsUtils
import com.example.lostfond2.service.Utils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_release.*
import kotlinx.android.synthetic.main.lf_common_toolbar.*
import kotlinx.android.synthetic.main.lf_release_cardview_cardinfo.*
import kotlinx.android.synthetic.main.lf_release_cardview_cardinfo_noname.*
import kotlinx.android.synthetic.main.lf_release_cardview_confirm.*
import kotlinx.android.synthetic.main.lf_release_cardview_contact.*
import kotlinx.android.synthetic.main.lf_release_cardview_delete.*
import kotlinx.android.synthetic.main.lf_release_cardview_info.*
import kotlinx.android.synthetic.main.lf_release_cardview_moreinfo.*
import kotlinx.android.synthetic.main.lf_release_cardview_publish.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ReleaseActivity : AppCompatActivity(), ReleaseContract.ReleaseView, View.OnClickListener {

    var duration = 1
    var selectedItemPosition = 0
    var id = 0
    var releasePresenter: ReleaseContract.ReleasePresenter = ReleasePresenterImpl(this)
    val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
    var selectedPic: List<Uri> = ArrayList()
    lateinit var lostOrFound: String
    lateinit var tableAdapter: ReleaseTableAdapter
    lateinit var progressDialog: ProgressDialog
    var judge = false

    private fun setToolbarView(toolbar: Toolbar) {
        toolbar.title = when (lostOrFound) {
            "lost" -> "发布丢失"
            "found" -> "发布捡到"
            else -> "编辑"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        lostOrFound = bundle.getString("lostOrFound")
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(com.example.lostfond2.R.layout.activity_release)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setToolbarView(toolbar)
        release_delete.visibility = View.GONE

        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }


        if (lostOrFound == "editLost" || lostOrFound == "editFound") {
            release_delete.visibility = View.VISIBLE
            id = bundle.getInt("id")
            selectedItemPosition = bundle.getInt("type") - 1
            onTypeItemSelected(selectedItemPosition)
            releasePresenter.loadDetailDataForEdit(id, this)
        }


        initSpinner()
        release_type_recycleriew.layoutManager = layoutManager
        drawRecyclerView(selectedItemPosition)
        release_type_recycleriew.adapter = tableAdapter

        onTypeItemSelected(selectedItemPosition)
        release_choose_pic.setOnClickListener { PermissionsUtils.requestPermission(this, PermissionsUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant) }
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun initSpinner() {
        val dateInt = longArrayOf(7, 15, 30)
        val spinnerList = ArrayList<String>()
        spinnerList.add("7天")
        spinnerList.add("15天")
        spinnerList.add("30天")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        release_publish_spinner.adapter = adapter
        release_publish_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val dateToShow = Date(System.currentTimeMillis() + dateInt[i] * 86400L * 1000L)
                val ft = SimpleDateFormat("yyyy/MM/dd")
                release_publish_res.text = "刊登至" + ft.format(dateToShow)
                duration = i
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
        release_confirm.setOnClickListener(this)
        release_delete.setOnClickListener(this)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(release_title.windowToken, 0)

    }

    override fun onClick(view: View) {

        if (view === release_confirm && (lostOrFound == "lost" || lostOrFound == "found")) {
            if (selectedPic.isNotEmpty()) {
                val file1: File
                var file: File? = null
                try {
                    file1 = File.createTempFile("pic", ".jpg")
                    val outputFile = file1.path
                    file = getFile(zipThePic(handleImageOnKitKat(selectedPic[0])), outputFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }


                val map = getUpdateMap()
                if (judge) {
                    progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                    releasePresenter.uploadReleaseDataWithPic(map, lostOrFound, file!!)
                }

            } else {
//                progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                val map = getUpdateMap()
                if (judge)
                    releasePresenter.uploadReleaseData(map, lostOrFound)
            }
        } else if (view === release_confirm) {
            val file1: File
            var file: File? = null
            try {
                file1 = File.createTempFile("pic", ".jpg")
                val outputFile = file1.path
                if (selectedPic.isNotEmpty()) {
                    file = getFile(zipThePic(handleImageOnKitKat(selectedPic[0])), outputFile)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }


            val map = getUpdateMap()
            if (judge) {
                progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                releasePresenter.uploadEditDataWithPic(map, lostOrFound, file, id)
            }
        } else if (view === release_delete) {
            progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在删除")
            releasePresenter.delete(id)
        }
    }

    override fun successCallBack(beanList: List<MyListDataOrSearchBean>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.apply {
            putString("shareOrSuccess", "success")
            putString("lostOrFound", lostOrFound)
            if (beanList[0].picture == null) {

            } else {
                putString("imageUrl", Utils.getPicUrl(beanList[0].picture))
            }
            putString("id", beanList[0].id.toString())
            putString("time", beanList[0].time)
            putString("place", beanList[0].place)
            putString("type", beanList[0].detail_type.toString())
            putString("title", beanList[0].title)
        }
        intent.apply {
            putExtras(bundle)
            setClass(this@ReleaseActivity, SuccessActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    override fun setEditData(detailData: DetailData) {
        if (detailData.picture == null) {
            Glide.with(this)
                    .load(Utils.getPicUrl("julao.jpg"))
                    .error(R.drawable.lf_detail_np)
                    .into(release_choose_pic)
        } else {
            Glide.with(this)
                    .load(Utils.getPicUrl(detailData.picture))
                    .asBitmap()
                    .into(release_choose_pic)
        }

        when (detailData.detail_type) {
            1, 2 -> {
                release_card_name.setText(detailData.card_name)
                release_card_num.setText(detailData.card_number)
            } // 1 = 身份证, 2 = 饭卡
            10 -> release_card_num_noname.setText(detailData.card_number) // 10 = 银行卡
            else -> {}
        }

        release_title.setText(detailData.title)
        release_title.setSelection(detailData.title.length)
        release_time.setText(detailData.time)
        release_place.setText(detailData.place)
        release_phone.setText(detailData.phone)
        release_contact_name.setText(detailData.name)
        release_remark.setText(detailData.item_description)
    }

    override fun deleteSuccessCallBack() {
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getUpdateMap(): Map<String, Any> {
        val titleString = release_title.text.toString()
        val nameString = release_contact_name.text.toString()
        val phoneString = release_phone.text.toString()
        val timeString = release_time.text.toString()
        val placeString = release_place.text.toString()
        val remarksString = release_remark.text.toString()
        val map = HashMap<String, Any>()
        map["title"] = titleString
        map["time"] = if (timeString == "") " " else timeString
        map["place"] = if (placeString == "") " " else placeString
        map["name"] = nameString
        map["detail_type"] = selectedItemPosition + 1
        map["phone"] = if (phoneString == "") " " else phoneString
        map["duration"] = duration
        map["item_description"] = if (remarksString == "") " " else remarksString

        judge = !(titleString == "" || phoneString == "" || nameString == "")
        if (selectedItemPosition == 0 || selectedItemPosition == 1) {
            val card_numString = release_card_num.getText().toString()
            val card_nameString = release_card_name.getText().toString()
            map["card_number"] = if (card_numString == "") "1234567890" else card_numString
            map["card_name"] = if (card_nameString == "") "巨佬" else card_nameString
        } else if (selectedItemPosition == 9) {
            val card_numString = release_card_num_noname.getText().toString()
            map["card_number"] = if (card_numString == "") "1234567890" else card_numString
            map["card_name"] = if (nameString == "") " " else nameString
        } else if (selectedItemPosition == 12) {
            map["other_tag"] = " "
        }
        return map
    }

    override fun drawRecyclerView(position: Int) {
        tableAdapter = ReleaseTableAdapter(this, position, this)
        release_type_recycleriew.adapter = tableAdapter
    }

    override fun onTypeItemSelected(position: Int) {
        selectedItemPosition = position

        when (position) {
            0, 1 -> {
                release_cardinfo.visibility = View.VISIBLE
                release_cardinfo_noname.visibility = View.GONE
            }
            9 -> {
                release_cardinfo_noname.visibility = View.VISIBLE
                release_cardinfo.visibility = View.GONE
            }
            else -> {
                release_cardinfo_noname.visibility = View.GONE
                release_cardinfo.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode != 0) {
            selectedPic = Matisse.obtainResult(data)
            Glide.with(this)
                    .load(selectedPic[0])
                    .into(release_choose_pic)
        }
    }

    private fun handleImageOnKitKat(uri: Uri): String? {
        var imagePath: String? = null
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                //Log.d(TAG, uri.toString());
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                //Log.d(TAG, uri.toString());
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null)
        }
        return imagePath
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
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
        options.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(filePath, options)
//        val height = options.outHeight
//        val width = options.outWidth
//        var inSampleSize = 1
//        val reqHeight = 800
//        val reqWidth = 480
//        if (height > reqHeight || width > reqWidth) {
//            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
//            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
//            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
//        }
        options.inSampleSize = calculateInSampleSize(options, 480, 800)
        options.inJustDecodeBounds = false
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

    @Throws(Exception::class)
    private fun saveImg(b: Bitmap?, name: String): String {
        var b = b

        val path = Environment.getExternalStorageDirectory().toString() + "/" + "BJLiuJian/YaSuoTuPian"
        val dirFile = File(path)
        val mediaFile = File(path + File.separator + name + ".jpg")
        if (mediaFile.exists()) {
            mediaFile.delete()
        }
        if (!File(path).exists()) {
            File(path).mkdirs()
        }
        mediaFile.createNewFile()
        val fos = FileOutputStream(mediaFile)
        b!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        b.recycle()
        b = null
        System.gc()
        return mediaFile.path
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

    private val mPermissionGrant: PermissionsUtils.PermissionGrant = object : PermissionsUtils.PermissionGrant {
        override fun onPermissionGranted(requestCode: Int) = when (requestCode) {
            PermissionsUtils.CODE_RECORD_AUDIO -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_GET_ACCOUNTS -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_READ_PHONE_STATE -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_CALL_PHONE -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_CAMERA -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_ACCESS_FINE_LOCATION -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_ACCESS_COARSE_LOCATION -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show()
            PermissionsUtils.CODE_READ_EXTERNAL_STORAGE -> openSeletPic()
            PermissionsUtils.CODE_WRITE_EXTERNAL_STORAGE -> Toast.makeText(this@ReleaseActivity, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {

        PermissionsUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant)

    }

    @SuppressLint("ResourceType")
    fun openSeletPic() = Matisse.from(this@ReleaseActivity)
            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
            .countable(true)
            .maxSelectable(1)
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .theme(R.style.Matisse_Zhihu)
            .forResult(2)


}
