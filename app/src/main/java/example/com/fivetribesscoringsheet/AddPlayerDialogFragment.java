package example.com.fivetribesscoringsheet;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import example.com.fivetribesscoringsheet.data.MyDatabase;
import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

import static android.support.v7.app.AlertDialog.*;


public class AddPlayerDialogFragment extends DialogFragment {

    private static final String[] PEOPLE_COLUMNS = {
            MyDatabaseContract.PeopleEntry.TABLE_NAME + "." + MyDatabaseContract.PeopleEntry._ID,
            MyDatabaseContract.PeopleEntry.COLUMN_NAME
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Builder builder = new Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_add_player_dialog, null))
                .setPositiveButton(R.string.add_new_player_button,
                        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = (EditText) getDialog()
                        .findViewById(R.id.fragment_add_player_edittext);
                String playerName = String.valueOf(editText.getText());

                Cursor cursor = getActivity().getContentResolver().query(
                        MyDatabaseContract.PeopleEntry.CONTENT_URI, PEOPLE_COLUMNS, null, null
                        , MyDatabaseContract.PeopleEntry.COLUMN_NAME + " ASC");

                while (cursor.moveToNext()){
                    String name = cursor
                            .getString(cursor.getColumnIndex(MyDatabaseContract.PeopleEntry.COLUMN_NAME));
                    //Log.d("DIalog", name);
                    if (name.equals(playerName)){
                        Toast.makeText(getContext(), playerName + R.string.in_database_fragment,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                ContentValues playerValues = new ContentValues();
                playerValues.put(MyDatabaseContract.PeopleEntry.COLUMN_NAME, playerName);
                playerValues.put(MyDatabaseContract.PeopleEntry.COLUMN_WINS, 0);

                getActivity().getContentResolver()
                        .insert(MyDatabaseContract.PeopleEntry.CONTENT_URI, playerValues);

            }
        }).setNegativeButton(R.string.add_new_player_negative_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }
}
