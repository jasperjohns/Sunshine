package com.example.asaldanha.sunshine.app;

/**
 * Created by asaldanha on 6/15/2015.
 */

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.asaldanha.sunshine.app.sync.SunshineSyncAdapter;

/**
 * A {@link PreferenceActivity} that presents a set of application settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static String LOG_TAG = SettingsActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        // TODO: Add preferences from XML
        addPreferencesFromResource(R.xml.pref_general);
        // Bind the prefernce back
//        bindPreferenceSummaryToValue(findPreference("pref_location_key"));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_temperature_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_notification_key)));


    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);


        if (preference instanceof CheckBoxPreference)
        {
            // Trigger the listener immediately with the preference's
            // current value.
            onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getBoolean(preference.getKey(),true));
        }
        else {
            // Trigger the listener immediately with the preference's
            // current value.
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));



        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        Log.v(LOG_TAG, "onPreferenceChange");

//        In onPreferenceChange in the SettingsActivity, call syncImmediately() instead of creating and executing a FetchWeatherTask.

        SunshineSyncAdapter.syncImmediately(this);

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference checkBoxPref = (CheckBoxPreference) preference;
            if (checkBoxPref.isChecked()){
                preference.setSummary( getString(R.string.pref_notification_summary_notify_no));
            }
            else {
                preference.setSummary( getString(R.string.pref_notification_summary_notify_yes));
            }

        }
        else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }



            return true;
    }


}