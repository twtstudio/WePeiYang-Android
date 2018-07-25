package com.twtstudio.service.tjwhm.exam.problem

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty

class ProblemFragment : Fragment() {

    companion object {

        const val CLASS_ID_KEY = "class_id_key"
        var classID: Int? = null

        // 题目类型 0->单选 1->多选 2->判断
        const val QUES_TYPE_KEY = "ques_type"
        const val SINGLE_CHOICE = 0
        const val MULTI_CHOICE = 1
        const val TRUE_FALSE = 2
        var type: Int? = null

        // 做题模式 -1->背题 -2->答题 -3->模拟测试
        const val MODE_KEY = "mode"
        const val READ_MODE = -1
        const val PRACTICE_MODE = -2
        const val TEST_MODE = -3
        var mode: Int? = null

        // 题目 ID
        private const val QUES_ID_KEY = "ques_id"
        var problemID: Int? = null

        public fun newInstance(classID: Int, type: Int, mode: Int, problemID: Int): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            args.apply {
                putInt(CLASS_ID_KEY, classID)
                putInt(QUES_TYPE_KEY, type)
                putInt(MODE_KEY, mode)
                putInt(QUES_ID_KEY, problemID)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var tvType: TextView
    private lateinit var tvTitle: TextView
    private lateinit var rvSelections: RecyclerView
    private lateinit var ivStarBlank: ImageView
    private lateinit var ivIndex: ImageView
    private lateinit var tvIndex: TextView
    private lateinit var divider: View
    private lateinit var tvAnswer: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_problem, container, false)

        classID = arguments?.getInt(CLASS_ID_KEY)
        type = arguments?.getInt(QUES_TYPE_KEY)
        mode = arguments?.getInt(MODE_KEY)
        problemID = arguments?.getInt(QUES_ID_KEY)

        tvType = view.findViewById(R.id.tv_problem_type)
        tvTitle = view.findViewById(R.id.tv_problem_title)
        rvSelections = view.findViewById(R.id.rv_problem_selections)
        ivStarBlank = view.findViewById(R.id.iv_star_blank)
        ivIndex = view.findViewById(R.id.iv_index)
        tvIndex = view.findViewById(R.id.tv_index)
        divider = view.findViewById(R.id.divider_problem)
        tvAnswer = view.findViewById(R.id.tv_problem_answer)

        rvSelections.layoutManager = LinearLayoutManager(context)

        getProblem(classID.toString(), type.toString(), problemID.toString()) {
            when (it) {
                is RefreshState.Failure -> {
                    context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                }
                is RefreshState.Success -> {
                    tvType.text = it.message.ques.type.toProblemType()
                    tvTitle.text = Html.fromHtml(it.message.ques.content)
                    tvAnswer.text = it.message.ques.answer
                    rvSelections.withItems {
                        for (i in 0 until it.message.ques.option.size) {
                            this@ProblemFragment.context?.let { it1 ->
                                selectionItem(it1, i.toSelectionIndex(), it.message.ques.option[i])
                                Log.e("zzzzzz", i.toString())
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    fun changeMode(isLeft: Boolean) {
        mode = if (isLeft) {
            READ_MODE
        } else {
            PRACTICE_MODE
        }
        handleViewsVisibility()
    }

    private fun handleViewsVisibility() {
        when (mode) {
            READ_MODE -> {
                divider.visibility = View.VISIBLE
                tvAnswer.visibility = View.VISIBLE
            }
            else -> {
                divider.visibility = View.GONE
                tvAnswer.visibility = View.GONE
            }
        }
    }

}