package example.com.fivetribesscoringsheet.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hania on 12.09.16.
 */
public class MyDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "scores.db";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PEOPLE_TABLE = "CREATE TABLE " +
                MyDatabaseContract.PeopleEntry.TABLE_NAME + " (" +
                MyDatabaseContract.PeopleEntry._ID + " INTEGER PRIMARY KEY," +
                MyDatabaseContract.PeopleEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, "  +
                MyDatabaseContract.PeopleEntry.COLUMN_WINS + " INTEGER NOT NULL "
                + " );";

        final String SQL_CREATE_SCORES_TABLE = "CREATE TABLE " +
                MyDatabaseContract.ScoresEntry.TABLE_NAME + " (" +
                MyDatabaseContract.ScoresEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                MyDatabaseContract.ScoresEntry.COLUMN_PEOPLE_KEY + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_MONEY + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_YELLOW + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_WHITE + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_DJINN + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_OASIS + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_PALACE + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_CAMEL + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_MERCHANDISE + " INTEGER NOT NULL, " +
                MyDatabaseContract.ScoresEntry.COLUMN_WIN + " INTEGER NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_PEOPLE_TABLE);
        db.execSQL(SQL_CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyDatabaseContract.PeopleEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MyDatabaseContract.ScoresEntry.TABLE_NAME);
        onCreate(db);
    }
}
