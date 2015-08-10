package com.example.asaldanha.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asaldanha.sunshine.app.data.WeatherContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends  android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private final String SHARE_HASHTAG = " #SunShine";
    static final String DETAIL_URI = "URI";
    private Uri mUri;

    private String mForecastStr = null;
    private  String mWeatherURI = null;
    private final static int LOADER_ID = 0;
    private ShareActionProvider mShareActionProvider;


    private   static final String[] DETAILS_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE

    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;
    static final int COL_HUMIDITY = 9;
    static final int COL_WIND_SPEED = 10;
    static final int COL_DEGREE = 11;
    static final int COL_PRESSURE = 12;


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        View rootView = inflater.inflate(R.layout.frame_detail_ref, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
             mUri= arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }



        if (bundle != null && bundle.containsKey(Intent.EXTRA_TEXT)) {

            if(bundle.getString(Intent.EXTRA_TEXT)!= null)
            {
//            Log.v(LOG_TAG, bundle.getString(Intent.EXTRA_TEXT).toString());

                //TODO here get the string stored in the string variable and do
//                TextView textDetail = (TextView) rootView.findViewById(R.id.textDetail);
//            textDetail.setText(bundle.getString(Intent.EXTRA_TEXT).toString());
//            textDetail.setText("asdasdasd");
/*
                if (textDetail != null) {
                    mForecastStr = bundle.getString(Intent.EXTRA_TEXT).toString();
                    textDetail.setText(mForecastStr);
                }
*/
            }
        }

        if ( intent != null) {
            mForecastStr = intent.getDataString();
            mWeatherURI= intent.getDataString();
        }
/*

        if (null != mForecastStr) {
            ((TextView) rootView.findViewById(R.id.textDetail))
                    .setText(mForecastStr);
        }
*/

       return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        //ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

//        ShareActionProvider shareActionProvider = new ShareActionProvider(getActivity());
        mShareActionProvider = new ShareActionProvider(getActivity());
        if (mShareActionProvider != null) {
            if (mForecastStr !=null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
            //MenuItemCompat.setActionProvider(item, shareActionProvider);
            Log.d(LOG_TAG, "Share Action Provider is NOT null");
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
//        if (shareActionProvider != null) {
//            shareActionProvider.setShareIntent(createShareForecastIntent());
//        } else {
//            Log.d(LOG_TAG, "Share Action Provider is null?");
//        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + SHARE_HASHTAG);
        return shareIntent;
    }


    //LOADER METHODS
    @Override
    public Loader<Cursor> onCreateLoader(int loader, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.

        if (mUri== null) return null;

/*
        if (mWeatherURI== null) return null;

        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }

*/

        //Uri, projection, selection, selectionArgs, sort order
        //Build URI for Content provider: Weather with a Start Date
        Uri weatherURI =  mUri;

        //Query using the content provider to get the  cursor
        //URI, projection, selection, selection args, sort order
//        Cursor cur = getActivity().getContentResolver().query(weatherURI, null, null, null, sortOrder);

        return new CursorLoader(getActivity(), weatherURI,
                DETAILS_COLUMNS, null, null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        //mForecastAdapter.swapCursor(cursor);
        if (!cursor.moveToFirst()) return;

        Context context = getActivity().getBaseContext();


        String dateString = Utility.getFriendlyDayString(context, cursor.getLong(COL_WEATHER_DATE));

        String weatherDescription = cursor.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String tempMax = Utility.formatTemp(context, cursor.getLong(COL_WEATHER_MAX_TEMP));
        String tempMin = Utility.formatTemp(context, cursor.getLong(COL_WEATHER_MIN_TEMP));

        mForecastStr = String.format("%s-%s-%s/%s", dateString, weatherDescription, tempMin, tempMax);

//        TextView textView = (TextView) getActivity().findViewById(R.id.textDetail);
//        textView.setText(mForecastStr);


        ImageView iconView = (ImageView) getActivity().findViewById(R.id.detail_icon);
        iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));



        TextView dateView = (TextView) getActivity().findViewById(R.id.detail_date_textview);
        dateView.setText(dateString);

        TextView descriptionView = (TextView) getActivity().findViewById(R.id.detail_forecast_textview);
        descriptionView.setText(weatherDescription);

        TextView highTempView = (TextView) getActivity().findViewById(R.id.detail_high_textview);
        highTempView.setText(tempMax);

        TextView lowTempView = (TextView) getActivity().findViewById(R.id.detail_low_textview);
        lowTempView.setText(tempMin);

        TextView humidityView = (TextView) getActivity().findViewById(R.id.detail_humidity_textview);
//        humidityView.setText();
        TextView windView = (TextView) getActivity().findViewById(R.id.detail_wind_textview);
        TextView pressureView  = (TextView) getActivity().findViewById(R.id.detail_pressure_textview);

        humidityView.setText(context.getString(R.string.format_humidity,cursor.getDouble(COL_HUMIDITY)));
        windView.setText(Utility.getFormattedWind(context, cursor.getFloat(COL_WIND_SPEED), cursor.getFloat(COL_DEGREE)));
        pressureView.setText(context.getString(R.string.format_pressure, cursor.getDouble(COL_PRESSURE)));


        if (mShareActionProvider !=null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        //mForecastAdapter.swapCursor(null);
    }



}
