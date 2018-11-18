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
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
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
import com.yookiely.lostfond2.service.Utils
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
        window.statusBarColor = Color.parseColor("#00a1e9")
        setContentView(com.example.lostfond2.R.layout.lf2_activity_release)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

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
        //初始化三个spinner
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
        val spinnerList = mutableListOf<String>()
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

            override fun onNothingSelected(adapterView: AdapterView<*>) = Unit
        }
        cv_release_confirm.setOnClickListener(this)
        cv_release_delete.setOnClickListener(this)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et_release_title.windowToken, 0)
    }

    //校区选择spinner的具体实现
    private fun initSpinnerOfCampus() {
        val spinnerOfCampus = mutableListOf<String>()
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

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    //领取站点spinner的具体实现
    private fun initSpinnerOfReceivingSite() {
        val spinnerListOfGarden = mutableListOf<String>()
        spinnerListOfGarden.apply {
            add("无")
            add("格园")
            add("诚园")
            add("正园")
            add("修园")
            add("齐园")
        }
        val spinnerListOfRoom = mutableListOf<String>()
        val spinnerListOfEntrance = mutableListOf<String>()
        val dataListOfRoom = mutableListOf<Int>()
        val dataListOfEntrance = mutableListOf<Int>()

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
                Utils.getListOfRoom(spinnerListOfRoom, dataListOfRoom, position)

                //斋的选择
                sp_receiving_site_room.adapter = adapterOfRoom
                sp_receiving_site_room.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        roomOfReceivingSite = spinnerListOfRoom[position]//dataListOfRoom[position].toString()
                        Utils.getListOfEntrance(spinnerListOfEntrance, dataListOfEntrance, dataListOfRoom[position])
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

                            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
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
            val map = getUpdateMap()

            if (!judgeNull(picListOfUri)) {
                if (judgementOfRelease) {
                    progressDialog = ProgressDialog.show(this@ReleaseActivity, "", "正在上传")
                    releasePresenter.uploadReleaseDataWithPic(map, lostOrFound, listOfFile)
                }
            } else {
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

    override fun failCallBack(message: String) {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        selectedRecaptureRoom = Utils.getPositionOfRoom(detailData.recapture_place!!)
        selectedRecaptureEntrance = Utils.getPositionOfEntrance(detailData.recapture_entrance)
        sp_receiving_site_garden.setSelection(Utils.getPositionOfGarden(detailData.recapture_place))
        if (detailData.campus != null) {
            sp_campus_spinner.setSelection(detailData.campus - 1)
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
        //request = 2 代表来自于
        if (requestCode == 2 && resultCode != 0) {
            this.selectedPic = Matisse.obtainResult(data) //将选择的图片加入到上传的列表中
            releasePicAdapter.changePic(this.selectedPic[0]) //加载选择的图片
        }
    }

    override fun onBackPressed() {
        showExitDialog()
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
                0 -> openSeletPic()
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
}