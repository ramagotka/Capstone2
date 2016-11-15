package example.com.fivetribesscoringsheet.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hania on 12.09.16.
 */
public class MyDatabaseContract {

    public static final String CONTENT_AUTHORITY = "example.com.fivetribesscoringsheet";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PEOPLE = "people";
    public static final String PATH_SCORES = "scores";

    public static final class PeopleEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PEOPLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PEOPLE; //TODO nie rozumiem

        // Table name
        public static final String TABLE_NAME = "people";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_WINS = "wins";

        public static Uri buildPeopleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ScoresEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SCORES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCORES; //TODO nie rozumiem

        // Table name
        public static final String TABLE_NAME = "scores";

        public static final String COLUMN_PEOPLE_KEY = "people_key";

        public static final String COLUMN_MONEY = "money";

        public static final String COLUMN_YELLOW = "yellow";

        public static final String COLUMN_WHITE = "white";

        public static final String COLUMN_DJINN = "djinn";

        public static final String COLUMN_OASIS = "oasis";

        public static final String COLUMN_PALACE = "palace";

        public static final String COLUMN_CAMEL = "camel";

        public static final String COLUMN_MERCHANDISE = "merchandise";

        public static final String COLUMN_WIN = "win";

        public static Uri buildScoresUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static String getNameFromUri(Uri uri) {
        return uri.getPathSegments().get(1);
    }
}
