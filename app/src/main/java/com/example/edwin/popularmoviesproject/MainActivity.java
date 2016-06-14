package com.example.edwin.popularmoviesproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static final String[] title = new String[10];
    static final String[] posterURL = new String[title.length];
    static final String[] synopsis = new String[title.length];
    static final double[] rating = new double[title.length];
    static final String[] release = new String[title.length];

    GridView gridView;
    ImageAdapter mMovieInfoAdapter = new ImageAdapter(this, title, posterURL);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateGrid();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class)
                        .putExtra("title", title[position])
                        .putExtra("imageurl", posterURL[position])
                        .putExtra("overview", synopsis[position])
                        .putExtra("rating", rating[position])
                        .putExtra("release", release[position]);
                startActivity(intent);
            }
        });

    }

    private void updateGrid() {
        FetchMovieInfo movieTask = new FetchMovieInfo();
        movieTask.execute();

        gridView = (GridView) findViewById(R.id.movies_gridview);
        gridView.setAdapter(mMovieInfoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateGrid();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieInfo extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = FetchMovieInfo.class.getSimpleName();

        private String[] getMovieInfofromJson(String movieinfoJsonStr)
                throws JSONException {

            final String OWM_RESULT = "results";
            final String OWN_TITLE = "title";
            final String OWN_POSTER = "poster_path";
            final String OWN_OVERVIEW = "overview";
            final String OWN_RELEASE = "release_date";
            final String OWN_RATING = "vote_average";
            final String imageBaseURL = "http://image.tmdb.org/t/p/";
            //final String lowImageQuality = "w185";
            final String highImageQuality = "w780";

            JSONObject movieinfoJson = new JSONObject(movieinfoJsonStr);
            JSONArray moviesArray = movieinfoJson.getJSONArray(OWM_RESULT);


            for (int i = 0; i < title.length; i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                title[i] = movie.getString(OWN_TITLE);
                posterURL[i] = imageBaseURL + highImageQuality + movie.getString(OWN_POSTER);
                synopsis[i] = movie.getString(OWN_OVERVIEW);
                rating[i] = movie.getInt(OWN_RATING);
                release[i] = movie.getString(OWN_RELEASE);
            }

            return title;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String infoJsonStr = null;

            try {
                String INFO_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String APPID_PARAM = "api_key";

                SharedPreferences sharedPrefs =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String sortOrder = sharedPrefs.getString(getString(R.string.pref_sort_key), "popular?");

                INFO_BASE_URL = INFO_BASE_URL + sortOrder;

                Log.v(LOG_TAG, "Sort Order :  " + sortOrder);

                Uri builtUri = Uri.parse(INFO_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THEMOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                infoJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
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

            try {
                return getMovieInfofromJson(infoJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        /*@Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                gridView = (GridView) findViewById(R.id.movies_gridview);
                gridView.setAdapter(mMovieInfoAdapter);
                // New data is back from the server.  Hooray!
            }
        }*/
    }

}