package com.bdpqchen.yellowpagesmodule.yellowpages.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.R;
import com.bdpqchen.yellowpagesmodule.yellowpages.R2;
import com.bdpqchen.yellowpagesmodule.yellowpages.adapter.SearchResultsListViewAdapter;
import com.bdpqchen.yellowpagesmodule.yellowpages.base.BaseActivity;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.DataManager;
import com.bdpqchen.yellowpagesmodule.yellowpages.data.SearchHelper;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CategoryFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.CollectedFragmentCallBack;
import com.bdpqchen.yellowpagesmodule.yellowpages.fragment.DepartmentFragment;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DataBean;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.DatabaseVersion;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.Phone;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.WordSuggestion;
import com.bdpqchen.yellowpagesmodule.yellowpages.network.NetworkClient;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PrefUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.PhoneUtils;
import com.bdpqchen.yellowpagesmodule.yellowpages.utils.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

@Route(path = "/yellowPage/main")
public class HomeActivity extends BaseActivity implements CollectedFragmentCallBack {

    private static final int REQUEST_CODE_WRITE_PHONE = 98;
    private static final int REQUEST_CODE_CALL_PHONE = 33;
    private static final String TAG = "HomeActivity";
    @BindView(R2.id.search_results_list)
    ListView mSearchResultsList;

