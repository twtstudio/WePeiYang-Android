package course.labs.classroomquery.CollectPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import course.labs.classroomquery.Model.ClassroomBean;
import course.labs.classroomquery.Model.SpacesItemDecoration;
import course.labs.classroomquery.R;
import course.labs.classroomquery.common.RecyclerAdapter;

/**
 * Created by asus on 2017/1/24.
 */
public class CollectPageActivity extends Activity {
    private List<ClassroomBean> mCclassrooms;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private Button backButton;
    private TextView textView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int ITEM_TYPE_HOME = 0;
    private final int ITEM_TYPE_COLLECT = 1;
    private final boolean isInDebugmode = false;
    private Window window;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.purple));
        setContentView(R.layout.collect);
        initialView();
        getClassrooms();

    }
    public void initialView(){
        mCclassrooms = new ArrayList<ClassroomBean>();

        backButton = (Button)findViewById(R.id.backButton2);
        textView = (TextView)findViewById(R.id.wrongTextView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getClassrooms();
            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration decor = new SpacesItemDecoration(25);
        mRecyclerView.addItemDecoration(decor,-1);
        mAdapter = new RecyclerAdapter(getApplicationContext(),ITEM_TYPE_COLLECT);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void getClassrooms(){
        mCclassrooms.clear();
        mAdapter.clear();
        swipeRefreshLayout.setRefreshing(true);
       CollectPageController newController = new CollectPageController() {
           @Override
           public void onClassroomsReceive(List<ClassroomBean> classrooms) {
               swipeRefreshLayout.setRefreshing(false);
               if(classrooms!=null){
                   textView.setVisibility(View.GONE);
               }

               for(ClassroomBean classroom:classrooms){
                   if(classroom.isCollected()){
                       mCclassrooms.add(classroom);
                   }
               }
               mAdapter.addAll(mCclassrooms);
           }

           @Override
           public void onError() {

               swipeRefreshLayout.setRefreshing(false);
               textView.setVisibility(View.VISIBLE);
           }
       };
        new CollectPagePresenter(this,newController).getClassrooms();
        if(mAdapter==null){
            Log.i("mAdapter","是空的");
            return;
        }
        mAdapter.addAll(mCclassrooms);
        if(mAdapter.getItemCount()==0&&isInDebugmode){
            textView.setVisibility(View.GONE);
            ClassroomBean classroomBean =  new ClassroomBean(true,true,true,true,true,true,true,false,true,45,"B402");
            ClassroomBean classroomBean2 =  new ClassroomBean(false,true,true,true,true,true,true,false,true,45,"B402");

            mAdapter.add(classroomBean);
            mAdapter.add(classroomBean2);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        getClassrooms();
    }
   }
