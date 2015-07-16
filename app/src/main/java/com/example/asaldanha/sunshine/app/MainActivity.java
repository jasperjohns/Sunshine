package com.example.asaldanha.sunshine.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity
        implements Preference.OnPreferenceChangeListener {

    TextView prefEditText;
    Context mContext;

    private static String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefEditText = (TextView)findViewById(R.id.pref_location);
        mContext = getBaseContext();

        loadPref();
//        mContext.getSharedPreferences()
//        bindPreferenceSummaryToValue(mContext.this.findPreference(getString(R.string.pref_location_key)));
//        YourActivity.this.findPreference("pref_key");

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
        else if (id == R.id.action_map_location){
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String my_edittext_preference = mySharedPreferences.getString(getString(R.string.pref_location_key), "");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri geoLocation = Uri.parse ("get:0").buildUpon().appendQueryParameter("q", my_edittext_preference).build();

//            intent.setData(Uri.parse("geo:47.6,-122.3"));
            intent.setData(geoLocation);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }


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


    private void loadPref(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String my_edittext_preference = mySharedPreferences.getString(getString(R.string.pref_location_key), "");
        prefEditText.setText(my_edittext_preference);

//        Log.v(LOG_TAG, getString(R.string.pref_location_key));
       Log.v(LOG_TAG + "GG:", my_edittext_preference);


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



}
