package course.labs.classroomquery.homePage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kelin.mvvmlight.base.ViewModel;
import com.orhanobut.hawk.Hawk;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import course.labs.classroomquery.API.apiClient;
import course.labs.classroomquery.Model.ClassroomBean;
import course.labs.classroomquery.Model.SpacesItemDecoration;
import course.labs.classroomquery.Model.userId;
import course.labs.classroomquery.chooseXiaoqu.ChooseXiaoquActivity;
import course.labs.classroomquery.CollectPage.CollectPageActivity;
import course.labs.classroomquery.R;
import course.labs.classroomquery.common.RecyclerAdapter;
import course.labs.classroomquery.getUserId.getUserIdController;
import course.labs.classroomquery.getUserId.getUserIdPresenter;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

//@Route(path = "/classroom/main")
public class MainActivity extends AppCompatActivity implements ViewModel{
    private GradientDrawable drawable;

    private RecyclerView mRecyclerview;
    private RecyclerAdapter mAdapter;
    private PopupWindow popupWindow1, popupWindow2, popupWindow3;

    private ImageView image1, image2, image3;
    private TextView textView1, textView2, textView3, wrongTextview;
    private View view1, view2, view3;
    private Layout layout1, layout2, layout3;
    private FrameLayout frame1;
    private LinearLayout mainTable;
    private boolean hasPop1, hasPop2, hasPop3;
    private Button backButton, gpsButton, collectButton;
    private TableLayout tableLayout1, tableLayout2, tableLayout3, tableLayout4;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int curWeek;

    private Set<ClassroomBean> curClassroomBeen;
    private Set<ClassroomBean> nextClassroomBeen;
    private final int ITEM_TYPE_HOME = 0;
    private final int ITEM_TYPE_COLLECT = 1;

    public static int REQUEST_CODE = 0;
    public static int RESULT_CODE = 1;
    private static int PERMISSION_REQUSET_CODE = 1;
    private static int LOAD_FINISHED = 2;
    private int OLD_CAMPUS = 0;
    private int NEW_CAMPUS = 1;
    private int xiaoqu;

    private int backCnt = 0;//统计已经返回结果的线程个数，当达到所有时间筛选条件个数时取交集
    private int totalCnt = 0;//统计返回的线程个数
    private Button[] buttonarray1;
    private Button[] buttonarray2;
    private Button[] buttonarray3;

    //记录三个下拉框中buuton选中的状态
    private boolean[] checkState1;
    private boolean[] checkState2;
    private boolean[] checkState3;
    private Set<Integer> condition1_buiding;
    private Set<Integer> condition2_time;
    private Set<Integer> condition3;//0biaoshi
    private apiClient client;
    private Animation animation, animation2;

    private final boolean isInDebugmode = false;
    private Set<ClassroomBean>[] setArray;
    private Handler mUIHandler;
    private Window window;

    private int statusBarHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //不稳定提醒

        String is_accept_unstable = "is_accept_classroom_unstable";

        boolean isAccept = Hawk.get(is_accept_unstable,false);

        if (!isAccept){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("此功能模块尚处于测试阶段，体验可能不稳定...")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("不再显示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Hawk.put(is_accept_unstable,true);
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }



       // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.purple));
        }
        int resourceId =getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        else{
            Log.i("状态栏","id为0");
        }
        setContentView(R.layout.roomquery_activity_main);
        getId();
        dealWithPermissions();



        condition1_buiding = new HashSet<Integer>();
        condition2_time = new HashSet<Integer>();
        condition3 = new HashSet<Integer>();
        curClassroomBeen = new HashSet<ClassroomBean>();
        nextClassroomBeen = new HashSet<ClassroomBean>();
        checkState1 = new boolean[100];
        checkState2 = new boolean[100];
        checkState3 = new boolean[100];
        buttonarray1 = new Button[100];
        buttonarray2 = new Button[100];
        buttonarray3 = new Button[100];
        client = new apiClient();
        setArray = new Set[300];
        for (int i = 0; i < setArray.length; i++) {
            setArray[i] = new HashSet<>();
        }

        mUIHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOAD_FINISHED) {
                    Log.i("Handler", "收到消息");
                    synchronized (this) {
                        for (int i = 1; i < backCnt; i++) {
                            Iterator<ClassroomBean> iterator = setArray[i].iterator();
                            while (iterator.hasNext()) {
                                ClassroomBean classroomBean = iterator.next();
                                if (!setArray[i - 1].contains(classroomBean)) {
                                    System.out.println();
                                    iterator.remove();
                                }
                            }
                        }


                        curClassroomBeen.addAll(setArray[backCnt - 1]);
                        shaixuan();

                        mAdapter.addAll(curClassroomBeen);
                        backCnt = 0;
                    }
                }
            }
        };
        initialViews();

        initialListenersAndAmination();

        if (!loadCampus()) {
            dialog();
            initialButton();
            initialConditions();
        } else {
            initialButton();
            initialConditions();
            refreshData();
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        pxTOdP();
    }
    public void initialViews() {
        frame1 = (FrameLayout) findViewById(R.id.frame1);
        mainTable = (LinearLayout) findViewById(R.id.mainTable);
        backButton = (Button) findViewById(R.id.backButton);
        gpsButton = (Button) findViewById(R.id.positionButton);
        collectButton = (Button) findViewById(R.id.collectButton);


        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView1);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(getApplicationContext(), ITEM_TYPE_HOME);
        mRecyclerview.setAdapter(mAdapter);
        RecyclerView.ItemDecoration decor = new SpacesItemDecoration(25);
        mRecyclerview.addItemDecoration(decor, -1);
        ((SimpleItemAnimator) mRecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.assist_color_1,R.color.assist_color_2,R.color.colorPrimary);


        image1 = (ImageView) findViewById(R.id.arrow1);
        image2 = (ImageView) findViewById(R.id.arrow2);
        image3 = (ImageView) findViewById(R.id.arrow3);
        textView1 = (TextView) findViewById(R.id.condition1);
        textView2 = (TextView) findViewById(R.id.condition2);
        textView3 = (TextView) findViewById(R.id.condition3);
        wrongTextview = (TextView) findViewById(R.id.wrongTextView);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = inflater.inflate(R.layout.popupwindow1_layout, null);
        view2 = inflater.inflate(R.layout.popupwindow2_layout, null);
        view3 = inflater.inflate(R.layout.popupwindow3_layout, null);

        tableLayout1 = (TableLayout) view1.findViewById(R.id.table1);
        tableLayout2 = (TableLayout) view1.findViewById(R.id.table2);
        tableLayout3 = (TableLayout) view2.findViewById(R.id.table3);
        tableLayout4 = (TableLayout) view3.findViewById(R.id.table4);

        popupWindow1 = new PopupWindow(view1, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow2 = new PopupWindow(view2, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow3 = new PopupWindow(view3, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


//        popupWindow1.setFocusable(true);
//        popupWindow2.setFocusable(true);
//        popupWindow3.setFocusable(true);

        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                disimissPopupWindow1();
            }
        });
        popupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                disimissPopupWindow2();
            }
        });
        popupWindow3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                disimissPopupWindow3();
            }
        });
        hasPop1 = false;
        hasPop2 = false;
        hasPop3 = false;
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);//创建动画
        animation.setInterpolator(new LinearInterpolator());//
        animation.setFillAfter(!animation.getFillAfter());//
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate2);//创建动画
        animation2.setInterpolator(new LinearInterpolator());//
        animation2.setFillAfter(!animation.getFillAfter());//

    }

    public void initialListenersAndAmination() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CollectPageActivity.class);
                startActivity(intent);
            }
        });
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ChooseXiaoquActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // image1.startAnimation(animation);
                if (hasPop1) {
                    disimissPopupWindow1();
                } else {
                    showPopupWindow1();
                }
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // image1.startAnimation(animation);
                if (hasPop1) {
                    disimissPopupWindow1();
                } else {
                    showPopupWindow1();
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // image2.startAnimation(animation);
                if (hasPop2) {
                    disimissPopupWindow2();
                } else {
                    showPopupWindow2();
                }
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // image2.startAnimation(animation);
                if (hasPop2) {
                    disimissPopupWindow2();
                } else {
                    showPopupWindow2();
                }
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //image3.startAnimation(animation);
                if (hasPop3) {
                    disimissPopupWindow3();
                } else {
                    showPopupWindow3();
                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // image3.startAnimation(animation);
                if (hasPop2) {
                    disimissPopupWindow3();
                } else {
                    showPopupWindow3();
                }
            }
        });


    }

    public void initialButton() {

        int cnt1 = tableLayout1.getChildCount();
        int tagCnt = 0;
        for (int i = 0; i < cnt1; i++) {
            View view = tableLayout1.getChildAt(i);
            if (view instanceof TableRow) {
                int cnt2 = ((TableRow) view).getChildCount();
                for (int j = 0; j < cnt2; j++) {
                    if (((TableRow) view).getChildAt(j) instanceof Button) {
                        buttonarray1[tagCnt] = (Button) ((TableRow) view).getChildAt(j);
                        tagCnt++;
                        switch (tagCnt) {
                            case 1:
                                if (loadCommonItems()) {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("常用");
                                } else {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("推荐");
                                }
                                break;
                            case 2:
                                if (xiaoqu == NEW_CAMPUS) {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("45楼");
                                } else {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("23楼");
                                }
                                break;
                            case 3:
                                if (xiaoqu == NEW_CAMPUS) {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("46楼");
                                } else {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("26楼");
                                }
                                break;
                            case 4:
                                break;
                            default:
                                if (xiaoqu == OLD_CAMPUS) {
                                    if (tagCnt <= 34) {
                                        ((Button) ((TableRow) view).getChildAt(j)).setText(tagCnt - 4 + "楼");
                                    } else {
                                        ((Button) ((TableRow) view).getChildAt(j)).setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    if (tagCnt <= 29) {
                                        ((Button) ((TableRow) view).getChildAt(j)).setText(30 + tagCnt - 3 + "楼");
                                    } else {
                                        ((Button) ((TableRow) view).getChildAt(j)).setVisibility(View.INVISIBLE);
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        }
        cnt1 = tableLayout2.getChildCount();

        for (int i = 0; i < cnt1; i++) {
            View view = tableLayout2.getChildAt(i);
            if (view instanceof TableRow) {
                int cnt2 = ((TableRow) view).getChildCount();
                for (int j = 0; j < cnt2; j++) {
                    if (((TableRow) view).getChildAt(j) instanceof Button) {
                        buttonarray1[tagCnt] = (Button) ((TableRow) view).getChildAt(j);
                        tagCnt++;
                        switch (tagCnt) {
                            case 1:
                                if (loadCommonItems()) {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("常用");
                                } else {
                                    ((Button) ((TableRow) view).getChildAt(j)).setText("推荐");
                                }
                                break;
                            case 2:
                                ((Button) ((TableRow) view).getChildAt(j)).setText("45楼");
                                break;
                            case 3:
                                ((Button) ((TableRow) view).getChildAt(j)).setText("46楼");
                                break;
                            case 4:
                                break;
                            default:
                                if (xiaoqu == OLD_CAMPUS) {
                                    if (tagCnt <= 34) {
                                        ((Button) ((TableRow) view).getChildAt(j)).setText(tagCnt - 4 + "楼");
                                    } else {
                                        ((Button) ((TableRow) view).getChildAt(j)).setVisibility(View.INVISIBLE);
                                    }
                                } else {
                                    if (tagCnt <= 29) {
                                        ((Button) ((TableRow) view).getChildAt(j)).setText(30 + tagCnt - 4 + "楼");
                                    } else {
                                        ((Button) ((TableRow) view).getChildAt(j)).setVisibility(View.INVISIBLE);
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        }
        System.out.println(tagCnt);
        final int temp1 = tagCnt;
        if (xiaoqu == OLD_CAMPUS) {

            for (int i = 1; i < tagCnt; i++) {
                final int tempi = i;
                buttonarray1[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ButtonClickListner(MainActivity.this, tempi, buttonarray1, checkState1, 0, temp1).onClick();
                    }
                });

            }
        } else {
            for (int i = 1; i < tagCnt; i++) {
                final int tempi = i;
                buttonarray1[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ButtonClickListner(MainActivity.this, tempi, buttonarray1, checkState1, 0, temp1).onClick();
                    }
                });

            }
        }

        int cnt2 = tableLayout3.getChildCount();
        tagCnt = 0;
        for (int i = 0; i < cnt2; i++) {
            View view = tableLayout3.getChildAt(i);
            if (view instanceof TableRow) {
                for (int j = 0; j < ((TableRow) view).getChildCount(); j++) {
                    View tempView = ((TableRow) view).getChildAt(j);
                    if (tempView instanceof Button) {
                        buttonarray2[tagCnt] = (Button) tempView;


                        tagCnt++;
                    }
                }
            }
        }
        final int temp2 = tagCnt;
        for (int i = 0; i < tagCnt; i++) {
            final int tempi = i;
            buttonarray2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ButtonClickListner(MainActivity.this, tempi, buttonarray2, checkState2, 1, temp2).onClick();
                }
            });
        }
        int cnt3 = tableLayout4.getChildCount();
        tagCnt = 0;
        for (int i = 0; i < cnt3; i++) {
            View view = tableLayout4.getChildAt(i);
            if (view instanceof TableRow) {
                for (int j = 0; j < ((TableRow) view).getChildCount(); j++) {
                    View tempView = ((TableRow) view).getChildAt(j);
                    if (tempView instanceof Button) {
                        buttonarray3[tagCnt] = (Button) tempView;

                        tagCnt++;
                    }
                }
            }
        }
        final int temp3 = tagCnt;
        for (int i = 0; i < tagCnt; i++) {
            final int tempi = i;
            buttonarray3[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ButtonClickListner(MainActivity.this, tempi, buttonarray3, checkState3, 2, temp3).onClick();
                }
            });
        }
    }

    public void initialConditions() {
        drawable = new GradientDrawable();
        drawable.setCornerRadius(15);
        drawable.setStroke(3, Color.parseColor("#9932CC"));
        drawable.setColor(Color.parseColor("#00000000"));
        checkState1[1] = true;
        checkState1[2] = true;
        buttonarray1[0].setTextColor(getResources().getColor(R.color.black));
        buttonarray1[1].setTextColor(getResources().getColor(R.color.purple));
        buttonarray1[2].setTextColor(getResources().getColor(R.color.purple));
        buttonarray1[1].setBackgroundDrawable(drawable);
        buttonarray1[2].setBackgroundDrawable(drawable);
        checkState2[0] = true;
        buttonarray2[0].setTextColor(getResources().getColor(R.color.purple));
        buttonarray2[0].setBackgroundDrawable(drawable);
    }

    public void showPopupWindow1() {
        image1.startAnimation(animation);
        if (hasPop2) {
            disimissPopupWindow2();
        }
        if (hasPop3) {
            disimissPopupWindow3();
        }
        // mAdapter.clear();

        if (!hasPop1) {
            popupWindow1.showAtLocation(MainActivity.this.findViewById(R.id.mainFrame), Gravity.TOP, 0, statusBarHeight+findViewById(R.id.card).getHeight());
        }
        hasPop1 = true;
    }

    public void showPopupWindow2() {
        image2.startAnimation(animation);
        if (hasPop1) {
            disimissPopupWindow1();
        }
        if (hasPop3) {
            disimissPopupWindow3();
        }
        // mAdapter.clear();

        if (!hasPop2) {
            popupWindow2.showAtLocation(MainActivity.this.findViewById(R.id.mainFrame), Gravity.TOP, 0,statusBarHeight+findViewById(R.id.card).getHeight());
        }
        hasPop2 = true;
    }

    public void showPopupWindow3() {
        image3.startAnimation(animation);
        if (hasPop1) {
            disimissPopupWindow1();
        }
        if (hasPop2) {
            disimissPopupWindow2();
        }
        //  mAdapter.clear();

        if (!hasPop3) {
            popupWindow3.showAtLocation(MainActivity.this.findViewById(R.id.mainFrame), Gravity.TOP, 0,statusBarHeight+ findViewById(R.id.card).getHeight());
        }
        hasPop3 = true;
    }

    public void disimissPopupWindow1() {
        image1.startAnimation(animation2);
        if (hasPop1) {
            popupWindow1.dismiss();
        }
        hasPop1 = false;
        if (!hasPop1 && (!hasPop2) && (!hasPop3)) {
            refreshData();
        }
    }

    public void disimissPopupWindow2() {
        image2.startAnimation(animation2);
        if (hasPop2) {
            popupWindow2.dismiss();
        }
        hasPop2 = false;
        if (!hasPop1 && (!hasPop2) && (!hasPop3)) {
            refreshData();
        }
    }

    public void disimissPopupWindow3() {
        image3.startAnimation(animation2);
        if (hasPop3) {
            popupWindow3.dismiss();
        }
        hasPop3 = false;
        if (!hasPop1 && (!hasPop2) && (!hasPop3)) {
            refreshData();
        }
    }

    public void refreshData() {
        //移动到刷新完成后
//        mAdapter.clear();
        condition1_buiding.clear();
        condition2_time.clear();
        condition3.clear();
        curClassroomBeen.clear();
        nextClassroomBeen.clear();
        for (int i = 0; i < setArray.length; i++) {
            setArray[i].clear();
        }
        backCnt = 0;
        totalCnt = 0;
        if(condition1_buiding.size()>0&&condition2_time.size()>0) {
            swipeRefreshLayout.setRefreshing(true);
        }
        if (xiaoqu == OLD_CAMPUS) {
            if (checkState1[1]) {
                condition1_buiding.add(Integer.valueOf(23));
            }
            if (checkState1[2]) {
                condition1_buiding.add(Integer.valueOf(26));
            }
            if (checkState1[3]) {
                for (int i = 1; i <= 30; i++) {
                    condition1_buiding.add(Integer.valueOf(i));
                }
            }
            for (int i = 4; i < 34; i++) {
                if (checkState1[i]) {
                    condition1_buiding.add(Integer.valueOf(i - 3));
                }
            }
        } else {
            if (checkState1[1]) {
                condition1_buiding.add(Integer.valueOf(45));
            }
            if (checkState1[2]) {
                condition1_buiding.add(Integer.valueOf(46));
            }
            if (checkState1[3]) {
                for (int i = 31; i <= 55; i++) {
                    condition1_buiding.add(Integer.valueOf(i));
                }
            }
            for (int i = 4; i < 34; i++) {
                if (checkState1[i]&&i!=32) {

                    condition1_buiding.add(Integer.valueOf(i - 4 + 30 + 1));
                }
            }
        }
        if (checkState2[0]) {
            condition2_time.add(0);
        }
        if (checkState2[1]) {
            condition2_time.add(Integer.valueOf(1));
            condition2_time.add(Integer.valueOf(3));
            condition2_time.add(Integer.valueOf(5));
            condition2_time.add(Integer.valueOf(7));
            condition2_time.add(Integer.valueOf(9));
        }
        for (int i = 2; i <= 6; i++) {
            if (checkState2[i]) {
                condition2_time.add(Integer.valueOf(2 * (i - 1) - 1));
            }
        }
        homePageController newController = new homePageController() {
            @Override
            public void onNowClassroomReceived(Set<ClassroomBean> classrooms) {
                wrongTextview.setVisibility(View.GONE);
                addTotalCnt();
                if (classrooms != null) {
                    synchronized (this) {
                        setArray[backCnt++].addAll(classrooms);
                        if (backCnt == condition2_time.size()*condition1_buiding.size()) {
                            /*Message message = mUIHandler.obtainMessage(LOAD_FINISHED);
                            message.sendToTarget();*/
                            for (int i = 0; i < backCnt; i++) {

                                Iterator<ClassroomBean> iterator = setArray[i].iterator();
                                while (iterator.hasNext()) {

                                    ClassroomBean classroomBean = iterator.next();
                                    Log.i("教学楼遍历",i+" : "+classroomBean.getRoomNumber());
                                    boolean isAllExist = true;
                                    for(int j = 0;j < backCnt;j++){
                                        Log.i("教学楼",classroomBean.getBuilding()+" "+setArray[j].iterator().next().getBuilding());
                                        if(j!=i) {
                                            if ((setArray[j].iterator().hasNext()&&setArray[j].iterator().next().getBuilding()==classroomBean.getBuilding())) {
                                                boolean isExist = false;
                                                for(ClassroomBean bean:setArray[j]){
                                                    if(bean.equals(classroomBean)){
                                                        isExist = true;
                                                    }
                                                }
                                                if(!isExist) {
                                                    isAllExist = false;
                                                    Log.i("筛选", classroomBean.getRoomNumber() + "backCnt" + backCnt);
                                                }

                                            }
                                        }
                                    }
                                    if (isAllExist) {
                                        curClassroomBeen.add(classroomBean);
                                    }
                                }
                            }



                            shaixuan();

                            mAdapter.clear();
                            mAdapter.addAll(curClassroomBeen);


                        }
                    }
                    Log.i("Controller", "返回数据不为空");
                } else {
                    Log.i("Controller", "返回数据为空");
                }




                //swipeRefreshLayout.setRefreshing(false);
            }

             /*  @Override
               public void onNextClassroomReceived(Set<ClassroomBean> classrooms) {
                   wrongTextview.setVisibility(View.GONE);
                  nextClassroomBeen.addAll(classrooms);
                   swipeRefreshLayout.setRefreshing(false);
               }*/

            @Override
            public void onError(int build) {
                Log.i("Controller", "OnError");

                addTotalCnt();


                    wrongTextview.setVisibility(View.VISIBLE);

            }
        };
        homePagePresenter mPresenter = new homePagePresenter(this, newController);
        System.out.println("条件一个数" + condition1_buiding.size() + "条件二个数" + condition2_time.size());
        for (int building : condition1_buiding) {
            for (int time : condition2_time) {


                mPresenter.getCurClassrooms(building, time);
                //mPresenter.getNextClassrooms(building,time);
            }
        }
       /* for(ClassroomBean classroomBean : curClassroomBeen){
            if(nextClassroomBeen.contains(classroomBean)){
                classroomBean.setWillBeOccupied(false);
            }
            else{
                classroomBean.setWillBeOccupied(true);
            }
        }*/


        if (mAdapter.getItemCount() == 0 && isInDebugmode) {
            wrongTextview.setVisibility(View.GONE);
            ClassroomBean classroomBean = new ClassroomBean(true, true, true, true, true, true, true, false, true, 45, "B402");
            ClassroomBean classroomBean2 = new ClassroomBean(false, true, true, true, true, true, true, false, true, 45, "B402");
            curClassroomBeen.add(classroomBean);
            curClassroomBeen.add(classroomBean2);
            shaixuan();
            mAdapter.addAll(curClassroomBeen);
        } else if ((mAdapter.getItemCount() == 0 && (!isInDebugmode))) {
            //wrongTextview.setVisibility(View.VISIBLE);
        }
        System.out.println("适配器中元素个数" + mAdapter.getItemCount());

    }

    private boolean res = false;

    public boolean loadCampus() {
        File file = new File(Environment.getExternalStorageDirectory() + "/TWT" + "/Cache.txt");
        if (!file.exists()) {
            setXiaoqu(NEW_CAMPUS);
            return false;
        } else {
            res = true;
            Observable.just(file).map(new Func1<File, Integer>() {
                @Override
                public Integer call(File file) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        int tempRes = -1;
                        String temp = null;
                        while (null != (temp = reader.readLine())) {
                            tempRes = Integer.valueOf(temp);
                        }
                        return Integer.valueOf(tempRes);
                    } catch (FileNotFoundException e) {

                        Log.i("File", "No exist");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }).subscribe(new Observer<Integer>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Integer integer) {

                    if (integer != null) {
                        setXiaoqu(integer);
                    } else {
                        res = false;
                    }
                }
            });


            return res;
        }
    }

    public void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您还未设置校区，请前去设置");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ChooseXiaoquActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }


        });

        builder.create().show();
    }

    public boolean loadCommonItems() {
        return false;
    }

    public int getNowCourse() {
        Date date = new Date(System.currentTimeMillis());

        switch (date.getHours()) {
            case 8: {
                if (date.getMinutes() < 30) {
                    return 0;
                } else {
                    return 1;
                }

            }
            case 9:
                return 1;
            case 10:
                if (date.getMinutes() < 25) {
                    return 1;
                } else {
                    return 3;
                }
            case 11:
                return 3;

            case 13:
                return 3;
            case 14:
                return 5;
            case 15:
                if (date.getMinutes() < 25) {
                    return 5;
                } else {
                    return 7;
                }

            case 16:
                return 7;
            case 17:
                return 7;
            case 18:
                if (date.getMinutes() < 30) {
                    return 7;
                } else {
                    return 9;
                }

            case 19:
                return 9;
            case 20:
                return 9;
            case 21:
                return 9;
            default:
                return 12;
        }
    }

    public void writeItem(int xiaoqu) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/TWT");
            if (!file.exists()) {
                try {
                    if (!file.mkdirs()) {
                        System.out.println("创建失败");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("创建文件异常");
                }
            }
            File file2 = new File(Environment.getExternalStorageDirectory() + "/TWT" + "/Cache.txt");
            if (!file2.exists()) {

                file2.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2, true));
            bw.write(xiaoqu + "");
            bw.newLine();
            bw.flush();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("写入异常");
        }

    }

    public void setXiaoqu(int xiaoqu) {
        this.xiaoqu = xiaoqu;
        mAdapter.setXiaoqu(xiaoqu == NEW_CAMPUS ? true : false);
    }

    public void shaixuan() {
        Iterator<ClassroomBean> iterator = curClassroomBeen.iterator();
        while (iterator.hasNext()) {
            ClassroomBean classroomBean = iterator.next();
            boolean[] conditions = new boolean[10];
            conditions[1] = classroomBean.isHasHeat();
            conditions[2] = classroomBean.isHasAC();
            conditions[3] = classroomBean.isHasElectricity();
            conditions[4] = classroomBean.isNearToliet();
            conditions[5] = classroomBean.isNearWater();
            if (checkState3[0]) {
                for (int i = 1; i < 6; i++) {
                    if (!conditions[i]) {
                        iterator.remove();
                        break;
                    }
                }
            } else {
                for (int i = 1; i <= 5; i++) {
                    if (checkState3[i] && (!conditions[i])) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    public void dealWithPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUSET_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUSET_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    public synchronized void addTotalCnt() {
        totalCnt++;
        if (totalCnt == condition1_buiding.size() * condition2_time.size()) {
            swipeRefreshLayout.setRefreshing(false);
            System.out.println("返回达到了教学楼数");
        }
    }
    public void getId(){
        getUserIdController mController = new getUserIdController() {
            @Override
            public void onNext(userId id) {
                System.out.println("请求学号是"+id.getID());
                Hawk.put("userId",id.getID());

            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(),"获得信息出错",Toast.LENGTH_SHORT).show();
            }
        };
        getUserIdPresenter mPresenter = new getUserIdPresenter(this,mController);
        mPresenter.getUserId(CommonPrefUtil.getToken());
    }
    public void pxTOdP(){
        float scale = getResources().getDisplayMetrics().density;
        Log.i("缩放尺寸",scale+"");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_REQUSET_CODE && permissions.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        client.unSubscribe(this);
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("OnActivityResult", "调用");
        Bundle bundle = data.getExtras();
        mAdapter.clear();
        condition1_buiding.clear();
        condition2_time.clear();
        condition3.clear();


        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            if (bundle.getInt("xiaoqu") == OLD_CAMPUS) {
                setXiaoqu(OLD_CAMPUS);
                writeItem(OLD_CAMPUS);
                Log.i("返回值", "尝试写入");
            }
            if (bundle.getInt("xiaoqu") == NEW_CAMPUS) {
                setXiaoqu(NEW_CAMPUS);
                writeItem(NEW_CAMPUS);
                Log.i("返回值", "尝试写入");
            }
            initialButton();
            refreshData();
        }
    }
}
