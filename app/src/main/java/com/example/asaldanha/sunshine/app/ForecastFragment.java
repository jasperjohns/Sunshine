package com.example.asaldanha.sunshine.app;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asaldanha.sunshine.app.data.WeatherContract;

//import android.support.v4.app.Fragment;


/**
 * A placeholder fragment containing a simple view.
 */


public class ForecastFragment extends android.support.v4.app.Fragment implements Preference.OnPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor>  {

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();
    private final static int LOADER_ID = 0;


    //private ArrayAdapter<String> mForecastAdapter;
    private ForecastAdapter mForecastAdapter;
    public String a1 = "test";
    ListView listView;
    private String mTemperature = "metric";

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //getLoaderManager().initLoader(LOADER_ID, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        // Wrong place ... sheould go to OnStart
//        UpdateWeatherData();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);



    }


    //Does not get called ????
//    @Override
    public void OnStart() {
        super.onStart();
        Log.v(LOG_TAG, "OnStart");
//        UpdateWeatherData();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

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

/*      This initialization  is maybe needed for when FetchWeatherTask is withing the class - this
has to removed when FetchWeather was implemented as a class as it was re-initializing the adapter
*/
/*
        ArrayList<String> WeekForecast = new ArrayList<String>();

        WeekForecast.add("Today Sunny 88/63");
        WeekForecast.add("Tomorrow Cloudy 90/70");
        WeekForecast.add("Wednesday Partially Cloudy 90/72");
        WeekForecast.add("Thursday Cloudy 92/72");
        WeekForecast.add("Friday Sunny 92/72");
        WeekForecast.add("Saturday Sunny 92/72");

        mForecastAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,R.id.list_item_forecast_textview, WeekForecast);
 */

/*
        //Use the Utilities to get the preferred location
        String prefLocation = Utility.getPreferredLocation(getActivity());

        //Sort order column date Asc
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

        //Build URI for Content provider: Weather with a Start Date
        Uri weatherURI = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(prefLocation, System.currentTimeMillis());

        //Query using the content provider to get the  cursor
        //URI, projection, selection, selection args, sort order
        Cursor cur = getActivity().getContentResolver().query(weatherURI, null, null, null, sortOrder);

        // use the cursor with the Adpater
        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);
*/

        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) v.findViewById(R.id.listView_forecast);
        listView.setAdapter(mForecastAdapter);

//        UpdateWeatherData();

/*
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
*/


        return v;

//        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        Log.v(LOG_TAG, "onPreferenceChange");
        String stringValue = value.toString();
        return true;
    }

   // PUBLIC METHODS
    public void UpdateWeatherData() {

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String my_edittext_preference = mySharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String my_temperature_preference = mySharedPreferences.getString(getString(R.string.pref_temperature_key), getString(R.string.pref_temperature_default));

/*
        if (mForecastAdapter == null) {
            ArrayList<String> WeekForecast = new ArrayList<String>();
            mForecastAdapter =
                    new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, WeekForecast);
        }
*/

//        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity().getBaseContext());

//        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        weatherTask.execute(my_edittext_preference, my_temperature_preference);

    }

    //LOADER METHODS
    @Override
    public Loader<Cursor> onCreateLoader(int loader, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.



        //Uri, projection, selection, selectionArgs, sort order


        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.

        //Use the Utilities to get the preferred location
        String prefLocation = Utility.getPreferredLocation(getActivity());

        //Sort order column date Asc
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

        //Build URI for Content provider: Weather with a Start Date
        Uri weatherURI = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(prefLocation, System.currentTimeMillis());



        //Query using the content provider to get the  cursor
        //URI, projection, selection, selection args, sort order
//        Cursor cur = getActivity().getContentResolver().query(weatherURI, null, null, null, sortOrder);

        return new CursorLoader(getActivity(), weatherURI,
                null, null, null,
                sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mForecastAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mForecastAdapter.swapCursor(null);
    }





}









