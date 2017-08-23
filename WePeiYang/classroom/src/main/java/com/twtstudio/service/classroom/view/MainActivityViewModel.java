package com.twtstudio.service.classroom.view;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.annimon.stream.Collector;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.kelin.mvvmlight.base.ViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.service.classroom.BR;
import com.twtstudio.service.classroom.R;
import com.twtstudio.service.classroom.database.DBManager;
import com.twtstudio.service.classroom.database.RoomCollection;
import com.twtstudio.service.classroom.model.ClassRoomProvider;
import com.twtstudio.service.classroom.model.FreeRoom2;
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
    public final ObservableField<Boolean> isLoading = new ObservableField<>(true);
    public final ObservableArrayList<ViewModel> items = new ObservableArrayList<>();
    public final ItemViewSelector itemView = ItemViewClassSelector.builder()
            .put(ItemViewModel.class, BR.viewModel, R.layout.list_item)
            .build();
    public static int building, week, time;
    private int day;
    private static String filterCondition = " ";
    private List<String> filterConditions = new ArrayList<>();
    String token;
    boolean update;

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
        filterConditions.clear();
        filterConditions.add(filterCondition);
        this.token = token;
        items.clear();
        ClassRoomProvider.init(rxActivity)
                .registerAction(this::processData)
                .getFreeClassroom(building, week, day, time, token);
    }

    public void iniData(int building, int week, int time, String token, String filterCondition) {
        this.building = building;
        this.week = week;
        this.time = time;
        this.day = TimeHelper.getDayOfWeek();
        this.token = token;
        items.clear();
        ClassRoomProvider.init(rxActivity)
                .registerAction(this::processData)
                .getFreeClassroom(building, week, day, time, token);
    }

    private void processData(FreeRoom2 freeRoom2) {
        List<FreeRoom2.FreeRoom> freeRooms = new ArrayList<>();
        List<FreeRoom2.FreeRoom> tmpFreeRooms = new ArrayList<>();
        isError.set(false);
        isLoading.set(false);
        if (freeRoom2 != null)
            if (freeRoom2.getErrorcode() == 1)
                isError.set(true);
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
            for (FreeRoom2.FreeRoom freeRoom : freeRooms) {
                freeRoom.setCollection(isCollection(freeRoom));
                items.add(new ItemViewModel(rxActivity, freeRoom, this, freeRoom2.getTime()));
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
                return false;
        return true;
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

    //获取全天教室情况
    public void getAllDayRoom(int building, String filterCondition) {
        List<ItemViewModel> itemViewModels = new ArrayList<>();
        isError.set(false);
        for (int i = 1; i <= 12; i += 2)
            ClassRoomProvider.init(rxActivity)
                    .registerAction(freeRoom2 -> {
                        if (freeRoom2 != null)
                            if (freeRoom2.getErrorcode() == 1)
                                isError.set(true);
                        if (freeRoom2.getData() != null)
                            for (FreeRoom2.FreeRoom freeRoom : freeRoom2.getData())
                                if (filterFreeRoom(freeRoom, filterCondition)) {
                                    freeRoom.setCollection(isCollection(freeRoom));
                                    itemViewModels.add(new ItemViewModel(rxActivity, freeRoom, this, freeRoom2.getTime()));
                                }
                        items.clear();
                        items.addAll(Stream.of(itemViewModels)
                                .sorted((p1, p2) -> p1.freeRoomTime.compareTo(p2.freeRoomTime))
                                .collect(Collectors.toList()));
                        isLoading.set(false);
                    })
                    .getFreeClassroom(building, week, day, i, token);
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
    public void onRefresh(){
        isLoading.set(true);
        iniData(building,week,time,token);
    }
}
