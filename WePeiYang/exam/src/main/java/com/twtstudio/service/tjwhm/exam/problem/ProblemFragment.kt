package com.twtstudio.service.tjwhm.exam.problem

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.withItems
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.ext.*
import es.dmoral.toasty.Toasty

class ProblemFragment : Fragment() {


    companion object {

        const val FRAGMENT_INDEX_KEY = "fragment_index_key"

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

        internal fun newInstance(fragmentIndex: Int, type: Int, mode: Int, problemID: Int): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            args.apply {
                putInt(FRAGMENT_INDEX_KEY, fragmentIndex)
                putInt(QUES_TYPE_KEY, type)
                putInt(MODE_KEY, mode)
                putInt(QUES_ID_KEY, problemID)
            }
            fragment.arguments = args
            return fragment
        }

        internal fun newInstance(fragmentIndex: Int, testOneProblemData: TestOneProblemData): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            args.apply {
                putInt(FRAGMENT_INDEX_KEY, fragmentIndex)
                putInt(MODE_KEY, TEST_MODE)
                putInt(QUES_TYPE_KEY, testOneProblemData.type)
                putInt(QUES_ID_KEY, testOneProblemData.id)
                putSerializable(ONE_PROBLEM_KEY, testOneProblemData)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var answerFromRemote: String
    private var answerIsShown: Boolean = false
    var type: Int = 0
    var mode: Int? = null
    private var problemID: Int = 0
    private var clickable = true
    private var fragmentIndex = -1
    private var singleSelectionAnswer = -1
    private var multiSelectionAnswers = mutableListOf<Int>()


    private lateinit var tvType: TextView
    private lateinit var tvTitle: TextView
    private lateinit var rvSelections: RecyclerView
    private lateinit var ivStarBlank: ImageView
    private lateinit var llIndex: LinearLayout
    private lateinit var tvIndex: TextView
    private lateinit var divider: View
    private lateinit var tvAnswer: TextView
    private lateinit var btConfirm: Button
    private lateinit var mActivity: ProblemActivity

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.exam_fragment_problem, container, false)

        fragmentIndex = arguments?.getInt(FRAGMENT_INDEX_KEY) ?: -1
        type = arguments?.getInt(QUES_TYPE_KEY) ?: 0
        mode = arguments?.getInt(MODE_KEY)

        problemID = arguments?.getInt(QUES_ID_KEY) ?: 0
        answerIsShown = (mode == READ_MODE)
        clickable = (mode == PRACTICE_MODE || mode == TEST_MODE)

        tvType = view.findViewById(R.id.tv_problem_type)
        tvTitle = view.findViewById(R.id.tv_problem_title)
        rvSelections = view.findViewById(R.id.rv_problem_selections)
        ivStarBlank = view.findViewById(R.id.iv_star_blank)
        llIndex = view.findViewById(R.id.ll_problem_index)
        tvIndex = view.findViewById(R.id.tv_index)
        divider = view.findViewById(R.id.divider_problem)
        tvAnswer = view.findViewById(R.id.tv_problem_answer)
        btConfirm = view.findViewById(R.id.bt_problem_confirm)
        mActivity = activity as ProblemActivity

        llIndex.setOnClickListener {
            mActivity.showProblemIndexPopupWindow(llIndex.x, llIndex.y)
        }
        btConfirm.setOnClickListener {
            val answerString = when (type) {
                SINGLE_CHOICE, TRUE_FALSE -> singleSelectionAnswer.toSelectionIndex()
                MULTI_CHOICE -> multiSelectionAnswers.toSelectionIndex()
                else -> "W"
            }
            val scrollPage = !(mode == PRACTICE_MODE && multiSelectionAnswers != answerFromRemote.multiSelectionIndexToInt())
            mActivity.storeResult(fragmentIndex, UpdateResultViewModel(problemID, answerString, type), scrollPage)
        }

        rvSelections.layoutManager = LinearLayoutManager(context)
        rvSelections.itemAnimator = null

        if (mode == PRACTICE_MODE || mode == READ_MODE) {
            changeMode()
        }

        tvIndex.text = "${fragmentIndex + 1}/${mActivity.size}"

