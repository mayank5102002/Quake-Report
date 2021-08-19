package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private EarthQuakeAdapter adapter;
    View progress ;
    private TextView mEmptyStateTextView;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL ="https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        progress = (View) findViewById(R.id.loading_spinner);
        if (checkNetwork()){
        LoaderManager manager = getSupportLoaderManager();
        manager.initLoader(EARTHQUAKE_LOADER_ID,null,this);}
        else{
            mEmptyStateTextView.setText("No internet connection");
            progress.setVisibility(View.GONE);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthQuakeAdapter(this,new ArrayList<EarthQuake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake earthQuake = (EarthQuake) adapter.getItem(position);
                Uri earthquakeuri = Uri.parse(earthQuake.geturl());
                Intent earthquakeintent = new Intent(Intent.ACTION_VIEW,earthquakeuri);
                startActivity(earthquakeintent);
            }
        });
    }

    private boolean checkNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeClassLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(androidx.loader.content.Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        progress.setVisibility(View.GONE);
        mEmptyStateTextView.setText("No earthquakes found");
        adapter.clear();
        if (earthQuakes != null && !earthQuakes.isEmpty()) {
            adapter.addAll(earthQuakes);
        }
    }

    @Override
    public void onLoaderReset(androidx.loader.content.Loader<List<EarthQuake>> loader) {
        adapter.clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}