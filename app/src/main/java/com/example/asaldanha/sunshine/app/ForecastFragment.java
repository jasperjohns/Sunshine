package com.example.asaldanha.sunshine.app;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */


public class ForecastFragment extends Fragment implements Preference.OnPreferenceChangeListener{

    private ArrayAdapter<String> mForecastAdapter;
    public String a1 = "test";
    ListView listView;
    private final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private String mTemperature ="metric";

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Wrong place ... sheould go to OnStart
        UpdateWeatherData();
    }

    //Does not get called ????
//    @Override
    public void OnStart(){
        super.onStart();
        Log.v(LOG_TAG, "OnStart");
        UpdateWeatherData();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu) ;
    }

    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh){

//            AsyncTask<String, Integer, String[]> weatherinfo = new FetchWeatherTask().execute("94043");
//            weatherinfo.

//            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

//            String my_edittext_preference = mySharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
//            new FetchWeatherTask().execute(my_edittext_preference);
                UpdateWeatherData();

//            mForecastAdapter =  new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,R.id.list_item_forecast_textview, WeekForecast);
//            listView.setAdapter(mForecastAdapter);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ArrayList<String> WeekForecast = new ArrayList<String>();
/*
        WeekForecast.add("Today Sunny 88/63");
        WeekForecast.add("Tomorrow Cloudy 90/70");
        WeekForecast.add("Wednesday Partially Cloudy 90/72");
        WeekForecast.add("Thursday Cloudy 92/72");
        WeekForecast.add("Friday Sunny 92/72");
        WeekForecast.add("Saturday Sunny 92/72");
*/
        mForecastAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,R.id.list_item_forecast_textview, WeekForecast);

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) v.findViewById(R.id.listView_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
//                Log.v(LOG_TAG + "CCC", listItem.toString());
                Intent detailAct = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, listItem.toString()) ;
//                Toast.makeText(getActivity(), "selected Item Name is " + listItem.toString(), Toast.LENGTH_LONG).show();
                startActivity(detailAct);

            }

        });



        return v;

//        return inflater.inflate(R.layout.fragment_main, container, false);

    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        Log.v(LOG_TAG, "onPreferenceChange");
        String stringValue = value.toString();


        return true;
    }


    public void UpdateWeatherData() {

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String my_edittext_preference = mySharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String my_temperature_preference = mySharedPreferences.getString(getString(R.string.pref_temperature_key), getString(R.string.pref_temperature_default));
        new FetchWeatherTask().execute(my_edittext_preference, my_temperature_preference);

    }



    private class FetchWeatherTask extends AsyncTask<String, Integer, String []> {

//    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + zipCode +"&mode=json&units=metric&cnt=7");


        private final String BASE_URL = "api.openweathermap.org";
        private final String DATA ="data";
        private final String VERSION ="2.5";
        private final String FORECAST = "forecast";
        private final String DAILY = "daily";


        private final String ZIPCODE = "q";
        private final String MODE = "mode";
        private final String UNITS = "units";
        private final String CNT = "cnt";

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            String zipCode = params[0];
            String temperature = null;

            if (params.length >= 2 ){
                temperature = params[1];
                mTemperature = temperature;
            }
//            Log.v(LOG_TAG,  String.valueOf(params.length));
//            Log.v(LOG_TAG,  temperature);

            String [] weatherinfo = new String[6];

            String units ="metric";
            String mode="json";
            String cnt ="7";



            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast


                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority(BASE_URL)
                        .appendPath(DATA)
                        .appendPath(VERSION)
                        .appendPath(FORECAST)
                        .appendPath(DAILY)
                        .appendQueryParameter(ZIPCODE, zipCode)
                        .appendQueryParameter(MODE, mode )
                        .appendQueryParameter(UNITS, units )
                        .appendQueryParameter(CNT, cnt);
                String myUrl = builder.build().toString();
                Log.e(LOG_TAG, myUrl);

//            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + zipCode +"&mode=json&units=metric&cnt=7");
                URL url = new URL(myUrl);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
//            Log.v("sunshine1", url.toString());
                urlConnection.connect();
//            Log.v("sunshine2", url.toString());


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
// But it does make debugging a *lot* easier if you print out the completed
// buffer for debugging.
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                //Log.v(LOG_TAG, forecastJsonStr);

                try{
//                WeatherDataParser.getMaxTemperatureForDay(forecastJsonStr, 2);
                    weatherinfo =getWeatherDataFromJson(forecastJsonStr, 7) ;
                }
                catch (JSONException e){
                    Log.e(LOG_TAG, "Error ", e);
                    forecastJsonStr = null;
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
//        return forecastJsonStr ;
          //  Log.e(LOG_TAG, weatherinfo.toString());

            return weatherinfo ;
        }


        @Override
        protected void onPostExecute(String[] strings) {

            super.onPostExecute(strings);

            if (strings != null) {
                mForecastAdapter.clear();
                for(String dayForecastStr : strings) {
                    mForecastAdapter.add(dayForecastStr);
                }
                // New data is back from the server.  Hooray!
            }
        }



        /* The date/time conversion code is going to be moved outside the asynctask later,
             * so for convenience we're breaking it out into its own method now.
             */
        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
//            Log.v(LOG_TAG, "xx" + mTemperature + "yy");

            if (mTemperature.contains("imperial") ){
//                Log.v(LOG_TAG, mTemperature);
                high = high * 9/5 + 32;
                low = low * 9/5 + 32;
            }

            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);


            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            String[] resultStrs = new String[numDays];
            for(int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);

                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

//            for (String s : resultStrs) {
//                Log.v(LOG_TAG, "Forecast entry: " + s);
//            }
            return resultStrs;
        }
    }
}
