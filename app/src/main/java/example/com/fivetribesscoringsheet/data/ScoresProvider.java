package example.com.fivetribesscoringsheet.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by hania on 12.09.16.
 */
public class ScoresProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MyDatabase mOpenHelper;

    static final int PEOPLE = 100;
    static final int PEOPLE_WITH_SCORES = 101;
    static final int SCORES = 200;

    private static final SQLiteQueryBuilder mQueryBuilder;

    static{
        mQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join
        mQueryBuilder.setTables(
                MyDatabaseContract.PeopleEntry.TABLE_NAME + " INNER JOIN " +
                        MyDatabaseContract.ScoresEntry.TABLE_NAME +
                        " ON " + MyDatabaseContract.ScoresEntry.TABLE_NAME +
                        "." + MyDatabaseContract.ScoresEntry.COLUMN_PEOPLE_KEY +
                        " = " + MyDatabaseContract.PeopleEntry.TABLE_NAME +
                        "." + MyDatabaseContract.PeopleEntry._ID);
    }

    private static final String sPeopleWithScoresSelection =
            MyDatabaseContract.PeopleEntry.TABLE_NAME+
                    "." + MyDatabaseContract.PeopleEntry.COLUMN_NAME + " = ? ";

    @Override
    public boolean onCreate() {
        mOpenHelper = new MyDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)){
            case PEOPLE:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyDatabaseContract.PeopleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case PEOPLE_WITH_SCORES:{
                retCursor = getPeoplewithScore(uri, projection, sortOrder);
                break;
            }
            case SCORES:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MyDatabaseContract.ScoresEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    private Cursor getPeoplewithScore(Uri uri, String[] projection, String sortOrder) {

        String name = MyDatabaseContract.getNameFromUri(uri);//TODO potencjalne miejsce do szukania bledu

        return mQueryBuilder.query(mOpenHelper.getReadableDatabase(),projection,
                sPeopleWithScoresSelection, new String[]{name}, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case PEOPLE:
                return MyDatabaseContract.PeopleEntry.CONTENT_TYPE;
            case PEOPLE_WITH_SCORES:
                return MyDatabaseContract.ScoresEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        getContext().getContentResolver().notifyChange(MyDatabaseContract.PeopleEntry.CONTENT_URI,
                null);

        switch (match){
            case PEOPLE:
            {
                long _id = db.insert(MyDatabaseContract.PeopleEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MyDatabaseContract.PeopleEntry.buildPeopleUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SCORES:
            {
                long _id = db.insert(MyDatabaseContract.ScoresEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MyDatabaseContract.ScoresEntry.buildScoresUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case PEOPLE:
            {
                rowsDeleted = db.delete(
                        MyDatabaseContract.PeopleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case SCORES:
            {
                rowsDeleted = db.delete(
                        MyDatabaseContract.ScoresEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case PEOPLE:
            {
                rowsUpdated = db.update(MyDatabaseContract.PeopleEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case SCORES:
            {
                rowsUpdated = db.update(MyDatabaseContract.ScoresEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    //TODO bulkinsert

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MyDatabaseContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MyDatabaseContract.PATH_PEOPLE, PEOPLE);
        matcher.addURI(authority, MyDatabaseContract.PATH_PEOPLE+ "/*", PEOPLE_WITH_SCORES); //TODO nie jestem pewna że to będzie działać
        matcher.addURI(authority,MyDatabaseContract.PATH_SCORES, SCORES);

        return matcher;
    }

}
