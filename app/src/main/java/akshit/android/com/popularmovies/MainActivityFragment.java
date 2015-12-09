package akshit.android.com.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter movieAdapter;
    public static String TAG = MainActivityFragment.class.getSimpleName();

    Movie[] movies = {
            new Movie("Captain America", "It has captain america", "5", new Date()),
            new Movie("Iron Man", "It has iron man", "4", new Date()),
            new Movie("Avengers", "It has all", "3", new Date()),
            new Movie("Thor", "It has thor", "1", new Date()),
    };

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMoviePosterTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        Log.i("MainActivityFragment", "Movies [0]" + movies[0].plotSummary + movies[0].title);
        movieAdapter = new MovieAdapter(getActivity(), Arrays.asList(movies));
        gridView.setAdapter(movieAdapter);

        return rootView;

    }

    public void fetchMoviePosterTask() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //  String pinCode = prefs.getString(getString(R.string.pref_location_key);
        String sortMethod = "vote_average.desc";

        FetchMoviePosterTask task = new FetchMoviePosterTask();

        int corePoolSize = 60;
        int maximumPoolSize = 80;
        int keepAliveTime = 10;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
        Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
        Log.i(TAG, "Refresh Action being called");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sortMethod);
        else
            task.execute(sortMethod);
    }

    public class FetchMoviePosterTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Inside onPreExecute Method");

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            if (params == null) {
                return null;
            }

            String sortMethod = params[0];

            if (sortMethod == null || sortMethod.isEmpty()) {
                return null;
            }

            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie/?";
            final String SORT_PARAM = "sort_by";
            final String API_PARAM = "api_key";
            final String API_KEY = "352d4079b8281b8afc99cb142fa05a0e";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;
            Log.i(TAG, "Starting ...");

            try {
                Uri buildUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_PARAM, API_KEY).appendQueryParameter(SORT_PARAM, sortMethod).build();
                URL url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                int status = urlConnection.getResponseCode();

                Log.i(TAG, "Movie API Server status :" + status);
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                br.close();

                if (buffer.length() == 0) {
                    Log.i(TAG, "bufferLength is zero");
                    return null;
                }

                jsonStr = buffer.toString();

                Log.i(TAG, "JsonStr = " + jsonStr);

            } catch (IOException e) {
                Log.e(TAG, "IOException", e);

            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);

                    }
                }
            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String data) {
            Log.i(TAG, "Inside onPostExecute method");

            super.onPostExecute(data);
        }

    }
}
