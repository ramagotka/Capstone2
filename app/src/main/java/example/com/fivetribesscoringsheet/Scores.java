package example.com.fivetribesscoringsheet;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

import example.com.fivetribesscoringsheet.data.MyDatabase;
import example.com.fivetribesscoringsheet.data.MyDatabaseContract;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * Created by hania on 03.10.16.
 */
public class Scores implements Serializable{
    final static String LOG_TAG = Scores.class.getName();

//    private static final String[] PEOPLE_COLUMNS = {
//            MyDatabaseContract.PeopleEntry.TABLE_NAME + "." + MyDatabaseContract.PeopleEntry._ID,
//            MyDatabaseContract.PeopleEntry.COLUMN_NAME
//    };

    public void set(int j, int value)
    {
        Log.d("HAHA", "Ustawiam " + j + " " + value);
        switch(j) {
            case 0:
                auction = value;
                break;
            case 1:
                money = value;
                break;
            case 2:
                yellow = value;
                break;
            case 3:
                white = value;
                break;
            case 4:
                djinn = value;
                break;
            case 5:
                palm = value;
                break;
            case 6:
                palace = value;
                break;
            case 7:
                camel = value;
                break;
            case 8:
                carts = value;
                break;
        }
    }

    public void setFirst(int j, int value){
        switch(j) {
            case 1:
                firstMoney = value;
                break;
            case 2:
                firstYellow = value;
                break;
            case 3:
                firstWhite = value;
                break;
            case 4:
                firstDjinn = value;
                break;
            case 5:
                firstPalm = value;
                break;
            case 6:
                firstPalace = value;
                break;
            case 7:
                firstCamel = value;
                break;
            case 8:
                firstCarts = value;
                break;
        }
    }

    public int get(int j)
    {
        switch(j) {
            case 0:
                return auction;
            case 1:
                return money;
            case 2:
                return yellow;
            case 3:
                return white;
            case 4:
                return djinn;
            case 5:
                return palm;
            case 6:
                return palace;
            case 7:
                return camel;
            case 8:
                return carts;
        }
        return 0;
    }

    public void resetRundTotal(){
        rundTotal = 0;
    }

    public void updateRundTotal(int i){
        rundTotal += i;
    }

    public int getRundTotal(){
        return rundTotal;
    }

    public void setTurn(int i){
        turn = i;
    }

    public int getTurn(){
        return turn;
    }

    private String name;
    private int yellow;
    private int white;
    private int money;
    private int djinn;
    private int palm;
    private int palace;
    private int camel;
    private int carts;
    private int win;
    private int rundTotal;
    private int bonusFromYellow;
    private int auction;
    private boolean inDatabase;
    private int firstYellow;
    private int firstWhite;
    private int firstMoney;
    private int firstDjinn;
    private int firstPalm;
    private int firstPalace;
    private int firstCamel;
    private int firstCarts;
    private int turn;

    public int calTotal(){
        return yellow + white + money + djinn + palm + palace + camel + carts + bonusFromYellow;
    }

    public int calRaundTotal(){
        Log.d(LOG_TAG, "auction " + auction + " money " + money);
        return yellow + white + money + djinn + 3*palm
                + 5*palace + camel + carts + bonusFromYellow - auction;
    }

    public int calTotalAdv(){
        return yellow + white + money + djinn + 3*palm
                + 5*palace + camel + carts + bonusFromYellow;
    }

    public void setName(String s){
        name = s;
    }

    public String getName(){
        return name;
    }

    public void setWin(boolean b) {
        if (b) {
            win = 1;
        }
        else {
            win = 0;
        }
    }

    public void initBool(){
        inDatabase = false;
    }

    public void insertToDatabase(Activity activity){
        if (inDatabase){
            Toast.makeText(activity.getApplicationContext(),
                    "Already in database!", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(LOG_TAG, "money" +  firstMoney);
        Log.d(LOG_TAG, "camel" +  firstCamel);
        Log.d(LOG_TAG, "yellow" +  firstYellow);
        ContentValues scoreValues = new ContentValues();

        Cursor cursor = activity.getContentResolver().query(
                MyDatabaseContract.PeopleEntry.CONTENT_URI, new String[]
                        {MyDatabaseContract.PeopleEntry._ID},
                MyDatabaseContract.PeopleEntry.COLUMN_NAME + " =?" , new String[] {name}
                , null);

        if (cursor.moveToFirst()){
            long people_id = cursor.getLong(cursor.getColumnIndex(MyDatabaseContract.PeopleEntry._ID));
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_PEOPLE_KEY,
                    String.valueOf(people_id));
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_MONEY, firstMoney);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_YELLOW, firstYellow);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_WHITE, firstWhite);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_CAMEL, firstCamel);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_DJINN, firstDjinn);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_PALACE, firstPalace);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_OASIS, firstPalm);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_MERCHANDISE, firstCarts);
            scoreValues.put(MyDatabaseContract.ScoresEntry.COLUMN_WIN, win);

            activity.getContentResolver()
                    .insert(MyDatabaseContract.ScoresEntry.CONTENT_URI, scoreValues);
        }
        else {
            Log.d("Scores ", "Adding to database failed");
        }
        cursor.close();
        Intent intent = new Intent(activity, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = {R.xml.new_app_widget_info};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        activity.sendBroadcast(intent);
        }

    public int getBonusFromYellow() {
        return bonusFromYellow;
    }

    public void setBonusFromYellow(int bonusFromYellow) {
        this.bonusFromYellow = bonusFromYellow;
    }
}
