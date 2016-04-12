package akshit.android.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Log.i("DetailsActivity", "savedInstanceState is null");
            // During initial setup, plug in the details fragment.
            DetailsActivityFragment details = new DetailsActivityFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, details).commit();
        }


        // For button

    }

    public static class DetailsActivityFragment extends Fragment {

        public static Movie movie;

        public DetailsActivityFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.i("DetailsActivity", "onCreateView called");

            View rootView = inflater.inflate(R.layout.fragment_details, container, false);


            // The detail Activity called via intent.  Inspect the intent for forecast data.
            Intent intent = getActivity().getIntent();
            ArrayList<String> list= new ArrayList<>();
            if (intent != null && intent.hasExtra("movie")) {
                movie = (Movie) intent.getSerializableExtra("movie");
                Log.i("DetailsActivity", movie.title);

                ((TextView) rootView.findViewById(R.id.movie_title))
                        .setText("Title : " + movie.title);

                ((TextView) rootView.findViewById(R.id.user_rating)).setText("User Rating : " + movie.userRating);

                ((TextView) rootView.findViewById(R.id.plot_summary)).setText("Summary : " + movie.plotSummary);

                ((TextView) rootView.findViewById(R.id.release_date)).setText("Release Date : " + movie.releaseDate);
                ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
                Picasso.with(getContext()).load(movie.posterPath).into(moviePoster);
                moviePoster.setAdjustViewBounds(true);

                fetchMoviePosterTask(movie);
                list=fetchMovieReviewTask(movie);
                Log.i("DetailsActivity", "YoutubeLink in DetailActivity is " + movie.getYouTubeVideoLink());

            }

            ArrayAdapter<String> listAdapter= new ArrayAdapter<String>(getActivity(),R.layout.list_review,R.id.list_item_textview,list);
            ListView listView = (ListView) rootView.findViewById(R.id.listview_reviews);
            listView.setAdapter(listAdapter);

            Button button = (Button) rootView.findViewById(R.id.trailer);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Context context = getActivity().getApplicationContext();

                    Toast.makeText(context, "Playing trailer of movie : " + movie.title, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movie.getYouTubeVideoLink()));
                    startActivity(intent);


                }
            });

            return rootView;
        }


        public ArrayList<String> fetchMovieReviewTask(Movie movie) {

            ArrayList<String> list= new ArrayList<String>();
            FetchMovieReviewTask task;
            task = new FetchMovieReviewTask();
            ArrayList<MovieReview> result = null;
            int corePoolSize = 60;
            int maximumPoolSize = 80;
            int keepAliveTime = 10;
            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
            Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            Log.i("DetailsActivity", "Refresh Action being called");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movie.id);
            else
                task.execute(movie.id);
            try {
                result = task.get();
                Log.i("DetailsActivity", "MovieReviewResult :  " + result);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            for(MovieReview movieReview: result)
            {
                StringBuilder input= new StringBuilder();
                input.append(movieReview.getAuthor()).append("\n").append(movieReview.getContent());
                list.add(input.toString());
            }

          //  movie.setYouTubeVideoLink(result);
            return list;
        }

        public void fetchMoviePosterTask(Movie movie) {

            FetchMovieVideoTask task;
            task = new FetchMovieVideoTask(movie);
            String result = null;
            int corePoolSize = 60;
            int maximumPoolSize = 80;
            int keepAliveTime = 10;
            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
            Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            Log.i("DetailsActivity", "Refresh Action being called");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movie.id);
            else
                task.execute(movie.id);
            try {
                result = task.get();
                Log.i("DetailsActivity", "result :  " + result);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            movie.setYouTubeVideoLink(result);

        }
    }
}
