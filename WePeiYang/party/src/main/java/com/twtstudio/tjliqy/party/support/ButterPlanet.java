package com.twtstudio.tjliqy.party.support;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by tjliqy on 2017/3/23.
 */

public class ButterPlanet {

    //id取得属性名字
    private static HashMap<Integer, String> r1Map;
    //属性名字回取id
    private static HashMap<String, Integer> r2Map;

    Object r = null;

    private static class SingletonHolder {
        private static final ButterPlanet INSTANCE = new ButterPlanet();
    }

    private ButterPlanet(){
        r1Map = new HashMap<>();
        r2Map = new HashMap<>();
        r = new R.id();
        //            System.out.println(aClass.getSimpleName());
        Field[] fields = R.id.class.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                int x = (int) field.get(r);
//                        System.out.println(field.getName() + " ----> " + x);
                r1Map.put(x, field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            r = R2.id.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Field[] fields2 = R2.id.class.getFields();
        for (Field field : fields2) {
            field.setAccessible(true);
            try {
                int x = (int) field.get(r);
//                        System.out.println(field.getName() + " ----> " + x);
                r2Map.put(field.getName(),x);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
    public static final ButterPlanet getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * R1 id cast to R2
     *
     * @param rid
     * @return
     */
    public static int cast(int rid) {
        String name = r1Map.get(rid);
        int id2 = r2Map.get(name);
        return id2;
    }

    /**
     * 初始化r1Map
     */
//    @SuppressWarnings("Duplicates")
//    private void initMap1() {
//
//        long time = System.currentTimeMillis();
//
//        Class[] classes = R.get();
//        Object r = null;
//        try {
//            r = R.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        for (Class aClass : classes) {
////            System.out.println(aClass.getSimpleName());
//            if (aClass.getSimpleName().equals("id")) {
//                Field[] fields = aClass.getFields();
//                for (Field field : fields) {
//                    field.setAccessible(true);
//                    try {
//                        int x = (int) field.get(r);
////                        System.out.println(field.getName() + " ----> " + x);
//                        r1Map.put(x, field.getName());
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        long time2 = System.currentTimeMillis();
//        long timeCost = time2 - time;
//
//        System.out.println("\nTimecost:" + timeCost + "ms");
//
//    }

    /**
     * 初始化r2Map
     */
//    @SuppressWarnings("Duplicates")
//    private void initMap2() {
//
//        long time = System.currentTimeMillis();
//
//        Class[] classes = R2.getClasses();
//        Object r = null;
//        try {
//            r = R2.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        for (Class aClass : classes) {
//            if (aClass.getSimpleName().equals("id")) {
//                Field[] fields = aClass.getFields();
//                for (Field field : fields) {
//                    field.setAccessible(true);
//                    try {
//                        int x = (int) field.get(r);
////                        System.out.println(field.getName() + " ----> " + x);
////                        r2Map.put(x, field.getName());
//                        r2Map.put(field.getName(), x);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }
//        long time2 = System.currentTimeMillis();
//        long timeCost = time2 - time;
//
//        System.out.println("\nTimecost:" + timeCost + "ms");
//
//    }
}
