package com.twtstudio.service.tjwhm.exam.problem

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
import com.twtstudio.service.tjwhm.exam.commons.*
import com.twtstudio.service.tjwhm.exam.user.addCollection
import com.twtstudio.service.tjwhm.exam.user.deleteCollection
import com.twtstudio.service.tjwhm.exam.user.star.StarActivity
import es.dmoral.toasty.Toasty
import okhttp3.MultipartBody


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

        internal fun newInstance(fragmentIndex: Int, testProblemBean: TestProblemBean): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            args.apply {
                putInt(FRAGMENT_INDEX_KEY, fragmentIndex)
                putInt(MODE_KEY, TEST_MODE)
                putInt(QUES_TYPE_KEY, testProblemBean.ques_type)
                putInt(QUES_ID_KEY, testProblemBean.ques_id)
                putSerializable(ONE_PROBLEM_KEY, testProblemBean)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var answerFromRemote: String
    private var answerIsShown: Boolean = false
    var type: Int = 0
    private var mode: Int? = null
    private var problemID: Int = 0
    private var clickable = true
    private var fragmentIndex = -1
    private var singleSelectionAnswer = -1
    private var multiSelectionAnswers: MutableList<Int> = mutableListOf()

    private var isCollected = false


    private lateinit var tvType: TextView
    private lateinit var tvTitle: TextView
    private lateinit var rvSelections: RecyclerView
    private lateinit var ivStar: ImageView
    private lateinit var llIndex: LinearLayout
    private lateinit var tvIndex: TextView
    private lateinit var divider: View
    private lateinit var tvRightOrWrong: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var tvAnswerUser: TextView
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
        ivStar = view.findViewById(R.id.iv_fragment_problem_star)
        llIndex = view.findViewById(R.id.ll_problem_index)
        tvIndex = view.findViewById(R.id.tv_index)
        divider = view.findViewById(R.id.divider_problem)
        tvRightOrWrong = view.findViewById(R.id.tv_problem_right_or_wrong)
        tvAnswer = view.findViewById(R.id.tv_problem_answer_content)
        tvAnswerUser = view.findViewById(R.id.tv_problem_answer_user)
        btConfirm = view.findViewById(R.id.bt_problem_confirm)
        mActivity = activity as ProblemActivity

        llIndex.setOnClickListener {
            mActivity.showProblemIndexPopupWindow(llIndex.x, llIndex.y, fragmentIndex)
        }

        btConfirm.setOnClickListener {
            onConfirmButtonClick()
        }

        rvSelections.layoutManager = LinearLayoutManager(context)
        rvSelections.itemAnimator = null

        tvIndex.text = "${fragmentIndex + 1}/${mActivity.size}"
        getProblemData()

        ivStar.setOnClickListener {
            if (isCollected) {
                cancelCollectProblem()
            } else {
                collectProblem()
            }
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun getProblemData() {
        if (mode == PRACTICE_MODE || mode == READ_MODE) {
            getProblem(mActivity.lessonID.toString(), type.toString(), problemID.toString()) { it ->
                when (it) {
                    is RefreshState.Failure -> context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                    is RefreshState.Success -> {
                        // 在 service 中已经判断 data 不为空
                        it.message.data!!.apply {
                            //                            this@ProblemFragment.type = type
                            tvType.text = this.ques_type.toProblemType()
                            tvTitle.text = Html.fromHtml(this.content)
                            tvTitle.setOnLongClickListener {
                                val cmb = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cmb.primaryClip = ClipData(ClipDescription("题目", arrayOf(MIMETYPE_TEXT_PLAIN)), ClipData.Item(tvTitle.text))
                                Toasty.success(tvTitle.context, "已复制到剪贴板").show()
                                true
                            }
                            tvAnswer.text = "答案: ${this.answer}"
                            answerFromRemote = this.answer
                            rvSelections.withItems {
                                if (mode == PRACTICE_MODE) {

                                    // 如果有储存的答案, 则直接显示答案
                                    if (!mActivity.userSelectionsForPractice[fragmentIndex].isEmpty()) {
                                        for (i in 0 until this@apply.option.size) {
                                            if (i in mActivity.userSelectionsForPractice[fragmentIndex]) {
                                                if (i in answerFromRemote.multiSelectionIndexToInt())
                                                    selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.TRUE)
                                                else
                                                    selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.FALSE)
                                            } else if (i in answerFromRemote.multiSelectionIndexToInt())
                                                selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.TRUE)
                                            else
                                                selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.NONE)
                                        }
                                        val isRight = answerFromRemote.multiSelectionIndexToInt().isRightWith(mActivity.userSelectionsForPractice[fragmentIndex])
                                        tvAnswer.visibility = View.VISIBLE
                                        clickable = false
                                        if (isRight) {
                                            tvRightOrWrong.text = "回答正确"
                                            context?.let { ContextCompat.getColor(it, R.color.examTextBlue) }?.let { tvRightOrWrong.setTextColor(it) }
                                        } else {
                                            tvRightOrWrong.text = "回答错误"
                                            context?.let { ContextCompat.getColor(it, R.color.examTextRed) }?.let { tvRightOrWrong.setTextColor(it) }
                                        }
                                    } else
                                        for (i in 0 until this@apply.option.size)
                                            selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.NONE)

                                } else if (mode == READ_MODE) {
                                    for (i in 0 until option.size) {
                                        if (i in answer.multiSelectionIndexToInt())
                                            selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.TRUE)
                                        else selectionItem(this@ProblemFragment, i.toSelectionIndex(), this@apply.option[i], SelectionItem.NONE)
                                    }
                                }
                            }
                            if (fragmentIndex == 0)
                                mark(course_id, ques_type, ques_id.toString(), 0.toString())
                            else
                                mark(course_id, ques_type, ques_id.toString(), fragmentIndex.toString())
                        }
                        changeMode()

                        if (it.message.data!!.is_collected == 1) {
                            ivStar.apply {
                                setImageResource(R.drawable.exam_ic_star_filled)
                                isCollected = true
                            }
                        } else {
                            ivStar.apply {
                                setImageResource(R.drawable.exam_ic_star_blank)
                                isCollected = false
                            }
                        }
                        showStoredAnswers()
                    }
                }
            }
        } else if (mode == TEST_MODE) {
            handleViewsVisibility()
            val oneProblemData = arguments?.getSerializable(ONE_PROBLEM_KEY)
            oneProblemData as TestProblemBean
            tvType.text = oneProblemData.ques_type.toProblemType()
            tvTitle.text = Html.fromHtml(oneProblemData.content)
            rvSelections.withItems {
                repeat(oneProblemData.option.size) {
                    selectionItem(this@ProblemFragment, it.toSelectionIndex(), oneProblemData.option[it], SelectionItem.NONE)
                }
            }
            ivStar.apply {
                if (oneProblemData.is_collected == 1) {
                    setImageResource(R.drawable.exam_ic_star_filled)
                    isCollected = true
                } else {
                    setImageResource(R.drawable.exam_ic_star_blank)
                    isCollected = false
                }
            }
            showStoredAnswers()
        }
    }

    private fun collectProblem() {
        val list = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ques_type", type.toString())
                .addFormDataPart("ques_id", problemID.toString())
                .build()
                .parts()
        addCollection(StarActivity.STAR.toString(), list) { it ->
            when (it) {
                is RefreshState.Failure -> context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                is RefreshState.Success -> {
                    if (it.message.error_code == 0) {
                        context?.let { it1 -> Toasty.success(it1, "收藏成功", Toast.LENGTH_SHORT).show() }
                        isCollected = true
                        ivStar.setImageResource(R.drawable.exam_ic_star_filled)
                    }
                }
            }
        }
    }

    private fun cancelCollectProblem() {
        val list = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ques_type", type.toString())
                .addFormDataPart("ques_id", problemID.toString())
                .build()
                .parts()
        deleteCollection(StarActivity.STAR.toString(), list) { it ->
            when (it) {
                is RefreshState.Failure -> context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                is RefreshState.Success -> {
                    if (it.message.error_code == 0) {
                        context?.let { it1 -> Toasty.success(it1, "取消收藏", Toast.LENGTH_SHORT).show() }
                        isCollected = false
                        ivStar.setImageResource(R.drawable.exam_ic_star_blank)
                    }
                }
            }
        }
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
        if (mode == PRACTICE_MODE && clickable) {
            when (type) {
                SINGLE_CHOICE, TRUE_FALSE -> onSelectionClickForPracticeSingle(clickId)
                MULTI_CHOICE -> onSelectionClickForPracticeMulti(clickId)
            }
        } else if (mode == TEST_MODE && clickable) {
            onSelectionClickForTest(clickId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onSelectionClickForPracticeSingle(clickId: Int) {
        val adapter = rvSelections.adapter as ItemAdapter
        val optionList: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
        for (i in 0 until optionList.size) {
            when (i) {
                answerFromRemote.selectionIndexToInt() -> optionList[i] = SelectionItem(optionList[i] as SelectionItem, SelectionItem.TRUE)
                clickId -> optionList[i] = SelectionItem(optionList[i] as SelectionItem, SelectionItem.FALSE)
            }
        }
        adapter.itemManager.refreshAll(optionList)
        clickable = false
        answerIsShown = true
        divider.visibility = View.VISIBLE
        tvAnswer.visibility = View.VISIBLE
        val isRight = clickId == answerFromRemote.selectionIndexToInt()
        mActivity.storeSelectionForPractice(fragmentIndex, listOf(clickId), isRight)
        tvAnswerUser.text = "你的答案: ${mActivity.userSelectionsForPractice[fragmentIndex].toSelectionIndex()}"
        tvAnswerUser.visibility = View.VISIBLE
        if (isRight) {
            tvRightOrWrong.text = "回答正确"
            context?.let { ContextCompat.getColor(it, R.color.examTextBlue) }?.let { tvRightOrWrong.setTextColor(it) }
        } else {
            tvRightOrWrong.text = "回答错误"
            context?.let { ContextCompat.getColor(it, R.color.examTextRed) }?.let { tvRightOrWrong.setTextColor(it) }
            val list = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ques_type", type.toString())
                    .addFormDataPart("ques_id", problemID.toString())
                    .addFormDataPart("error_answer", clickId.toSelectionIndex())
                    .build()
                    .parts()
            addCollection(StarActivity.WRONG.toString(), list) {
                when (it) {
                    is RefreshState.Failure -> context?.let { it1 -> Toasty.error(it1, "网络错误", Toast.LENGTH_SHORT).show() }
                    is RefreshState.Success -> {
                        if (it.message.error_code == 0) {
                            context?.let { it1 -> Toasty.info(it1, "已加入错题本", Toast.LENGTH_SHORT).show() }
                        }
                    }
                }
            }
        }

    }

    private fun onSelectionClickForPracticeMulti(clickId: Int) {
        when (clickId) {
            in multiSelectionAnswers -> multiSelectionAnswers.remove(clickId)
            else -> multiSelectionAnswers.add(clickId)
        }
        refreshMultiSelectionAnswers()
    }

    @SuppressLint("SetTextI18n")
    private fun onConfirmButtonClick() {
        if (mode == PRACTICE_MODE) {
            clickable = false
            val adapter = rvSelections.adapter as ItemAdapter
            val optionList: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()
            for (i in multiSelectionAnswers) {
                if (i in answerFromRemote.multiSelectionIndexToInt())
                    optionList[i] = SelectionItem(optionList[i] as SelectionItem, SelectionItem.TRUE)
                else
                    optionList[i] = SelectionItem(optionList[i] as SelectionItem, SelectionItem.FALSE)
            }

            for (i in answerFromRemote.multiSelectionIndexToInt()) {
                optionList[i] = SelectionItem(optionList[i] as SelectionItem, SelectionItem.TRUE)
            }

            adapter.itemManager.refreshAll(optionList)

            val isRight = answerFromRemote.multiSelectionIndexToInt().isRightWith(multiSelectionAnswers)
            if (isRight) {
                tvRightOrWrong.text = "回答正确"
                context?.let { ContextCompat.getColor(it, R.color.examTextBlue) }?.let { tvRightOrWrong.setTextColor(it) }
            } else {
                tvRightOrWrong.text = "回答错误"
                context?.let { ContextCompat.getColor(it, R.color.examTextRed) }?.let { tvRightOrWrong.setTextColor(it) }
            }
            tvAnswer.visibility = View.VISIBLE
            mActivity.storeSelectionForPractice(fragmentIndex, multiSelectionAnswers, isRight)
            tvAnswerUser.text = "你的答案: ${mActivity.userSelectionsForPractice[fragmentIndex].toSelectionIndex()}"
            tvAnswerUser.visibility = View.VISIBLE
        } else if (mode == TEST_MODE) {
            val answerString = when (type) {
                SINGLE_CHOICE, TRUE_FALSE -> singleSelectionAnswer.toSelectionIndex()
                MULTI_CHOICE -> multiSelectionAnswers.toSelectionIndex()
                else -> "W"
            }
            val problemIndex: ProblemIndex = if (mode == PRACTICE_MODE) {
                when (multiSelectionAnswers == answerFromRemote.multiSelectionIndexToInt()) {
                    true -> ProblemIndex.TRUE
                    else -> ProblemIndex.WRONG
                }
            } else ProblemIndex.TRUE
            mActivity.storeResult(fragmentIndex, UpdateResultViewModel(problemID, answerString, type), problemIndex)
        }
    }

    private fun onSelectionClickForTest(clickId: Int) {
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
                mActivity.storeResult(fragmentIndex, UpdateResultViewModel(problemID, singleSelectionAnswer.toSelectionIndex(), type), ProblemIndex.TRUE)
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
        if (mActivity.userSelectionsForTest[fragmentIndex] != null && mActivity.userSelectionsForTest[fragmentIndex]!!.answer != "") {
            multiSelectionAnswers = mActivity.userSelectionsForTest[fragmentIndex]!!.answer.multiSelectionIndexToInt()
            val adapter = rvSelections.adapter as ItemAdapter
            val list: MutableList<Item> = adapter.itemManager.itemListSnapshot.toMutableList()

            when (mode) {
                PRACTICE_MODE -> {
                    clickable = false
                    repeat(list.size) {
                        when (it) {
                            answerFromRemote.selectionIndexToInt() -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.TRUE)
                            in multiSelectionAnswers -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.FALSE)
                        }
                    }
                    adapter.itemManager.refreshAll(list)
                    answerIsShown = true
                    divider.visibility = View.VISIBLE
                    tvAnswer.visibility = View.VISIBLE
                }
                TEST_MODE -> {
                    clickable = false
                    repeat(list.size) {
                        when (it) {
                            in multiSelectionAnswers -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.TRUE)
                            else -> list[it] = SelectionItem(list[it] as SelectionItem, SelectionItem.NONE)
                        }
                    }
                    adapter.itemManager.refreshAll(list)
                }
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
                tvAnswerUser.visibility = View.GONE
                btConfirm.visibility = View.GONE
            }
            else -> {
                divider.visibility = View.GONE
                tvAnswer.visibility = View.GONE
                tvAnswerUser.visibility = View.GONE
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
