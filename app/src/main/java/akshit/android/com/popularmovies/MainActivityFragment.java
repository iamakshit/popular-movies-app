package akshit.android.com.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;

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

    }

    public class FetchMoviePosterTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Inside onPreExecute Method");

            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] data) {
            Log.i(TAG, "Inside onPostExecute method");

            super.onPostExecute(data);
        }

    }
}
