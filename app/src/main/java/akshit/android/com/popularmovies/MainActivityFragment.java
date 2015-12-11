package akshit.android.com.popularmovies;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
    private static ArrayList<Movie> movies;
    public static String TAG = MainActivityFragment.class.getSimpleName();
    public static String imagePath="http://image.tmdb.org/t/p/w185/";

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
        final GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity().getApplicationContext();
                Movie movie = (Movie) gridView.getAdapter().getItem(position);

                Toast.makeText(context, "Following info: " + movie.title, Toast.LENGTH_SHORT).show();

                // Intent intent = new Intent(context, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, text);
                // startActivity(intent);


            }
        });

        return rootView;

    }

    private Movie[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_TITLE = "original_title";
        final String MDB_OVERVIEW = "overview";
        final String MDB_POST = "poster_path";
        final String MDB_RATE = "vote_average";
        final String MDB_RELEASEDATE = "release_date";
        final String MDB_LIST = "results";

        JSONObject movieDBJson = new JSONObject(forecastJsonStr);
        JSONArray movieDBArray = movieDBJson.getJSONArray(MDB_LIST);
        Movie[] movies = new Movie[numDays];
        for (int i = 0; i < movieDBArray.length(); i++) {

            JSONObject movieData = movieDBArray.getJSONObject(i);


            String title = movieData.getString(MDB_TITLE);
            String plotSummary = movieData.getString(MDB_OVERVIEW);
            String userRating = movieData.getString(MDB_RATE);
            String releaseDate = movieData.getString(MDB_RELEASEDATE);
            String posterPath = movieData.getString(MDB_POST);
            Movie movie = new Movie(title, plotSummary, userRating, releaseDate, posterPath);
            movies[i] = movie;

        }

        return movies;

    }

    public void fetchMoviePosterTask() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortMethod = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        Log.i(TAG, "sortMethod =" + sortMethod);
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

    public class FetchMoviePosterTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Inside onPreExecute Method");

            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(String... params) {


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
            int num = 20;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;
            Log.i(TAG, "Starting ...");
            Movie[] data = new Movie[num];

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
                try {
                    data = getWeatherDataFromJson(jsonStr, num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

            return data;
        }

        @Override
        protected void onPostExecute(Movie[] data) {
            Log.i(TAG, "Inside onPostExecute method");
            Log.i(TAG, "movie size : " + data.length);

            movieAdapter.clear();
            if (movies != null) {
                movies.clear();
            } else {
                movies = new ArrayList<Movie>();
            }
            for (Movie movie : data) {
                movie.posterPath=imagePath+movie.posterPath;
                movieAdapter.add(movie);
                movies.add(movie);
            }

            super.onPostExecute(data);
        }

    }
}
