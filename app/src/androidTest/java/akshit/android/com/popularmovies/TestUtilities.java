package akshit.android.com.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import akshit.android.com.popularmovies.data.MovieContract;

/**
 * Created by akshitgupta on 12/04/16.
 */
public class TestUtilities extends AndroidTestCase {


    static ContentValues createMovie() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "123");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, "Superman vs Batman");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SUMMARY, "This movie is good");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, "3.5");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, "12/3/15");
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE, "http://akshit.org");
        return testValues;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
