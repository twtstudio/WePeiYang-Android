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
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import es.dmoral.toasty.Toasty

class ProblemFragment : Fragment() {

    private lateinit var answer: String
    private var answerIsShown: Boolean = false
    var type: Int? = null
    var classID: Int? = null
    var mode: Int? = null
    private var problemID: Int? = null
    private var clickable = true


    companion object {

        const val CLASS_ID_KEY = "class_id_key"

        // 题目类型 0->单选 1->多选 2->判断
        const val QUES_TYPE_KEY = "ques_type"
        const val SINGLE_CHOICE = 0
        const val MULTI_CHOICE = 1
        const val TRUE_FALSE = 2

        // 做题模式 -1->背题 -2->答题 -3->模拟测试
        const val MODE_KEY = "mode"
        const val READ_MODE = -1
        const val PRACTICE_MODE = -2
        const val TEST_MODE = -3

        // 题目 ID
        private const val QUES_ID_KEY = "ques_id"

        private const val ONE_PROBLEM_KEY = "one_problem_key"

        internal fun newInstance(classID: Int, type: Int, mode: Int, problemID: Int): ProblemFragment {
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

        internal fun newInstace(testOneProblemData: TestOneProblemData): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            args.putInt(MODE_KEY, TEST_MODE)
            args.putSerializable(ONE_PROBLEM_KEY, testOneProblemData)
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
        answerIsShown = (mode == READ_MODE)
        clickable = (mode == PRACTICE_MODE || mode == TEST_MODE)

        tvType = view.findViewById(R.id.tv_problem_type)
        tvTitle = view.findViewById(R.id.tv_problem_title)
        rvSelections = view.findViewById(R.id.rv_problem_selections)
        ivStarBlank = view.findViewById(R.id.iv_star_blank)
        ivIndex = view.findViewById(R.id.iv_index)
        tvIndex = view.findViewById(R.id.tv_index)
        divider = view.findViewById(R.id.divider_problem)
        tvAnswer = view.findViewById(R.id.tv_problem_answer)

        rvSelections.layoutManager = LinearLayoutManager(context)

        if (mode == PRACTICE_MODE || mode == READ_MODE) {
            changeMode()
        }

        if (mode == PRACTICE_MODE || mode == READ_MODE) {
            getProblem(classID.toString(), type.toString(), problemID.toString()) {
                when (it) {
                    is RefreshState.Failure -> {
                        context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                    }
                    is RefreshState.Success -> {
                        tvType.text = it.message.ques.type.toProblemType()
                        tvTitle.text = Html.fromHtml(it.message.ques.content)
                        tvAnswer.text = "答案：${it.message.ques.answer}"
                        answer = it.message.ques.answer
                        rvSelections.withItems {
                            if (mode == PRACTICE_MODE) {
                                for (i in 0 until it.message.ques.option.size) {
                                    selectionItem(this@ProblemFragment, i.toSelectionIndex(), it.message.ques.option[i], SelectionItem.NONE)
                                }
                            } else if (mode == READ_MODE) {
                                for (i in 0 until it.message.ques.option.size) {
                                    if (i.toSelectionIndex() == it.message.ques.answer) {
                                        selectionItem(this@ProblemFragment, i.toSelectionIndex(), it.message.ques.option[i], SelectionItem.TRUE)
                                    } else {
                                        selectionItem(this@ProblemFragment, i.toSelectionIndex(), it.message.ques.option[i], SelectionItem.NONE)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (mode == TEST_MODE) {
            handleViewsVisibility()
            val oneProblemData = arguments?.getSerializable(ONE_PROBLEM_KEY)
            oneProblemData as TestOneProblemData
            tvType.text = oneProblemData.type.toProblemType()
            tvTitle.text = Html.fromHtml(oneProblemData.content)
            rvSelections.withItems {
                repeat(oneProblemData.option.size) {
                    selectionItem(this@ProblemFragment, it.toSelectionIndex(), oneProblemData.option[it], SelectionItem.NONE)
                }
            }
        }
        return view
    }

    fun changeMode() {
        mode = if (ProblemActivity.isLeft) {
            clickable = false
            answerIsShown = true
            READ_MODE
        } else {
            answerIsShown = false
            clickable = !answerIsShown
            hideAnswer()
            PRACTICE_MODE
        }
        handleViewsVisibility()
    }

    fun showAnswersOnSelections(clickId: Int) {
        if (mode == PRACTICE_MODE && clickable) {
            val adapter = rvSelections.adapter as ItemAdapter
            val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
            for (i in 0 until list.size) {
                when (i) {
                    answer.selectionIndexToInt() -> {
                        list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.TRUE)
                    }
                    clickId -> {
                        list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.FALSE)
                    }
                }
            }
            adapter.itemManager.refreshAll(list)
            clickable = false
            answerIsShown = true
        }
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

    private fun hideAnswer() {
        val adapter = rvSelections.adapter as ItemAdapter
        val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
        for (i in 0 until list.size) {
            list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.NONE)
        }
        adapter.itemManager.refreshAll(list)
        answerIsShown = false
    }
}