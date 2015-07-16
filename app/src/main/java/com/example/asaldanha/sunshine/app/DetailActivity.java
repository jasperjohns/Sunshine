package com.example.asaldanha.sunshine.app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import android.widget.ShareActionProvider;


public class DetailActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider ;
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_share, menu);
//        getMenuInflater().inflate(R.menu.menu_share, menu);
        getMenuInflater().inflate(R.menu.menu_detail, menu);

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
            Intent SettingsAct = new Intent(this, SettingsActivity.class) ;
            startActivity(SettingsAct);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailActivityFragmentNew extends Fragment {


        private final String LOG_TAG = DetailActivityFragmentNew.class.getSimpleName();
        private final String SHARE_HASHTAG = " #SunShine";
        private String mForecastStr = null;


        public DetailActivityFragmentNew() {
            Log.v (LOG_TAG,"TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
            setHasOptionsMenu(true);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Bundle bundle = getActivity().getIntent().getExtras();

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            if(bundle.getString(Intent.EXTRA_TEXT)!= null)
            {
//            Log.v(LOG_TAG, bundle.getString(Intent.EXTRA_TEXT).toString());

                //TODO here get the string stored in the string variable and do
                TextView textDetail = (TextView) rootView.findViewById(R.id.textDetail);
//            textDetail.setText(bundle.getString(Intent.EXTRA_TEXT).toString());
//            textDetail.setText("asdasdasd");
                if (textDetail != null) {
                    mForecastStr = bundle.getString(Intent.EXTRA_TEXT).toString();
                    textDetail.setText(mForecastStr);
                }
            }
            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_share, menu);
            MenuItem item = menu.findItem(R.id.action_share);

            //ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

            ShareActionProvider shareActionProvider = new ShareActionProvider(getActivity());
            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(createShareForecastIntent());
                MenuItemCompat.setActionProvider(item, shareActionProvider);
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
    }


}
