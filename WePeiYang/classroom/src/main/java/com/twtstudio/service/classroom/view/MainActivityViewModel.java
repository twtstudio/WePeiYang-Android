package com.twtstudio.service.classroom.view;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.annimon.stream.Collector;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twt.wepeiyang.commons.utils.CommonPrefUtil;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.database.DBManager;
import com.twtstudio.service.classroom.database.RoomCollection;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.FreeRoom2;
import com.twtstudio.service.classroom.utils.StringHelper;
import com.twtstudio.service.classroom.utils.TimeHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewClassSelector;

/**
 * Created by zhangyulong on 7/11/17.
 */

public class MainActivityViewModel {
    private RxAppCompatActivity rxActivity;
    public final ObservableField<String> condition1 = new ObservableField<>();
    public final ObservableField<String> condition2 = new ObservableField<>("现在");
    public final ObservableField<String> condition3 = new ObservableField<>("筛选");
    public final ObservableField<Boolean> isError = new ObservableField<>(false);
    public final ObservableField<Boolean> isLoading = new ObservableField<>();
    public final ObservableField<Boolean> isEmpty = new ObservableField<>(false);
    public final ObservableField<Boolean> isHttpError = new ObservableField<>(false);//没有符合条件的教学楼时为true
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(ItemViewModel.class, BR.viewModel, R.layout.classroom_list_item)
            .put(OnLoadMoreItemViewModel.class, BR.viewModel, R.layout.activity_classroom_on_load_more_item)
            .build();
    public static int building, week, time;
    private int day;
    private static String filterCondition = " ";
    private List<String> filterConditions = new ArrayList<>();
    String token;
    private boolean onLoadMore = false;
    public boolean onLoadMoreError = false;
    private int limit = 0;//一栋楼内总的自习室数量
    public final ReplyCommand<Integer> loadMoreCommand = new ReplyCommand<>(
            (count) -> {
                if (onLoadMore) {
                    if (!onLoadMoreError) {
                        int page = count / limit + 1;
                        time = page + page - 1;
                        if (time > 12)
                            onLoadMore = false;
                        else {
                            getData(building, TimeHelper.getWeekInt(), time, CommonPrefUtil.getStudentNumber());
                            if (!items.isEmpty())
                                items.remove(items.size() - 1);
                            items.add(new OnLoadMoreItemViewModel(false, false));

                            if (items.size() > 1)
                                items.add(new OnLoadMoreItemViewModel(true, false));
                            else
                                isError.set(true);
                        }
                    } else {
                        onLoadMoreError = false;
                    }

                }
            });

    MainActivityViewModel(RxAppCompatActivity rxAppCompatActivity) {
        this.rxActivity = rxAppCompatActivity;
        this.filterCondition = " ";
        building = week = time = 0;
//        iniData(buiding,week,time,token);
    }


    public void iniData(int building, int week, int time, String token) {
        this.building = building;
        this.week = week;
        this.time = time;
        this.day = TimeHelper.getDayOfWeek();
        this.token = token;
        onLoadMore = false;
        isLoading.set(true);
        isHttpError.set(false);
        isError.set(false);
        isEmpty.set(false);
        if (!condition2.get().equals("全天"))
            items.clear();
        getData(building, week, time, token);
    }

    private void processData(FreeRoom2 freeRoom2) {
        List<FreeRoom2.FreeRoom> freeRooms = new ArrayList<>();
        List<FreeRoom2.FreeRoom> tmpFreeRooms = new ArrayList<>();
        if (!items.isEmpty())
            items.remove(items.size() - 1);
        if (freeRoom2 != null) {
            isError.set(false);
            isLoading.set(false);
            isEmpty.set(false);
            isHttpError.set(false);
            if (freeRoom2.getErrorcode() == 1) {
                isError.set(true);
                return;
            }
        }
        if (freeRoom2.getBuilding() != building)
            return;
        if (freeRoom2.getData() != null) {
            for (FreeRoom2.FreeRoom freeRoom : freeRoom2.getData())
                freeRooms.add(freeRoom);
            for (String filterCondition : filterConditions) {
                tmpFreeRooms.clear();
                for (FreeRoom2.FreeRoom freeRoom : freeRooms)
                    if (filterFreeRoom(freeRoom, filterCondition)) {
                        tmpFreeRooms.add(freeRoom);
                    }
                freeRooms.clear();
                freeRooms.addAll(tmpFreeRooms);
            }
            if (freeRooms.isEmpty()) {
                isEmpty.set(true);
            } else {
                for (FreeRoom2.FreeRoom freeRoom : freeRooms) {
                    freeRoom.setCollection(isCollection(freeRoom));
                    items.add(new ItemViewModel(rxActivity, freeRoom, this, freeRoom2.getTime()));
                }
                limit = freeRoom2.getData().size() + 6;
                //用不显示的条目填充items，防止因为item数量的原因误触发上拉加载
                for (int i = 0; i < (freeRoom2.getData().size() - freeRooms.size() + 6); i++)
                    items.add(new OnLoadMoreItemViewModel(false, false));
            }
        }
    }

//    public void setCollected(String building) {
//        ClassRoomProvider.init(rxActivity).collect(building, token);
//    }
//
//    public void cancelCollected(String building) {
//        ClassRoomProvider.init(rxActivity).cancelCollect(token, building);
//    }
//
//    public void getCollected() {
//        ClassRoomProvider.init(rxActivity).getAllCollectedClassroom(token, week);
//    }

