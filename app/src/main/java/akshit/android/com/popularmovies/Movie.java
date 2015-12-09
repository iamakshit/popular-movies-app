package akshit.android.com.popularmovies;

import java.util.Date;

/**
 * Created by akshit on 9/12/15.
 */
public class Movie {


    String title;
    String plotSummary;
    String userRating;
    Date releaseDate;

    public Movie(String title, String plotSummary, String userRating, Date releaseDate) {
        this.title=title;
         this.plotSummary=plotSummary;
       this.userRating=userRating;
        this.releaseDate=releaseDate;
    }
}
