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

import akshit.android.com.popularmovies.data.utils.ConstantUtils;

/**
 * Created by akshitgupta on 10/04/16.
 */
public class FetchMovieReviewTask extends AsyncTask<String,Void,ArrayList<MovieReview>> {

    public static String TAG = FetchMovieReviewTask.class.getSimpleName();
    final String API_PARAM = "api_key";
    final String MDB_LIST = "results";
    final String MDB_AUTHOR = "author";
    final String MDB_CONTENT = "content";


    String BASE_URL = "https://api.themoviedb.org/3/movie/{id}/reviews?";

    public FetchMovieReviewTask() {
    }

    @Override
    protected ArrayList<MovieReview> doInBackground(String... params) {

        if (params == null) {
            return null;
        }

        String movieId = params[0];

        if (movieId == null || movieId.isEmpty()) {
            return null;
        }

        ArrayList<MovieReview> movieReviews= new ArrayList<>();
        int num = 20;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;
        Log.i(TAG, "Starting ...");
        Log.i(TAG, "MovieId is " + movieId);
        try {
            BASE_URL = BASE_URL.replace("{id}", movieId);
            Uri buildUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_PARAM, ConstantUtils.API_KEY).build();
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
                for(int i=0;i<movieDBArray.length();i++)
                {
                    JSONObject movieData = movieDBArray.getJSONObject(i);
                    String author=movieData.getString(MDB_AUTHOR);
                    String content= movieData.getString(MDB_CONTENT);
                    MovieReview movieReview= new MovieReview();
                    movieReview.setAuthor(author);
                    movieReview.setContent(content);
                    movieReviews.add(movieReview);
                }


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
        return movieReviews;
    }


    @Override
    protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
        Log.i(TAG, "Inside onPostExecute method");
        Log.i(TAG,"MovieReviews Length "+movieReviews.size());
        super.onPostExecute(movieReviews);
    }


}
