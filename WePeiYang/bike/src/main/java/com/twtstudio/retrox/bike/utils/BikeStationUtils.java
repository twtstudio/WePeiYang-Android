package com.twtstudio.retrox.bike.utils;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twt.wepeiyang.commons.experimental.CommonContext;
import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.model.StationsBrief;
import com.twtstudio.retrox.bike.model.StationsDetail;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jcy on 2016/8/13.
 */

public class BikeStationUtils {
    private List<MarkerOptions> mMarkerOptionsList = new ArrayList<>();
    private Map<String, StationsDetail> mIdMap = new HashMap<>();
    private List<StationsDetail> mDetailList = new ArrayList<>();


    private BikeStationUtils() {
        InputStream is = CommonContext.INSTANCE.getApplication().getResources().openRawResource(R.raw.detail);
        String detailJsonString = null;
        try {
            detailJsonString = IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<StationsDetail>>() {
        }.getType();
        mDetailList = gson.fromJson(detailJsonString, type);
        for (StationsDetail detail : mDetailList) {
            mIdMap.put(String.valueOf(detail.id), detail);
        }
    }

    public static BikeStationUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public StationsDetail queryId(String id) {
        StationsDetail detail = mIdMap.get(id);
        if (detail != null) {
            return detail;
        } else {
            return new StationsDetail();
        }
    }

    public StationsDetail queryId(int id) {
        StationsDetail detail = mIdMap.get(String.valueOf(id));
        if (detail != null) {
            return detail;
        } else {
            return new StationsDetail();
        }
    }

    public List<MarkerOptions> getStationsDetail() {
        if (!mMarkerOptionsList.isEmpty()) {
            return mMarkerOptionsList;
        }

        for (StationsDetail detail : mDetailList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(detail.lat_c, detail.lng_c))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_unselected))
                    .snippet(String.valueOf(detail.id))
                    .title(detail.name);
            mMarkerOptionsList.add(markerOptions);
        }
        return mMarkerOptionsList;
    }

    public List<MarkerOptions> getStationsBrief() {
        InputStream is = CommonContext.INSTANCE.getApplication().getResources().openRawResource(R.raw.brief);
        String briefJsonString = null;
        try {
            briefJsonString = IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        List<StationsBrief> briefList = new ArrayList<>();
        Type type = new TypeToken<List<StationsBrief>>() {
        }.getType();
        briefList = gson.fromJson(briefJsonString, type);
        List<MarkerOptions> markerOptionsList = new ArrayList<>();
        for (StationsBrief brief : briefList) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(brief.lat_c, brief.lng_c))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_selected))
                    .title(brief.name);
            markerOptionsList.add(markerOptions);
        }
        return markerOptionsList;
    }

    private static class SingletonHolder {
        private static final BikeStationUtils INSTANCE = new BikeStationUtils();
    }
}
