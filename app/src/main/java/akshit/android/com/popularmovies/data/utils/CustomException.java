package akshit.android.com.popularmovies.data.utils;

/**
 * Created by akshitgupta on 08/05/16.
 */
public class CustomException extends Exception {

    String message;

    public CustomException(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {

        return message;
    }
}
