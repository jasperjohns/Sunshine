package com.example.asaldanha.sunshine.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asaldanha on 6/11/2015.
 */
public class WeatherDataParser {

    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */

    private static String LOG_TAG = WeatherDataParser.class.getSimpleName();


    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {
        // TODO: add parsing code here
        if (weatherJsonStr !=null){
            JSONObject jsonObj = new JSONObject(weatherJsonStr);
            JSONArray menuitemArr = jsonObj.getJSONArray("list");
            if (menuitemArr !=null) {
                if (dayIndex < menuitemArr.length()){
                    JSONObject mainobj =menuitemArr .getJSONObject(dayIndex);
                    JSONObject tempObj = mainobj.getJSONObject("temp");
//                    double temp = Double.parseDouble(tempObj.getDouble("max") .toString());
                    Log.v(LOG_TAG, tempObj.getString("max").toString()) ;
                }
            }

        }


        return -1;
    }
}



