package com.twt.service.job.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.twt.service.job.R
import com.twt.service.job.service.ARG_KIND
import com.twt.service.job.service.funs

class JobFragment : Fragment(), JobHomeContract.JobHomeView {
    private lateinit var kind: String// 记录是四种类型中的哪一种
    private var type: Int = 0

    companion object {
        fun newInstance(kind: String): JobFragment {
            val args = Bundle()
            args.putString(ARG_KIND, kind)
            val jobFragment = JobFragment()
            jobFragment.arguments = args
            return jobFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            kind = getString(ARG_KIND)
            type = funs.getType(kind)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.job_fragment_home, container, false)
        return view
    }

    override fun showJobFair() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showOther() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNull() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}