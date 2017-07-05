package com.twtstudio.retrox.bike.bike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.PFragment;
import com.twtstudio.retrox.bike.model.BikeStation;
import com.twtstudio.retrox.bike.utils.BikeStationUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeFragment extends PFragment<BikeFragPresenter> implements BikeViewController, LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener {
//    @BindView(R2.id.amap_view)
    private MapView mAmapView;

    @BindView(R2.id.bike_location)
    TextView bikeLocation;
    @BindView(R2.id.bike_available_num)
    TextView bikeAvailableNum;
    @BindView(R2.id.bike_available_weak)
    TextView bikeAvailableWeak;
    @BindView(R2.id.bike_free_available)
    TextView bikeFreeAvailable;
    @BindView(R2.id.bike_free_available_weak)
    TextView bikeFreeAvailableWeak;

    private Unbinder mUnbinder;

    private AMap mAmap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationSource.OnLocationChangedListener mListener;

    private String TAG = "Bike";
    private ArrayList<MarkerOptions> mDetailMarkerOptions;
    private ArrayList<MarkerOptions> mBriefMarkerOptions;

    private Marker mSelectedMarker;

    private static final float CHANGEPOINT = 18.23416f;
    private boolean isBrief = true;

    private boolean isCameraChanged = false;

    private BottomSheetBehavior sheetBehavior;

    @Override
    protected BikeFragPresenter getPresenter() {
        return new BikeFragPresenter(getContext(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_bike;
    }

    @Override
    protected void initView() {
        mDetailMarkerOptions = (ArrayList<MarkerOptions>) BikeStationUtils.getInstance().getStationsDetail();
        mPresenter.cacheStationStatus();
        //mBriefMarkerOptions = (ArrayList<MarkerOptions>) mPresenter.getStationsBrief();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // TODO: 2016/8/23 觉得高德地图很烦的话旧注释掉下一句
        View view = inflater.inflate(R.layout.fragment_bike,container,false);
        mAmapView = (MapView) view.findViewById(R.id.amap_view);
        mAmapView.onCreate(savedInstanceState);

        //test
        //mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        mUnbinder = ButterKnife.bind(this,view);

        View sharedView = view.findViewById(R.id.share_view);
        sheetBehavior = BottomSheetBehavior.from(sharedView);
        sheetBehavior.setHideable(true);
        //初始化bottomsheet的状态（隐藏）
        if (sheetBehavior.getState()!=BottomSheetBehavior.STATE_HIDDEN){
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }


        mAmap = mAmapView.getMap();
        mAmap.addMarkers(mDetailMarkerOptions,!isCameraChanged);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation_icon));
        myLocationStyle.radiusFillColor(R.color.bike_circle_fill_color);
        mAmap.setMyLocationStyle(myLocationStyle);
        mAmap.setOnMarkerClickListener(this);
        mAmap.setLocationSource(this);
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);
        mAmap.setMyLocationEnabled(true);
        mAmap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                if (mSelectedMarker != null) {
                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_unselected));
                }

                if (sheetBehavior.getState()!=BottomSheetBehavior.STATE_HIDDEN){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }


            }
        });

//        mAmap.addMarkers()
//        Marker marker1=mAmap.addMarker(new MarkerOptions().position(new LatLng(38.997704,117.315942)).icon(BitmapDescriptorFactory.fromResource(R.drawable.qinwa)));
//        marker1.setTitle("A1");
        //mAmap.setOnCameraChangeListener(this);
        return view;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //System.out.println(aMapLocation.getLatitude());
                mListener.onLocationChanged(aMapLocation);
                if (!isCameraChanged){
                    CameraUpdate update=CameraUpdateFactory.zoomTo(17.685846f);
                    mAmap.moveCamera(update);
                    isCameraChanged = true;
                }
            } else {
                //Log.d("jcy", "定位失败" + aMapLocation.getErrorCode());
            }
        } else {
            Log.d("jcy", "初始化问题");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mSelectedMarker != null) {
            mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_unselected));
        }
        if (marker.getSnippet() != null) {

            if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            mSelectedMarker = marker;
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_selected));
            // TODO: 2016/8/11 marker logic
//            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            mSlidingUpPanelLayout.setTouchEnabled(false);
            bikeLocation.setText(marker.getTitle());

            mPresenter.queryCachedStatus(marker.getSnippet());
            mPresenter.getStationStatus(marker.getSnippet());
        }
        return true;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);

            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onResume() {
        mAmapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mAmapView.onDestroy();
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onPause() {
        mAmapView.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mAmapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d(TAG, "onCameraChange: " + cameraPosition.zoom);
        if (cameraPosition.zoom < CHANGEPOINT && isBrief == false) {
            mAmap.clear(true);
            mAmap.addMarkers(mBriefMarkerOptions, false);
            isBrief = true;
        } else if (cameraPosition.zoom > CHANGEPOINT && isBrief == true) {
            mAmap.clear(true);
            mAmap.addMarkers(mDetailMarkerOptions, false);
            isBrief = false;
        } else {
            //Log.d(TAG, "onCameraChange: "+cameraPosition.zoom+"------>"+isBrief);
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void setStationDetail(BikeStation stationDetail) {

        String location = bikeLocation.getText().toString();
        if (!location.contains(String.valueOf(stationDetail.used))){
            location = location+" "+stationDetail.used+"/"+stationDetail.free;
        }

        bikeLocation.setText(location);
        bikeAvailableNum.setText("可用车辆:" + stationDetail.used);
        bikeAvailableWeak.setText("含:不佳:" + String.valueOf(stationDetail.used_poor - stationDetail.used_bad) + " 损坏:" + stationDetail.used_bad + "");
        bikeFreeAvailable.setText("可用空位" + stationDetail.free);
        bikeFreeAvailableWeak.setText("含:不佳:" + String.valueOf(stationDetail.free_poor - stationDetail.free_bad) + " 损坏:" + stationDetail.free_bad + "");
//        mEmptyText.setText("可用空位" + stationDetail.free + " (含:不佳:" + String.valueOf(stationDetail.free_poor - stationDetail.free_bad) + " 损坏:" + stationDetail.free_bad + ")");
//        mAvailableText.setText();
//        if (stationDetail.status.equals("0")) {
//            mStatusText.setText("offline");
//        } else {
//            mStatusText.setText("online");
//        }
    }
}