package com.twtstudio.retrox.bike.homeitem;

import android.arch.lifecycle.MutableLiveData;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.tapadoo.alerter.Alerter;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.api.AuthHelper;
import com.twtstudio.retrox.bike.api.BikeApiClient;
import com.twtstudio.retrox.bike.api.BikeResponseTransformer;
import com.twtstudio.retrox.bike.utils.BikeStationUtils;
import com.twtstudio.retrox.bike.utils.TimeStampUtils;

import java.net.SocketTimeoutException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/24.
 */

public class BikeHomeItemViewModel implements ViewModel {

    //绑定生命周期和Alerter会用到
    private RxAppCompatActivity activity;

    public final MutableLiveData<String> moneyLeft = new MutableLiveData<>();

    public final MutableLiveData<String> lastLeavePostion = new MutableLiveData<>();

    public final MutableLiveData<String> lastLeaveTime = new MutableLiveData<>();

    public final MutableLiveData<String> lastArriveTime = new MutableLiveData<>();

    public final MutableLiveData<String> lastArrivePostion = new MutableLiveData<>();

    public final MutableLiveData<String> costMoney = new MutableLiveData<>();

    public final MutableLiveData<Boolean> isProgressing = new MutableLiveData<>();

    public final ReplyCommand refreshClick = new ReplyCommand(this::getData);

    public final ReplyCommand callBikeCenter = new ReplyCommand(this::callBike);

    public BikeHomeItemViewModel(RxAppCompatActivity activity) {
        this.activity = activity;
        isProgressing.setValue(false);
        getData();
    }

    public void getData() {
        isProgressing.setValue(true);
        BikeApiClient.getInstance().getService().getUserInfo("fake")
                .retryWhen(new AuthHelper(BikeApiClient.getInstance().getService())) //token逻辑
                .map(new BikeResponseTransformer<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.bindToLifecycle())
                .subscribe(bikeUserInfo -> {
                    isProgressing.setValue(false);
                    moneyLeft.setValue("余额: "+bikeUserInfo.balance+"元");
                    if (bikeUserInfo.status == 0) {
                        moneyLeft.setValue("尚未绑定自行车，请在自行车模块绑定或在设置中关闭");
                    }
                    if (bikeUserInfo.record != null) {
                        String dep = BikeStationUtils.getInstance().queryId(bikeUserInfo.record.dep).name;
                        if (dep.equals("no data")){
                            dep = "点位无法查询";
                        }
                        lastLeavePostion.setValue(dep + "-" + bikeUserInfo.record.dep_dev + "号桩 取出");
                        lastLeaveTime.setValue(TimeStampUtils.getDateString(bikeUserInfo.record.dep_time));
                        String arr = BikeStationUtils.getInstance().queryId(bikeUserInfo.record.arr).name;
                        if (arr.equals("no data")) {
                            if (null == bikeUserInfo.record.arr || bikeUserInfo.record.arr_time.equals("")) {
                                //没还车
                                lastArrivePostion.setValue("尚未归还TAT");
                                lastArriveTime.setValue("点击下面按钮拨打自行车服务商电话");
                                Alerter.create(activity)
                                        .setBackgroundColor(R.color.bike_warning_color)
                                        .setTitle("自行车尚未归还TAT")
                                        .setText("点击自行车条目的下面按钮拨打自行车服务商电话")
                                        .show();
                            } else {
                                lastArrivePostion.setValue("点位名无法查询");
                                lastArriveTime.setValue(TimeStampUtils.getDateString(bikeUserInfo.record.arr_time));
                                costMoney.setValue("本次消费:" + bikeUserInfo.record.fee);
                            }
                        } else {
                            lastArrivePostion.setValue(arr + "-" + bikeUserInfo.record.arr_dev + "号桩 还入");
                            lastArriveTime.setValue(TimeStampUtils.getDateString(bikeUserInfo.record.arr_time));
                            costMoney.setValue("本次消费:" + bikeUserInfo.record.fee);
                        }
                    }
                }, throwable -> {
                    if (throwable instanceof SocketTimeoutException){
                        Toast.makeText(activity, "网络超时，请重试", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                    throwable.printStackTrace();
                });
    }

    public void callBike(){

        String[] strings = new String[]{"13114951501", "18020061573"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("拨打自行车客服")
                .setIcon(R.drawable.bike_bike_icon)
                .setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("tel:"+strings[which]);
                        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                        activity.startActivity(intent);
                    }
                });
        builder.create().show();
    }

}
