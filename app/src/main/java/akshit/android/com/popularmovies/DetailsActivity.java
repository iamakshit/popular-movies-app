package akshit.android.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

            if (intent != null && intent.hasExtra("movie")) {
                movie = (Movie) intent.getSerializableExtra("movie");
                Log.i("DetailsActivity", movie.title);

                ((TextView) rootView.findViewById(R.id.movie_title))
                        .setText("Title : "+movie.title);

                ((TextView) rootView.findViewById(R.id.user_rating)).setText("User Rating : "+movie.userRating);

                ((TextView) rootView.findViewById(R.id.plot_summary)).setText("Summary : "+movie.plotSummary);

                ((TextView) rootView.findViewById(R.id.release_date)).setText("Release Date : "+movie.releaseDate);
                ImageView moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
                Picasso.with(getContext()).load(movie.posterPath).into(moviePoster);
                moviePoster.setAdjustViewBounds(true);
            }

            return rootView;
        }

    }
}