    public void addFilterCondition(String filterCondition) {
        filterConditions.add(filterCondition);
    }

    public boolean isFilterConditionRepeated(String filterCondition) {
        //判断list中是否已有此项条件
        for (String condition : filterConditions)
            if (condition.equals(filterCondition))
                return true;
        return false;
    }

    public void removeFilterCondition(String filterCondition) {
        Iterator<String> sListIterator = filterConditions.iterator();
        while (sListIterator.hasNext()) {
            String e = sListIterator.next();
            if (e.equals(filterCondition)) {
                sListIterator.remove();
            }
        }
    }

    //将筛选条件重置为初始状态，即筛选条件为全部时的状态
    public void resetFilterCondition() {
        filterConditions.clear();
    }

    public void getAllDayRoom(int building, String filterCondition) {
        this.building = building;
        onLoadMore = false;
        isLoading.set(true);
        isHttpError.set(false);
        isError.set(false);
        isEmpty.set(false);
        if (building == 47) {
            //因为47楼只能获取一间自习室，所以无法使用上拉加载，暂时只能这样处理了
            onLoadMore = false;
            items.clear();
            for (int i = 1; i <= 12; i += 2) {
                getData(building, TimeHelper.getWeekInt(), i, CommonPrefUtil.getStudentNumber());
            }
        } else {
            onLoadMore = true;
            items.clear();
            this.filterCondition = filterCondition;
            time = 1;
            getData(building, TimeHelper.getWeekInt(), time, CommonPrefUtil.getStudentNumber());
        }
    }

    private boolean isCollection(FreeRoom2.FreeRoom freeRoom) {
        List<RoomCollection> roomCollections = DBManager.getInstance(rxActivity).queryRoomCollectionListByRoom(freeRoom.getRoom());
        if (roomCollections.isEmpty())
            return false;
        return true;
    }

    private boolean filterFreeRoom(FreeRoom2.FreeRoom freeRoom, String filterCondition) {
        if (filterCondition.equals(" "))
            return true;
        else if (filterCondition.equals("power_pack"))
            if (freeRoom.getPower_pack()) return true;
            else return false;
        else if (filterCondition.equals("water_dispenser"))
            if (freeRoom.getWater_dispenser()) return true;
            else return false;
        else if (filterCondition.equals("heating"))
            if (freeRoom.getHeating()) return true;
            else return false;
        else if (filterCondition.equals("empty"))
            if (freeRoom.getState().equals("空闲")) return true;
            else return false;
        return true;
    }

    public void onRefresh() {
        refreshData(building);
    }

    private void getData(int building, int week, int time, String token) {
        ClassRoomProvider.init(rxActivity)
                .registerAction(this::processData)
                .getFreeClassroom(building, week, TimeHelper.getDayOfWeek(), time, token, this);
    }

    public void refreshData(int building) {
        int week = TimeHelper.getWeekInt();
        String token = CommonPrefUtil.getStudentNumber();
        if (condition3.get().equals("全部"))
            resetFilterCondition();
        if (condition2.get().equals("全天"))
            getAllDayRoom(building, filterCondition);
        else if (condition2.get().equals("现在")) {
            int time = TimeHelper.getTimeInt();
            iniData(building, week, time, token);
        } else
            iniData(building, week, time, token);
    }
}
