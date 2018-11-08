package com.yookiely.lostfond2.release

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.yookiely.lostfond2.SuccessActivity
import com.yookiely.lostfond2.service.DetailData
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.PermissionsUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.lf2_activity_release.*
import kotlinx.android.synthetic.main.lf2_release_cardview_receiving_site.*
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
import kotlin.collections.ArrayList

class ReleaseActivity : AppCompatActivity(), ReleaseContract.ReleaseView, View.OnClickListener {

    private var duration = 1
    private var entranceOfReceivingSite = 1 // 领取站点 入口
    private var roomOfReceivingSite = "1斋" // 领取站点 斋
    private var selectedItemPosition: Int = 0
    private var id = 0
    private var releasePresenter: ReleaseContract.ReleasePresenter = ReleasePresenterImpl(this)
    private val releaseTypeLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
    private val picRecyclerViewManager = LinearLayoutManager(this)
    private lateinit var releasePicAdapter: ReleasePicAdapter
    private var selectedPic: List<Uri> = mutableListOf() //get a pic's url
    private lateinit var lostOrFound: String
    private lateinit var tableAdapter: ReleaseTableAdapter
    private lateinit var progressDialog: ProgressDialog
    private var judgementOfRelease = false
    private var selectPicList = mutableListOf<Any?>()
    private lateinit var picRecyclerView: RecyclerView // 添加多图的recycler
    private var campus = 1
    private var refreshSpinnerOfRoom = false
    private var refreshSpinnerOfEntrance = false
    private var selectedRecaptureRoom = 0
    private var selectedRecaptureEntrance = 0


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
        setContentView(com.example.lostfond2.R.layout.lf2_activity_release)
        val toolbar = findViewById<Toolbar>(R.id.tb_common)
        setToolbarView(toolbar)
        cv_release_delete.visibility = View.GONE
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { showExitDialog() }
        }
        campus = Hawk.get("campus") // 得到所在校区

        if (lostOrFound == "editLost" || lostOrFound == "editFound") {//open editwindow
            cv_release_delete.visibility = View.VISIBLE
            id = bundle.getInt("id")
            selectedItemPosition = bundle.getInt("type") - 1
            onTypeItemSelected(selectedItemPosition)
            releasePresenter.loadDetailDataForEdit(id, this)
        }

        if (lostOrFound == "lost" || lostOrFound == "editLost") {
            release_receiving_site.visibility = View.GONE
            ll_release_receiving_site_mark.visibility = View.GONE
        }

        initSpinnerOfTime()
        initSpinnerOfReceivingSite()
        initSpinnerOfCampus()
        rv_release_type.layoutManager = releaseTypeLayoutManager
        drawRecyclerView(selectedItemPosition)
        rv_release_type.adapter = tableAdapter
        onTypeItemSelected(selectedItemPosition)
        sp_campus_spinner.setSelection(campus - 1)

        selectPicList.add(null) // supply a null list
        releasePicAdapter = ReleasePicAdapter(selectPicList, this, this)
        picRecyclerViewManager.orientation = LinearLayoutManager.HORIZONTAL
        picRecyclerView = findViewById(R.id.rv_release_pic)
        picRecyclerView.apply {
            layoutManager = picRecyclerViewManager
            adapter = releasePicAdapter
        }

        //开启手机虚拟键盘
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    //时间选择spinner的具体实现
    private fun initSpinnerOfTime() {
        val dateInt = longArrayOf(7, 15, 30)
        val spinnerList = ArrayList<String>()
        spinnerList.add("7天")
        spinnerList.add("15天")
        spinnerList.add("30天")
        val adapter = ArrayAdapter<String>(this, R.layout.lf2_custom_spiner_text_item, spinnerList)
        adapter.setDropDownViewResource(R.layout.lf2_custom_spinner_dropdown_item)
        sp_release_publish_spinner.adapter = adapter
        sp_release_publish_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val dateToShow = Date(System.currentTimeMillis() + dateInt[i] * 86400L * 1000L)
                val ft = SimpleDateFormat("yyyy/MM/dd")
                tv_release_publish_res.text = "刊登至" + ft.format(dateToShow)
                duration = i
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        cv_release_confirm.setOnClickListener(this)
        cv_release_delete.setOnClickListener(this)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_release_title.windowToken, 0)
    }

    //校区选择spinner的具体实现
    private fun initSpinnerOfCampus() {
        val spinnerOfCampus = ArrayList<String>()
        spinnerOfCampus.apply {
            add("北洋园")
            add("卫津路")
        }

        val adapterOfCampus = ArrayAdapter<String>(this, R.layout.lf2_custom_spiner_text_item, spinnerOfCampus)
        adapterOfCampus.setDropDownViewResource(R.layout.lf2_custom_spinner_dropdown_item)
        sp_campus_spinner.apply {
            adapter = adapterOfCampus
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    campus = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //To change body of created functions use File | Settings | File Templates.
                }
            }
        }
    }

    //领取站点spinner的具体实现
    private fun initSpinnerOfReceivingSite() {
        val spinnerListOfGarden = ArrayList<String>()
        spinnerListOfGarden.apply {
            add("无")
            add("格园")
            add("诚园")
            add("正园")
            add("修园")
            add("齐园")
        }
        val spinnerListOfRoom = ArrayList<String>()
        val spinnerListOfEntrance = ArrayList<String>()
        val dataListOfRoom = ArrayList<Int>()
        val dataListOfEntrance = ArrayList<Int>()

        //园的选择
        val adapterOfGarden = ArrayAdapter<String>(this, R.layout.lf2_custom_spiner_text_item, spinnerListOfGarden)
        adapterOfGarden.setDropDownViewResource(R.layout.lf2_custom_spinner_dropdown_item)
        val adapterOfRoom = ArrayAdapter<String>(this@ReleaseActivity, R.layout.lf2_custom_spiner_text_item, spinnerListOfRoom)
        adapterOfRoom.setDropDownViewResource(R.layout.lf2_custom_spinner_dropdown_item)
        val adapterOfEntrance = ArrayAdapter<String>(this@ReleaseActivity, R.layout.lf2_custom_spiner_text_item, spinnerListOfEntrance)
        adapterOfEntrance.setDropDownViewResource(R.layout.lf2_custom_spinner_dropdown_item)

        sp_receiving_site_garden.adapter = adapterOfGarden
        sp_receiving_site_garden.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getListOfRoom(spinnerListOfRoom, dataListOfRoom, position)

                //斋的选择
                sp_receiving_site_room.adapter = adapterOfRoom
                sp_receiving_site_room.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        roomOfReceivingSite = spinnerListOfRoom[position]//dataListOfRoom[position].toString()
                        getListOfEntrance(spinnerListOfEntrance, dataListOfEntrance, dataListOfRoom[position])
                        //口的选择

                        if (refreshSpinnerOfRoom) {
                            sp_receiving_site_room.setSelection(selectedRecaptureRoom)
                            refreshSpinnerOfRoom = false
                        }
                        sp_receiving_site_entrance.adapter = adapterOfEntrance
                        sp_receiving_site_entrance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                entranceOfReceivingSite = dataListOfEntrance[position]

                                if (refreshSpinnerOfEntrance) {
                                    sp_receiving_site_entrance.setSelection(selectedRecaptureEntrance)
                                    refreshSpinnerOfEntrance = false
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                //To change body of created functions use File | Settings | File Templates.
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        //To change body of created functions use File | Settings | File Templates.
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //To change body of created functions use File | Settings | File Templates.
            }
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_release_title.windowToken, 0)
    }

    //确定和取消按钮的点击事件
    override fun onClick(view: View) {
        val picListOfUri = mutableListOf<Uri?>()
        val picListOfString = mutableListOf<String?>()

        for (i in selectPicList) {
            if (i is Uri) {
                picListOfUri.add(i)
            }
            if (i is String) {
                picListOfString.add(i)
            }
        }

        val file1: File
        val listOfFile = mutableListOf<File?>()
        try {
            file1 = File.createTempFile("pic", ".jpg")
            val outputFile = file1.path

            if (!judgeNull(picListOfUri)) {
                for (i in picListOfUri) {
                    if (i != null) {
                        listOfFile.add(getFile(zipThePic(handleImageOnKitKat(i)), outputFile))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (view === cv_release_confirm && (lostOrFound == "lost" || lostOrFound == "found")) {
            if (!judgeNull(picListOfUri)) {
//                val file1: File
//                val arrayOfFile = ArrayList<File?>(0)
//                try {
//                    file1 = File.createTempFile("pic", ".jpg")
//                    val outputFile = file1.path
//                    for (i in picListOfUri) {
//                        if (i != null) {
//                            arrayOfFile.add(getFile(zipThePic(handleImageOnKitKat(i)), outputFile))
//                        }
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }

                val map = getUpdateMap()
                if (judgementOfRelease) {
                    progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                    releasePresenter.uploadReleaseDataWithPic(map, lostOrFound, listOfFile)
                }
            } else {
                val map = getUpdateMap()

                if (judgementOfRelease) {
                    progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                    releasePresenter.uploadReleaseData(map, lostOrFound)
                }
            }
        } else if (view === cv_release_confirm) {
            val map = getUpdateMap()
            if (judgementOfRelease) {
                progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                releasePresenter.uploadEditDataWithPic(map, lostOrFound, listOfFile, picListOfString, id)
            }
        } else if (view === cv_release_delete) {
            progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在删除")
            releasePresenter.delete(id)
        }
    }

    override fun successCallBack(beanList: List<MyListDataOrSearchBean>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.apply {
            //            if (beanList[0].picture != null) {
//                putString("imageUrl", Utils.getPicUrl(beanList[0].picture!![0]))
//            }

            putString("shareOrSuccess", "success")
            putString("lostOrFound", lostOrFound)
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
        if (detailData.picture != null) {
            releasePicAdapter.addPicUrl(detailData.picture)
        }

        when (detailData.detail_type) {
            1, 2 -> {
                et_release_card_name.setText(detailData.card_name)
                et_release_card_num.setText(detailData.card_number)
            } // 1 = 身份证, 2 = 饭卡
            10 -> et_release_card_num_noname.setText(detailData.card_number) // 10 = 银行卡
            else -> {
            }
        }

        if (detailData.time != "忘记了") {
            et_release_time.setText(detailData.time)
        }
        if (detailData.place != "忘记了") {
            et_release_place.setText(detailData.place)
        }
        et_release_title.setText(detailData.title)
        et_release_title.setSelection(detailData.title!!.length)
        et_release_phone.setText(detailData.phone)
        et_release_content_name.setText(detailData.name)
        et_release_remark.setText(detailData.item_description)

        refreshSpinnerOfRoom = true
        refreshSpinnerOfEntrance = true
        selectedRecaptureRoom = getPositionOfRoom(detailData.recapture_place!!)
        selectedRecaptureEntrance = getPositionOfEntrance(detailData.recapture_entrance)
        sp_receiving_site_garden.setSelection(getPositionOfGarden(detailData.recapture_place))
        if (detailData.campus != null) {
            sp_campus_spinner.setSelection(detailData.campus.toInt() - 1)
        }
    }

    override fun deleteSuccessCallBack() {
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
        finish()
    }

    //得到release中用户所填内容
    private fun getUpdateMap(): Map<String, Any> {
        val titleString = et_release_title.text.toString()
        val nameString = et_release_content_name.text.toString()
        val phoneString = et_release_phone.text.toString()
        val timeString = et_release_time.text.toString()
        val placeString = et_release_place.text.toString()
        val remarksString = et_release_remark.text.toString()

        judgementOfRelease = !(titleString.isBlank() || phoneString.isBlank() || nameString.isBlank())

        val map = HashMap<String, Any>()
        map["title"] = titleString
        map["time"] = if (timeString.isNotBlank()) timeString else ""
        map["place"] = if (placeString.isNotBlank()) placeString else ""
        map["name"] = nameString
        map["detail_type"] = selectedItemPosition + 1
        map["phone"] = phoneString
        map["duration"] = duration
        map["item_description"] = if (remarksString.isNotBlank()) remarksString else ""
        map["campus"] = campus

        if (lostOrFound == "found") {
            map["recapture_place"] = roomOfReceivingSite
            map["recapture_entrance"] = entranceOfReceivingSite
        }

        if (selectedItemPosition == 0 || selectedItemPosition == 1) {
            val cardNumString = et_release_card_num.text.toString()
            val cardNameString = et_release_card_name.text.toString()
            judgementOfRelease = judgementOfRelease && !(cardNameString.isBlank() || cardNumString.isBlank())
            map["card_number"] = cardNumString
            map["card_name"] = cardNameString
        } else if (selectedItemPosition == 9) {
            val cardNumString = et_release_card_num_noname.text.toString()
            judgementOfRelease = judgementOfRelease && cardNumString.isNotBlank()
            map["card_number"] = cardNumString
            map["card_name"] = if (nameString == "") " " else nameString
        } else if (selectedItemPosition == 12) {
            map["other_tag"] = " "
        }

        return map
    }

    override fun drawRecyclerView(position: Int) {
        tableAdapter = ReleaseTableAdapter(this, position, this)
        rv_release_type.adapter = tableAdapter
    }

    //物品类型选择对列表的影响
    override fun onTypeItemSelected(position: Int) {
        selectedItemPosition = position

        when (position) {
            0, 1 -> {
                tv_release_lost_content.visibility = View.VISIBLE
                release_cardinfo.visibility = View.VISIBLE
                release_cardinfo_noname.visibility = View.GONE
            } // 身份证，饭卡
            9 -> {
                tv_release_lost_content.visibility = View.VISIBLE
                release_cardinfo_noname.visibility = View.VISIBLE
                release_cardinfo.visibility = View.GONE
            } // 身份证
            else -> {
                tv_release_lost_content.visibility = View.GONE
                release_cardinfo_noname.visibility = View.GONE
                release_cardinfo.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode != 0) {
            this.selectedPic = Matisse.obtainResult(data) //将选择的图片加入到上传的列表中
            releasePicAdapter.changePic(this.selectedPic[0]) //加载选择的图片
        }
    }

    //Android自带的三件套的返回键的点击事件
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog()
        }

        return false
    }

    //退出编辑发布或丢失页面的dialog
    private fun showExitDialog() {
        val isExit = AlertDialog.Builder(this@ReleaseActivity)
                .setTitle("放弃编辑吗～")
                .setCancelable(false)
                .setPositiveButton("取消") { dialog, _ -> dialog.dismiss() }
                .setNegativeButton("确定") { _, _ -> this@ReleaseActivity.finish() }
                .create()
        isExit.show()
    }

    //相册图片路径
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
        options.inJustDecodeBounds = true
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

    val mPermissionGrant: PermissionsUtils.PermissionGrant = object : PermissionsUtils.PermissionGrant {
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
                                            grantResults: IntArray) = PermissionsUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant)


    //用第三方库打开相册
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

    //release界面中长按图片的dialog
    fun setPicEdit() {
        val list = arrayOf<CharSequence>("更改图片", "删除图片", "取消")
        val alertDialogBuilder = AlertDialog.Builder(this@ReleaseActivity)
        alertDialogBuilder.setItems(list) { dialog, item ->
            when (item) {
                0 -> PermissionsUtils.requestPermission(this@ReleaseActivity, PermissionsUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant)
                1 -> releasePicAdapter.removePic()
                2 -> dialog.dismiss()
            }
        }
        val alert = alertDialogBuilder.create()
        alert.show()
    }

    //以下几个方法是关于领取站点的spinner的一系列内容处理
    private fun getListOfRoom(list: ArrayList<String>, intList: ArrayList<Int>, position: Int) {
        list.clear()
        intList.clear()
        when (position) {
            0 -> {
                list.add("无")
                intList.add(0)
            }
            1 -> {
                list.add("1斋")
                list.add("2斋")
                list.add("3斋")
                intList.add(1)
                intList.add(2)
                intList.add(3)
            }
            2 -> {
                list.add("6斋")
                list.add("7斋")
                list.add("8斋")
                intList.add(6)
                intList.add(7)
                intList.add(8)
            }
            3 -> {
                list.add("9斋")
                list.add("10斋")
                intList.add(9)
                intList.add(10)
            }
            4 -> {
                list.add("11斋")
                list.add("12斋")
                intList.add(11)
                intList.add(12)
            }
            5 -> {
                list.add("13斋")
                list.add("14斋")
                list.add("15斋")
                list.add("16斋")
                intList.add(13)
                intList.add(14)
                intList.add(15)
                intList.add(16)
            }
            else -> {
            }
        }
    }

    private fun getListOfEntrance(list: ArrayList<String>, intList: ArrayList<Int>, room: Int) {
        list.clear()
        intList.clear()

        when (room) {
            0 -> {
                list.add("无")
                intList.add(0)
            }
            1, 2, 9, 10 -> {
                list.add("只有一个入口")
                intList.add(0)
            }
            11, 12 -> {
                list.add("只可A口")
                intList.add(0)
            }
            else -> {
                list.add("A口")
                list.add("B口")
                intList.add(1)
                intList.add(2)
            }
        }
    }

    private fun judgeNull(list: List<Uri?>): Boolean {
        for (i in 0..(list.size - 1)) {
            if (list[i] != null) {
                return false
            }
        }

        return true
    }

    // 返回值数据为其在所对应数组中的位置
    private fun getPositionOfGarden(i: String): Int = when (i) {
        "无" -> 0
        "1斋", "2斋", "3斋" -> 1
        "6斋", "7斋", "8斋" -> 2
        "9斋", "10斋" -> 3
        "11斋", "12斋" -> 4
        "13斋", "14斋", "15斋", "16斋" -> 5
        else -> 2333
    }

    private fun getPositionOfRoom(i: String): Int = when (i) {
        "无", "1斋", "6斋", "9斋", "11斋", "13斋" -> 0
        "2斋", "7斋", "10斋", "12斋", "14斋" -> 1
        "3斋", "8斋", "15斋" -> 2
        "16斋" -> 3
        else -> 2333
    }

    private fun getPositionOfEntrance(i: Int): Int = when (i) {
        0, 1 -> 0
        2 -> 1
        else -> 404
    }
}