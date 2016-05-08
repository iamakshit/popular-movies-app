package akshit.android.com.popularmovies;

import java.io.Serializable;

/**
 * Created by akshit on 9/12/15.
 */
public class Movie implements Serializable {


    String id;
    String title;
    String plotSummary;
    String userRating;
    String releaseDate;
    String posterPath;
    String youTubeVideoLink;

    public Movie() {

    }

    public Movie(String title, String plotSummary, String userRating, String releaseDate, String posterPath, String id) {
        this.title = title;
        this.plotSummary = plotSummary;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.id = id;
    }

    public String getYouTubeVideoLink() {
        return youTubeVideoLink;
    }

    public void setYouTubeVideoLink(String youTubeVideoLink) {
        this.youTubeVideoLink = youTubeVideoLink;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", plotSummary='" + plotSummary + '\'' +
                ", userRating='" + userRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", youTubeVideoLink='" + youTubeVideoLink + '\'' +
                '}';
    }
}