        if (mode == PRACTICE_MODE || mode == READ_MODE) {
            getProblem(mActivity.lessonID.toString(), type.toString(), problemID.toString()) {
                when (it) {
                    is RefreshState.Failure -> context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                    is RefreshState.Success -> {
                        tvType.text = it.message.ques.type.toProblemType()
                        tvTitle.text = Html.fromHtml(it.message.ques.content)
                        tvAnswer.text = "答案：${it.message.ques.answer}"
                        answerFromRemote = it.message.ques.answer
                        rvSelections.withItems {
                            if (mode == PRACTICE_MODE) {
                                for (i in 0 until it.message.ques.option.size) {
                                    selectionItem(this@ProblemFragment, i.toSelectionIndex(), it.message.ques.option[i], SelectionItem.NONE)
                                }
                            } else if (mode == READ_MODE) {
                                for (i in 0 until it.message.ques.option.size) {
                                    if (i.toSelectionIndex() == it.message.ques.answer)
                                        selectionItem(this@ProblemFragment, i.toSelectionIndex(), it.message.ques.option[i], SelectionItem.TRUE)
                                    else selectionItem(this@ProblemFragment, i.toSelectionIndex(), it.message.ques.option[i], SelectionItem.NONE)
                                }
                            }
                        }
                        showStoredAnswers()
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
            showStoredAnswers()
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
            if (rvSelections.adapter != null) hideAnswer()
            PRACTICE_MODE
        }
        handleViewsVisibility()
    }

    fun onSelectionItemClick(clickId: Int) {
        if (mode == PRACTICE_MODE && clickable && (type == SINGLE_CHOICE || type == TRUE_FALSE)) {
            showAnswersForSingleSelection(clickId)
        } else if (mode == TEST_MODE && clickable) {
            showSelectedSelectionForTest(clickId)
        }
    }

    private fun showAnswersForSingleSelection(clickId: Int) {
        val adapter = rvSelections.adapter as ItemAdapter
        val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
        for (i in 0 until list.size) {
            when (i) {
                answerFromRemote.selectionIndexToInt() -> list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.TRUE)
                clickId -> list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.FALSE)
            }
        }
        adapter.itemManager.refreshAll(list)
        clickable = false
        answerIsShown = true
        divider.visibility = View.VISIBLE
        tvAnswer.visibility = View.VISIBLE
        val scrollPage = clickId == answerFromRemote.selectionIndexToInt()
        mActivity.storeResult(fragmentIndex, UpdateResultViewModel(problemID, clickId.toSelectionIndex(), type), scrollPage)
    }

    private fun showSelectedSelectionForTest(clickId: Int) {
        val adapter = rvSelections.adapter as ItemAdapter
        when (type) {
            SINGLE_CHOICE, TRUE_FALSE -> {
                singleSelectionAnswer = clickId
                val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
                for (i in 0 until list.size) {
                    when (i) {
                        singleSelectionAnswer -> list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.TRUE)
                        else -> list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.NONE)
                    }
                }
                adapter.itemManager.refreshAll(list)
                mActivity.storeResult(fragmentIndex, UpdateResultViewModel(problemID, singleSelectionAnswer.toSelectionIndex(), type), true)
            }
            MULTI_CHOICE -> {
                when (clickId) {
                    in multiSelectionAnswers -> multiSelectionAnswers.remove(clickId)
                    else -> multiSelectionAnswers.add(clickId)
                }
                refreshMultiSelectionAnswers()
            }
        }
    }

    private fun showStoredAnswers() {
        val answers = mActivity.userSelections[fragmentIndex]?.answer?.multiSelectionIndexToInt()
        val adapter = rvSelections.adapter as ItemAdapter
        val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()

        when (mode) {
            PRACTICE_MODE -> if (answers != null) {
                clickable = false
                repeat(list.size) {
                    when (it) {
                        answerFromRemote.selectionIndexToInt() -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.TRUE)
                        in answers -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.FALSE)
                    }
                }
                adapter.itemManager.refreshAll(list)
                answerIsShown = true
                divider.visibility = View.VISIBLE
                tvAnswer.visibility = View.VISIBLE
            }
            TEST_MODE -> if (answers != null) {
                repeat(list.size) {
                    when (it) {
                        in answers -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.TRUE)
                        else -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.NONE)
                    }
                }
                adapter.itemManager.refreshAll(list)
            }
        }
    }

    private fun refreshMultiSelectionAnswers() {
        val adapter = rvSelections.adapter as ItemAdapter
        val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
        for (i in 0 until list.size) {
            when (i) {
                in multiSelectionAnswers -> list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.TRUE)
                else -> list[i] = SelectionItem(list[i] as SelectionItem, SelectionItem.NONE)
            }
        }
        adapter.itemManager.refreshAll(list)
    }

    private fun handleViewsVisibility() {
        when (mode) {
            READ_MODE -> {
                divider.visibility = View.VISIBLE
                tvAnswer.visibility = View.VISIBLE
                btConfirm.visibility = View.GONE
            }
            else -> {
                divider.visibility = View.GONE
                tvAnswer.visibility = View.GONE
                if (type == MULTI_CHOICE) btConfirm.visibility = View.VISIBLE
                else btConfirm.visibility = View.GONE
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