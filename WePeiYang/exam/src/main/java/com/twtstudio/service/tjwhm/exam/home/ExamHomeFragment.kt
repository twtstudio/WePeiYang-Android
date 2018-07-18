package com.twtstudio.service.tjwhm.exam.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.list.ListActivity
import es.dmoral.toasty.Toasty

class ExamHomeFragment : Fragment(), View.OnClickListener {

    private lateinit var ivParty: ImageView
    private lateinit var ivPolicy: ImageView
    private lateinit var ivOnline: ImageView
    private lateinit var ivMore: ImageView
    private lateinit var tvParty: TextView
    private lateinit var tvPolicy: TextView
    private lateinit var tvOnline: TextView
    private lateinit var tvMore: TextView

    companion object {
        fun newInstance(): ExamHomeFragment {
            return ExamHomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_home, container, false)
        ivParty = view.findViewById(R.id.iv_party)
        ivParty.setOnClickListener(this)
        ivPolicy = view.findViewById(R.id.iv_policy)
        ivPolicy.setOnClickListener(this)
        ivOnline = view.findViewById(R.id.iv_online)
        ivOnline.setOnClickListener(this)
        ivMore = view.findViewById(R.id.iv_more)
        ivMore.setOnClickListener(this)
        tvParty = view.findViewById(R.id.tv_party)
        tvParty.setOnClickListener(this)
        tvPolicy = view.findViewById(R.id.tv_policy)
        tvPolicy.setOnClickListener(this)
        tvOnline = view.findViewById(R.id.tv_online)
        tvOnline.setOnClickListener(this)
        tvMore = view.findViewById(R.id.tv_more)
        tvMore.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        val intent = Intent(context, ListActivity::class.java)
        when (v) {
            ivParty, tvParty -> {
                intent.putExtra(ListActivity.LESSON_TYPE, ListActivity.PARTY)
                context?.startActivity(intent)
            }
            ivPolicy, tvPolicy -> {
                intent.putExtra(ListActivity.LESSON_TYPE, ListActivity.POLICY)
                context?.startActivity(intent)
            }
            ivOnline, tvOnline -> {
                intent.putExtra(ListActivity.LESSON_TYPE, ListActivity.ONLINE)
                context?.startActivity(intent)
            }
            ivMore, tvMore -> {
                Toasty.info(activity!!, "暂无其他类课程!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}