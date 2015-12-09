package akshit.android.com.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akshit on 9/12/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_display, parent, false);
        }


        TextView plotSummary = (TextView) convertView.findViewById(R.id.movie_plotSummary);
        plotSummary.setText(movie.plotSummary);


        TextView userRating = (TextView) convertView.findViewById(R.id.movie_userRating);
        userRating.setText(movie.userRating);


        TextView title = (TextView) convertView.findViewById(R.id.movie_title);
        title.setText(movie.title);

        return convertView;
    }
}
