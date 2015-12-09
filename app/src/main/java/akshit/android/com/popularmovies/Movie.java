package akshit.android.com.popularmovies;

/**
 * Created by akshit on 9/12/15.
 */
public class Movie {


    String title;
    String plotSummary;
    String userRating;
    String releaseDate;
    String posterPath;

    public Movie() {

    }

    public Movie(String title, String plotSummary, String userRating, String releaseDate, String posterPath) {
        this.title = title;
        this.plotSummary = plotSummary;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath=posterPath;
    }


}
