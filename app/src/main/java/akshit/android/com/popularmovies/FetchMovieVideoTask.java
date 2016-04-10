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
public class FetchMovieVideoTask extends AsyncTask<String, Void, String> {
    public static String TAG = FetchMovieVideoTask.class.getSimpleName();
    final String API_PARAM = "api_key";
    final String API_KEY = "352d4079b8281b8afc99cb142fa05a0e";
    final String MDB_LIST = "results";
    final String MDB_YOUTUBE = "key";
    String BASE_URL = "https://api.themoviedb.org/3/movie/{id}/videos?";

    private Movie movie;

    public FetchMovieVideoTask(Movie movie) {
        this.movie = movie;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params == null) {
            return null;
        }

        String movieId = params[0];

        if (movieId == null || movieId.isEmpty()) {
            return null;
        }

        String youTubeLink = null;
        int num = 20;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        Log.i(TAG, "Starting ...");
        Log.i(TAG, "MovieId is " + movieId);
        try {
            BASE_URL = BASE_URL.replace("{id}", movieId);
            Uri buildUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_PARAM, API_KEY).build();
            Log.i(TAG, "Uri being called = " + buildUri.toString());
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

                JSONObject movieDBJson = new JSONObject(jsonStr);
                JSONArray movieDBArray = movieDBJson.getJSONArray(MDB_LIST);
                JSONObject movieData = movieDBArray.getJSONObject(0);
                youTubeLink = movieData.getString(MDB_YOUTUBE);


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
        return youTubeLink;
    }


    @Override
    protected void onPostExecute(String youtubeLink) {
        Log.i(TAG, "Inside onPostExecute method");
        Log.i(TAG, "Youtube video link: " + youtubeLink);
        movie.setYouTubeVideoLink(youtubeLink);
        super.onPostExecute(youtubeLink);
    }


}
