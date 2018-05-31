package com.example.korea.planner.util;

import java.util.HashMap;

/**
 * Created by korea on 2017-04-25.
 */

public class SolarLunarUtil {
    private volatile static SolarLunarUtil ourInstance;
    private static HashMap<String, String> solar_map = new HashMap<>();
    private static HashMap<String, String> lunar_map = new HashMap<>();
    private static String[] solarArr = new String[]{"0101", "0301", "0505", "0606", "0815", "1003", "1009", "1225"};
    private static String[] solarArrText = new String[]{"신정", "3.1절", "어린이날", "현충일", "광복절", "개천절", "한글날", "크리스마스"};
    private static String[] lunarArr = new String[]{"0101", "0408", "0815"};
    private static String[] lunarArrText = new String[]{"구정", "석가탄신일", "추석"};

    public static SolarLunarUtil getInstance() {
        if (ourInstance == null) {
            synchronized (AlarmUtil.class) {
                if (ourInstance == null) {
                    ourInstance = new SolarLunarUtil();
                }
            }
        }
        return ourInstance;
    }

    public void setSolarLunar() {
        for (int i = 0; i < solarArrText.length; i++) {
            solar_map.put(solarArr[i], solarArrText[i]);
        }
        for (int i = 0; i < lunarArr.length; i++) {
            lunar_map.put(lunarArr[i], lunarArrText[i]);
        }
    }

    public void setSolar(String key, String text) {
        solar_map.put(key, text);
    }

    public void delSolar(String key) {
        if (solar_map.get(key) != null) {
            solar_map.remove(key);
        }
    }

    public String getSolar(String key) {
        return solar_map.get(key);
    }

    public void setLunar(String key, String text) {
        lunar_map.put(key, text);
    }

    public void delLunar(String key) {
        if (lunar_map.get(key) != null) {
            lunar_map.remove(key);
        }
    }

    public String getLundar(String key) {
        return lunar_map.get(key);
    }

    public String changeKey(int month, int day) {
        String key;
        if (month < 9) {
            key = "0" + Integer.toString(month + 1);
        } else {
            key = Integer.toString(month + 1);
        }
        if (day < 10) {
            key = key + "0" + Integer.toString(day);
        } else {
            key = key + Integer.toString(day);
        }
        return key;
    }
}
