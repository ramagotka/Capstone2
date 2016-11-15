package example.com.fivetribesscoringsheet;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

import static android.R.id.list;

public class AddPlayerActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String LOG_TAG = AddPlayerActivity.class.getName();

    private String class_name;
    private final String name = "class_name";
    private MyCursorAdapter mmyCursorAdapter;
    private static final int PEOPLE_LOADER = 0;


    private static final String[] PEOPLE_COLUMNS = {
            MyDatabaseContract.PeopleEntry.TABLE_NAME + "." + MyDatabaseContract.PeopleEntry._ID,
            MyDatabaseContract.PeopleEntry.COLUMN_NAME
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.addplayer_activity_title);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException e){
            Log.d(LOG_TAG, e.getMessage());
        }

        getLoaderManager().initLoader(PEOPLE_LOADER, null, this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                class_name = null;
            } else {
                class_name = extras.getString(name);
            }
        } else {
            class_name = (String) savedInstanceState.getString(name);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new AddPlayerDialogFragment();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });


        Cursor cursor = getContentResolver().query(MyDatabaseContract.PeopleEntry.CONTENT_URI,
                PEOPLE_COLUMNS, null, null, MyDatabaseContract.PeopleEntry.COLUMN_NAME + " ASC");
        ListView listView = (ListView) findViewById(R.id.add_player_listview);
        listView.setEmptyView(findViewById(R.id.empty_view_add_player));

        mmyCursorAdapter = new MyCursorAdapter(getApplicationContext(), cursor, 0);
        listView.setAdapter(mmyCursorAdapter);

    }

    public void onClickFinish(View view) {
        ArrayList<String> list = mmyCursorAdapter.list;
        if (list.size() < 2) {
            Toast.makeText(getApplicationContext(), R.string.addplayer_error, Toast.LENGTH_LONG).show();
        }
        else if(list.size() > 5){
            Toast.makeText(getApplicationContext(), R.string.addplayer_error_more, Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent;
            Log.d(LOG_TAG, "string " + getString(R.string.bas) + " " + class_name);
            if (class_name.equals(getString(R.string.bas))) {
                intent = new Intent(this, BasicActivity.class);
            } else {
                intent = new Intent(this, AdvancedActivity.class);
            }
            intent.putExtra("people", list);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = MyDatabaseContract.PeopleEntry.COLUMN_NAME + " ASC";

        return new android.content.CursorLoader(getApplicationContext(),
                MyDatabaseContract.PeopleEntry.CONTENT_URI, PEOPLE_COLUMNS,
                null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        data.setNotificationUri(getContentResolver(), MyDatabaseContract.PeopleEntry.CONTENT_URI);
        mmyCursorAdapter.changeCursor(data);

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mmyCursorAdapter.changeCursor(null);
    }
}