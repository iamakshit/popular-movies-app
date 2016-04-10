package akshit.android.com.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by akshitgupta on 10/04/16.
 */
public class FetchMoviePosterTask extends AsyncTask<String, Void, Movie[]> {
    public static String TAG = FetchMoviePosterTask.class.getSimpleName();
    private static ArrayList<Movie> movies;
    private MovieAdapter movieAdapter;
    public static String imagePath = "http://image.tmdb.org/t/p/w185/";

    public FetchMoviePosterTask(MovieAdapter movieAdapter) {
        this.movieAdapter = movieAdapter;
    }

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
            movie.posterPath = imagePath + movie.posterPath;
            movieAdapter.add(movie);
            movies.add(movie);
        }

        super.onPostExecute(data);
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
        final String MDB_ID = "id";

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
            String id = movieData.getString(MDB_ID);
            Movie movie = new Movie(title, plotSummary, userRating, releaseDate, posterPath, id);
            movies[i] = movie;

        }

        return movies;

    }
}