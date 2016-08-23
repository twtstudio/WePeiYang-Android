package com.twt.service.bike.bike;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.twt.service.R;
import com.twt.service.bike.common.ui.PFragment;
import com.twt.service.bike.model.BikeStation;
import com.twt.service.bike.utils.BikeStationUtils;


import java.util.ArrayList;

import butterknife.InjectView;

/**
 * Created by jcy on 2016/8/7.
 */

public class BikeFragment extends PFragment<BikeFragPresenter> implements BikeViewController, LocationSource, AMapLocationListener, AMap.OnMarkerClickListener, AMap.OnCameraChangeListener {
    @InjectView(R.id.amap_view)
    MapView mAmapView;
    @InjectView(R.id.bike_available)
    TextView mAvailableText;
    @InjectView(R.id.bike_empty)
    TextView mEmptyText;
    @InjectView(R.id.station_name)
    TextView mStationName;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.station_status)
    TextView mStatusText;


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
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // TODO: 2016/8/23 觉得高德地图很烦的话旧注释掉下一句
        mAmapView.onCreate(savedInstanceState);

        //test
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

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
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                if (mSelectedMarker != null) {
                    mSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_unselected));
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
                System.out.println(aMapLocation.getLatitude());
                mListener.onLocationChanged(aMapLocation);
                if (!isCameraChanged){
                    CameraUpdate update=CameraUpdateFactory.zoomTo(17.685846f);
                    mAmap.moveCamera(update);
                    isCameraChanged = true;
                }
            } else {
                Log.d("jcy", "定位失败" + aMapLocation.getErrorCode());
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
            mSelectedMarker = marker;
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_selected));
            // TODO: 2016/8/11 marker logic
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            mSlidingUpPanelLayout.setTouchEnabled(false);
            mStationName.setText(marker.getTitle());

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
        mAmapView.onResume();
        super.onDestroy();
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
        mAvailableText.setText("可用车辆" + stationDetail.used + " (含:不佳:" + String.valueOf(stationDetail.used_poor - stationDetail.used_bad) + " 损坏:" + stationDetail.used_bad + ")");
        mEmptyText.setText("可用空位" + stationDetail.free + " (含:不佳:" + String.valueOf(stationDetail.free_poor - stationDetail.free_bad) + " 损坏:" + stationDetail.free_bad + ")");
//        mAvailableText.setText();
        if (stationDetail.status.equals("0")) {
            mStatusText.setText("offline");
        } else {
            mStatusText.setText("online");
        }
    }
}