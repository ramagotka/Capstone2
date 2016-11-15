package example.com.fivetribesscoringsheet;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

/**
 * Created by hania on 11.09.16.
 */
public class MyCursorAdapter extends CursorAdapter {
    private final String LOG_TAG = MyCursorAdapter.class.getName();
    public ArrayList<String> list = new ArrayList<String>();


    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_add_player, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.item_player_name);
        final String player = cursor
                .getString(cursor.getColumnIndexOrThrow(MyDatabaseContract.PeopleEntry.COLUMN_NAME));
        textView.setText(player);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    list.add(player);
                }
                else {
                    list.remove(player);
                }
            }
        });
    }
}
