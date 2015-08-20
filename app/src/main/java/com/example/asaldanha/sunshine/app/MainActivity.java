package com.example.asaldanha.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.asaldanha.sunshine.app.sync.SunshineSyncAdapter;


public class MainActivity extends ActionBarActivity
        implements Preference.OnPreferenceChangeListener, ForecastFragment.Callback {


    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private final String FORECASTFRAGMENT_TAG = "FFTAG";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
//    In MainActivity create a mLocation variable to store our current known location.
    private static  String mLocation;
    TextView prefEditText;
    Context mContext;
    private static boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Is this a two-pane/tablet layout or not
        if (findViewById(R.id.weather_detail_container) != null) {
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }

            ForecastFragment foreFrag = (ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            foreFrag.setUseTodayLayout(!mTwoPane);
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }



//        prefEditText = (TextView)findViewById(R.id.pref_location);
        mContext = getBaseContext();

        loadPref();
//        mContext.getSharedPreferences()
//        bindPreferenceSummaryToValue(mContext.this.findPreference(getString(R.string.pref_location_key)));
//        YourActivity.this.findPreference("pref_key");

        SunshineSyncAdapter.initializeSyncAdapter(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.forcastfragment , menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Start Settings Activity
        if (id == R.id.action_settings) {
            Intent SettingsAct = new Intent( this, SettingsActivity.class) ;
            startActivity(SettingsAct);

            return true;
        }
/*
        else if (id == R.id.action_map_location){
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mLocation = mySharedPreferences.getString(getString(R.string.pref_location_key), "");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri geoLocation = Uri.parse ("get:0").buildUpon().appendQueryParameter("q", mLocation).build();

//            intent.setData(Uri.parse("geo:47.6,-122.3"));
            intent.setData(geoLocation);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }
*/
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadPref();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        Log.v(LOG_TAG,"onPreferenceChange" );
        String stringValue = value.toString();
        loadPref();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation(this);
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
//            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_forecast);
            if ( null != ff ) {
                ff.onLocationChanged();
            }

            mLocation = location;
        }
    }

    private void loadPref(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mLocation = mySharedPreferences.getString(getString(R.string.pref_location_key), "");
//        prefEditText.setText(mLocation);

//        Log.v(LOG_TAG, getString(R.string.pref_location_key));
       Log.v(LOG_TAG + "GG:", mLocation);


    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, contentUri);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }



}