    @BindView(R2.id.parent_view)
    RelativeLayout mParentView;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.floating_search_view)
    FloatingSearchView mSearchView;
    private static ProgressBar mProgressBar;
    private Context mContext;
    private SearchResultsListViewAdapter mSearchResultsAdapter;
    private ProgressDialog mProgressDialog;
    private String mLastQuery = "";
    private CollectedFragmentCallBack mRingUpCallBack;
    public int mDatabaseVersionCode = 1;
    private static int loadFragmentTimes = 0;
    private boolean isUpdatingDb = false;
    private String callPhoneNum = "";
    private String mWritePhoneName = "";
    private String mWritePhoneNum = "";


    @Override
    public int getLayout() {
        return R.layout.yp_activity_home;
    }

    @Override
    protected Toolbar getToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.yp_app_name));
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        this.mContext = this;
        this.mRingUpCallBack = this;
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        DepartmentFragment departmentFragment = new DepartmentFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_department, departmentFragment);
        fragmentTransaction.commit();
        setupSearchView();
        setupResultsList();
        if (!PrefUtils.isFirstOpen()) {
            setListViewShow();
            checkForUpdateDatabase();
        } else {
            showInitDialog();
            getDataList();
        }

    }

    public static void setProgressBarDismiss() {
        loadFragmentTimes++;
        if (loadFragmentTimes >= 2) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void setListViewShow() {
        CollectedFragment collectedFragment = new CollectedFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_collected, collectedFragment);
        fragmentTransaction.add(R.id.fragment_container_list, categoryFragment);
        fragmentTransaction.commit();
    }

    public void getDataList() {
        Subscriber subscriber = new Subscriber<DataBean>() {
            @Override
            public void onCompleted() {
                updateProgressDialogStatus(50);
            }

            @Override
            public void onError(Throwable e) {
                updateProgressDialogStatus(-1);
                updateProgressDialogStatus(0);
                showInitErrorDialog();
            }

            @Override
            public void onNext(DataBean dataBean) {
                Logger.i("onNext()");
                Logger.i(String.valueOf(dataBean.getCategory_list().size()));
                Logger.i(String.valueOf(dataBean.getCategory_list().get(0).getDepartment_list().size()));
                Logger.i(String.valueOf(dataBean.getCategory_list().get(0).getDepartment_list().get(0).getUnit_list().size()));
                initDatabase(dataBean);
            }
        };
        NetworkClient.getInstance().getDataList(subscriber);
        updateProgressDialogStatus(10);
    }

    private void initDatabase(final DataBean dataBean) {
        final long time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Phone> phoneList = new ArrayList<>();
                for (int i = 0; i < dataBean.getCategory_list().size(); i++) { //3个分类
                    updateProgressDialogStatus(10);
                    DataBean.CategoryListBean categoryList = dataBean.getCategory_list().get(i);
//                    Logger.d(i + "====i");
                    for (int j = 0; j < categoryList.getDepartment_list().size(); j++) {     //第i分类里的部门j
//                        Logger.d(j + "===j");
                        DataBean.CategoryListBean.DepartmentListBean departmentList = categoryList.getDepartment_list().get(j);
                        for (int k = 0; k < departmentList.getUnit_list().size(); k++) {     //第k部门里的单位
//                            Logger.d(k + "===k");
                            DataBean.CategoryListBean.DepartmentListBean.UnitListBean list = departmentList.getUnit_list().get(k);
//                            Logger.d(k + "===l");
                            Phone phone = new Phone();
                            phone.setCategory(i);
                            phone.setPhone(list.getItem_phone());
                            phone.setName(list.getItem_name());
                            phone.setIsCollected(0);
                            phone.setDepartment(departmentList.getDepartment_name());
                            phoneList.add(phone);
                        }
                    }
                }
                /*Logger.i(String.valueOf(phoneList.size()));
                Logger.i("DataList.size", phoneList.size() + "");
                Logger.i(String.valueOf(phoneList.get(0).getIsCollected()));
                Logger.i(String.valueOf(phoneList.get(1).getIsCollected()));
                Logger.d(System.currentTimeMillis() - time);*/
                DataManager.initPhoneDatabase(phoneList);
                updateProgressDialogStatus(10);
                initDbCompletely();
            }
        }).start();

    }

    private void initDbCompletely(){
        PrefUtils.setDatabaseVersion(mDatabaseVersionCode);
        Log.i("mDatabase version", String.valueOf(mDatabaseVersionCode));
        Log.i("Pref database version", String.valueOf(PrefUtils.getDatabaseVersion()));
        if (isUpdatingDb) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }else{
            setListViewShow();
            updateProgressDialogStatus(-1);
            PrefUtils.setIsFirstOpen(false);
        }
    }

    private void showInitDialog() {
        String msg = "首次使用，需要导入号码库，请等待...";
        if (isUpdatingDb){
            msg = "正在更新号码库，请等待...";
        }
        if (null == mProgressDialog){
            mProgressDialog = new ProgressDialog(this);
        }
        updateProgressDialogStatus(-10);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage(msg);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void showInitErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("出错啦")
                .setCancelable(false)
                .setMessage("\t导入失败，请检查网络是否可用")
                .setPositiveButton("重新导入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInitDialog();
                        getDataList();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    private void showUpdateDbDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提醒")
                .setMessage("\t发现最新版本号码库，请及时更新")
                .setNegativeButton("下次", null)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInitDialog();
                        getDataList();
                    }
                }).show();
    }

    private void setupSearchView() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {
                    mSearchView.showProgress();
                    SearchHelper.findSuggestions(newQuery, 5, new SearchHelper.OnFindSuggestionsListener() {

                        @Override
                        public void onResults(List<WordSuggestion> results) {
                            mSearchView.swapSuggestions(results);
                            mSearchView.hideProgress();
                        }

                    });
                }
                Logger.d("onSearchTextChanged()");
                mLastQuery = newQuery;
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                WordSuggestion wordSuggestion = (WordSuggestion) searchSuggestion;
                Logger.t(TAG).d(mLastQuery);
                String t = mLastQuery;
                mLastQuery = wordSuggestion.getBody();

                SearchHelper.findWord(wordSuggestion.getBody(), t,
                        new SearchHelper.OnFindWordListener() {
                            @Override
                            public void onResults(List<SearchResult> results) {
                                mSearchView.clearQuery();
                                mSearchResultsAdapter.swapData(results);
                                mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(SearchResult searchResult) {
                                        Logger.i("clicked the item, onSuggestionClicked()");

                                    }
                                });
                            }
                        });

                Logger.d("onSuggestionClicked()");
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                SearchHelper.findWord(query, mLastQuery,
                        new SearchHelper.OnFindWordListener() {
                            @Override
                            public void onResults(List<SearchResult> results) {
                                mSearchResultsAdapter.swapData(results);
                                mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(SearchResult searchResult) {
                                        Toast.makeText(mContext, "You clicked the item" + searchResult.name, Toast.LENGTH_LONG).show();
                                        Logger.d("you clicked the item" + searchResult.name);
                                    }
                                });
                            }
                        });
                Logger.d("onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.swapSuggestions(SearchHelper.getHistory(10));
                Logger.d("onFocus()");
            }

            @Override
            public void onFocusCleared() {
                if (mLastQuery.equals(DataManager.TOO_MUCH_DATA_NAME)) {
                    mSearchView.clearQuery();
                }
                Logger.d("onFocusCleared()");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.yp_action_clear_history) {
                    DataManager.clearHistory();
                    ToastUtils.show((Activity) mContext, "已清空搜索记录");
                }

            }
        });

        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                updateViewVisibility(View.GONE);
                Logger.d("onHomeClicked()");
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                WordSuggestion searchSuggestion = (WordSuggestion) item;
                if (searchSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }
            }

        });

        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListViewAdapter(this, null, mRingUpCallBack);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
    }

    @Override
    public void callPhone(String phoneNum) {
        this.callPhoneNum = phoneNum;
        PhoneUtils.permissionCheck(mContext, phoneNum, REQUEST_CODE_CALL_PHONE, null);
    }

    @Override
    public void saveToContact(String name, String phone) {
        this.mWritePhoneNum = phone;
        this.mWritePhoneName = name;
        PhoneUtils.permissionCheck(mContext, phone, name, REQUEST_CODE_WRITE_PHONE, null);
        Logger.i("home activity is called");
    }

    private void updateViewVisibility(int type) {
        mSearchView.setVisibility(type);
        mSearchResultsList.setVisibility(type);
        mParentView.setVisibility(type);
    }

    private void updateProgressDialogStatus(int what){
        if (null != mProgressDialog){
            if (what == -1){
                mProgressDialog.dismiss();
            }else{
                mProgressDialog.incrementProgressBy(what);
            }
        }
    }

    private void checkForUpdateDatabase() {
        final Subscriber subscriber = new Subscriber<DatabaseVersion>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                Logger.i("自动检查更新数据库的网络请求发生错误");
            }

            @Override
            public void onNext(DatabaseVersion databaseVersion) {
                Log.i("pref version", String.valueOf(PrefUtils.getDatabaseVersion()));
                Log.i("version_code", String.valueOf(databaseVersion.version_code));
                if (databaseVersion.version_code > PrefUtils.getDatabaseVersion()){
                    isUpdatingDb = true;
                    showUpdateDbDialog();
                    mDatabaseVersionCode = databaseVersion.version_code;
                }

            }
        };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetworkClient.getInstance().getDatabaseVersion(subscriber);
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yp_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_search) {
            int duration = 200;
            updateViewVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInRight)
                    .duration(duration).playOn(findViewById(R.id.floating_search_view));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchView.setSearchFocused(true);
                }
            }, duration);

            Logger.d("clicked the toolbar");
        } else if (i == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhoneUtils.ringUp(mContext, callPhoneNum);
                } else {
                    ToastUtils.show(this, "请在权限管理中开启微北洋拨打电话权限");
                }
                break;
            case REQUEST_CODE_WRITE_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    PhoneUtils.insertContact(mContext, mWritePhoneName, mWritePhoneNum);
                }else{
                    ToastUtils.show(this, "请在权限管理中开启微北洋添加联系人权限");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSearchView.getVisibility() == View.GONE) {
                finish();
            } else {
                updateViewVisibility(View.GONE);
            }
        }
        return false;
    }


}
