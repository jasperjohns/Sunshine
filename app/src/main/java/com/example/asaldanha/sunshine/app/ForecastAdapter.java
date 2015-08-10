package com.example.asaldanha.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;
    private boolean mUseTodaylayout = true;


    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    public void setUseTodayLayout (boolean useTodayLayout){
        mUseTodaylayout= useTodayLayout;
    }


    @Override
    public int getItemViewType(int position) {
        // Override the logic if UseTodayLayout is set/not set
//        return (position == 0 && mUseTodaylayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;

/*
        if (position == 0){
            if (mUseTodaylayout){
                return VIEW_TYPE_TODAY;
            }
            else {
            }
        }
        else {
            return VIEW_TYPE_FUTURE_DAY;
        }
*/

        if (mUseTodaylayout){
            return (position == 0) ? VIEW_TYPE_TODAY :  VIEW_TYPE_FUTURE_DAY ;
        }
        else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }



    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }




    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
/*
        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
*/

        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        View view;
        // Decide what view type to return
        if (viewType == VIEW_TYPE_TODAY){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast_today, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
        }

        return view;
    }




    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE)));
        viewHolder.descriptionView.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));
        viewHolder.highTempView.setText( Utility.formatTemp(context, cursor.getLong(ForecastFragment.COL_WEATHER_MAX_TEMP)));
        viewHolder.lowTempView.setText(Utility.formatTemp(context, cursor.getLong(ForecastFragment.COL_WEATHER_MIN_TEMP)));
        int viewType = getItemViewType(cursor.getPosition());
        if (viewType == VIEW_TYPE_TODAY){
            viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        }
        else {
            viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
        }

        view.setTag(viewHolder);




/*
        TextView tv_date = (TextView) view.findViewById(R.id.list_item_date_textview);
        tv_date.setText(Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)));

        TextView tv_high = (TextView) view.findViewById(R.id.list_item_high_textview);
        tv_high.setText(formatTemp(cursor.getLong(ForecastFragment.COL_WEATHER_MAX_TEMP)));

        TextView tv_low = (TextView) view.findViewById(R.id.list_item_low_textview);
        tv_low.setText(formatTemp((cursor.getLong(ForecastFragment.COL_WEATHER_MIN_TEMP))));

        TextView tv_desc = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        tv_desc.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));

        ImageView iv_icon = (ImageView) view.findViewById(R.id.list_item_icon);
        iv_icon.setImageResource(R.drawable.ic_launcher);
*/
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;


        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);

        }
    }

}
