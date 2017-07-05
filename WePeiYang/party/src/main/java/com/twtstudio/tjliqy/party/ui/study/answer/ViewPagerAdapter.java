package com.twtstudio.tjliqy.party.ui.study.answer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.bean.QuizInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjliqy on 2016/8/24.
 */
public class ViewPagerAdapter extends PagerAdapter implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private List<QuizInfo.DataBean> list;

    private Context context;

    private StudyAnswerBridge bridge;

    private int position;

    boolean[][] isSelected;

    public ViewPagerAdapter(Context context, StudyAnswerBridge bridge, List<QuizInfo.DataBean> list) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(list);
        this.bridge = bridge;
        isSelected = new boolean[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < 5; j++) {
                isSelected[i][j] = false;
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        QuizInfo.DataBean data = list.get(position);
        View view;
        if (data.getExercise_type() == 0) {//单选题
            view = View.inflate(context, R.layout.fragment_party_answer, null);
            TextView title = (TextView) view.findViewById(R.id.tv_answer_title);
            RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg_answer);
            RadioButton rbA = (RadioButton) view.findViewById(R.id.rb_a);
            RadioButton rbB = (RadioButton) view.findViewById(R.id.rb_b);
            RadioButton rbC = (RadioButton) view.findViewById(R.id.rb_c);
            RadioButton rbD = (RadioButton) view.findViewById(R.id.rb_d);
            RadioButton rbE = (RadioButton) view.findViewById(R.id.rb_e);

            title.setText((position + 1) + ".【单选题】" + data.getExercise_content());
            setRadioButton(rbA, data.getChoose().get(0));
            setRadioButton(rbB, data.getChoose().get(1));
            setRadioButton(rbC, data.getChoose().get(2));
            setRadioButton(rbD, data.getChoose().get(3));
            if (data.getChoose().size() >= 5) {
                setRadioButton(rbE, data.getChoose().get(4));
                rbE.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 5; i++) {
                if (isSelected[position][i]) {
                    switch (i) {
                        case 0:
                            rg.check(rbA.getId());
                            break;
                        case 1:
                            rg.check(rbB.getId());
                            break;
                        case 2:
                            rg.check(rbC.getId());
                            break;
                        case 3:
                            rg.check(rbD.getId());
                            break;
                        case 4:
                            rg.check(rbE.getId());
                            break;
                    }
                }
            }
            rg.setOnCheckedChangeListener(this);
        } else {//多选题
            view = View.inflate(context, R.layout.fragment_party_answer_check, null);

            TextView title = (TextView) view.findViewById(R.id.tv_answer_title);
            CheckBox cbA = (CheckBox) view.findViewById(R.id.cb_a);
            CheckBox cbB = (CheckBox) view.findViewById(R.id.cb_b);
            CheckBox cbC = (CheckBox) view.findViewById(R.id.cb_c);
            CheckBox cbD = (CheckBox) view.findViewById(R.id.cb_d);
            CheckBox cbE = (CheckBox) view.findViewById(R.id.cb_e);

            title.setText((position + 1) + ".【多选题】" + data.getExercise_content());
            setCheckBox(cbA, data.getChoose().get(0));
            setCheckBox(cbB, data.getChoose().get(1));
            setCheckBox(cbC, data.getChoose().get(2));
            setCheckBox(cbD, data.getChoose().get(3));
            if (data.getChoose().size() >= 5) {
                setCheckBox(cbE, data.getChoose().get(4));
                cbE.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < 5; i++) {
                if (isSelected[position][i]) {
                    switch (i) {
                        case 0:
                            cbA.setChecked(true);
                            break;
                        case 1:
                            cbB.setChecked(true);
                            break;
                        case 2:
                            cbC.setChecked(true);
                            break;
                        case 3:
                            cbD.setChecked(true);
                            break;
                        case 4:
                            cbE.setChecked(true);
                            break;
                    }
                }
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private void setCheckBox(CheckBox cb, QuizInfo.DataBean.ChooseBean choose) {
        cb.setText(choose.getName());
        cb.setOnCheckedChangeListener(this);
    }

    private void setRadioButton(RadioButton rb, QuizInfo.DataBean.ChooseBean choose) {
        rb.setText(choose.getName());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            int id = compoundButton.getId();
            if (id == R.id.cb_a) {
                bridge.setAnswer(position, list.get(position).getChoose().get(0).getPos());
                isSelected[position][0] = true;
            } else if (id == R.id.cb_b) {
                bridge.setAnswer(position, list.get(position).getChoose().get(1).getPos());
                isSelected[position][1] = true;
            } else if (id == R.id.cb_c) {
                bridge.setAnswer(position, list.get(position).getChoose().get(2).getPos());
                isSelected[position][2] = true;
            } else if (id == R.id.cb_d) {
                bridge.setAnswer(position, list.get(position).getChoose().get(3).getPos());
                isSelected[position][3] = true;
            } else if (id == R.id.cb_e) {
                bridge.setAnswer(position, list.get(position).getChoose().get(4).getPos());
                isSelected[position][4] = true;
            }
        } else {
            int id = compoundButton.getId();
            if (id == R.id.cb_a) {
                bridge.cancelAnswer(position, list.get(position).getChoose().get(0).getPos());
                isSelected[position][0] = false;
            } else if (id == R.id.cb_b) {
                bridge.cancelAnswer(position, list.get(position).getChoose().get(1).getPos());
                isSelected[position][1] = false;
            } else if (id == R.id.cb_c) {
                bridge.cancelAnswer(position, list.get(position).getChoose().get(2).getPos());
                isSelected[position][2] = false;
            } else if (id == R.id.cb_d) {
                bridge.cancelAnswer(position, list.get(position).getChoose().get(3).getPos());
                isSelected[position][3] = false;
            } else if (id == R.id.cb_e) {
                bridge.cancelAnswer(position, list.get(position).getChoose().get(4).getPos());
                isSelected[position][4] = false;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        for (int k = 0; k < 5; k++) {
            isSelected[position][k] = false;
        }
        if (id == R.id.rb_a) {
            bridge.setSingleAnswer(position, list.get(position).getChoose().get(0).getPos());
            isSelected[position][0] = true;
        } else if (id == R.id.rb_b) {
            bridge.setSingleAnswer(position, list.get(position).getChoose().get(1).getPos());
            isSelected[position][1] = true;
        } else if (id == R.id.rb_c) {
            bridge.setSingleAnswer(position, list.get(position).getChoose().get(2).getPos());
            isSelected[position][2] = true;
        } else if (id == R.id.rb_d) {
            bridge.setSingleAnswer(position, list.get(position).getChoose().get(3).getPos());
            isSelected[position][3] = true;
        } else if (id == R.id.rb_e) {
            bridge.setSingleAnswer(position, list.get(position).getChoose().get(4).getPos());
            isSelected[position][4] = true;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
