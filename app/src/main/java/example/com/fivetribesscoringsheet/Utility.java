package example.com.fivetribesscoringsheet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceConfigurationError;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

/**
 * Created by hania on 18.10.16.
 */

public class Utility {
    public static void whoFirstInEach(List<Scores> arrayList){
        int money = 0;
        int yellow = 0;
        int white = 0;
        int djinn = 0;
        int palm = 0;
        int camels = 0;
        int palace = 0;
        int carts = 0;
        for (int i = 0; i < arrayList.size(); i++){
            if (money < arrayList.get(i).get(1)){
                money = arrayList.get(i).get(1);
            }
            if (yellow < arrayList.get(i).get(2)){
                yellow = arrayList.get(i).get(2);
            }
            if (white < arrayList.get(i).get(3)){
                white = arrayList.get(i).get(3);
            }
            if (djinn < arrayList.get(i).get(4)){
                djinn = arrayList.get(i).get(4);
            }
            if (palm < arrayList.get(i).get(5)){
                palm = arrayList.get(i).get(5);
            }
            if (palace < arrayList.get(i).get(6)){
                palace = arrayList.get(i).get(6);
            }
            if (camels < arrayList.get(i).get(7)){
                camels = arrayList.get(i).get(7);
            }
            if (carts < arrayList.get(i).get(8)){
                carts = arrayList.get(i).get(8);
            }
        }
        for (int i = 0; i < arrayList.size(); i++){
            if (money == arrayList.get(i).get(1)){
                arrayList.get(i).setFirst(1, 1);
            }
            else {
                arrayList.get(i).setFirst(1, 0);
            }
            if (yellow == arrayList.get(i).get(2)){
                arrayList.get(i).setFirst(2, 1);
            }
            else {
                arrayList.get(i).setFirst(2, 0);
            }
            if (white == arrayList.get(i).get(3)){
                arrayList.get(i).setFirst(3, 1);
            }
            else {
                arrayList.get(i).setFirst(3, 0);
            }
            if (djinn == arrayList.get(i).get(4)){
                arrayList.get(i).setFirst(4, 1);
            }
            else {
                arrayList.get(i).setFirst(4, 0);
            }
            if (palm == arrayList.get(i).get(5)){
                arrayList.get(i).setFirst(5, 1);
            }
            else {
                arrayList.get(i).setFirst(5, 0);
            }
            if (palace == arrayList.get(i).get(6)){
                arrayList.get(i).setFirst(6, 1);
            }
            else {
                arrayList.get(i).setFirst(6, 0);
            }
            if (camels == arrayList.get(i).get(7)){
                arrayList.get(i).setFirst(7, 1);
            }
            else {
                arrayList.get(i).setFirst(7, 0);
            }
            if (carts == arrayList.get(i).get(8)){
                arrayList.get(i).setFirst(8, 1);
            }
            else {
                arrayList.get(i).setFirst(8, 0);
            }
        }
    }
    public static String whoFirst(List<Scores> scores, Context context, String type){
        int size = scores.size();
        int total = 0;
        String name  = "";
        ArrayList<String> names = new ArrayList<String>();
        if (type.equals(context.getString(R.string.bas))) {
            for (int i = 0; i < size; i++) {

                if (total <= scores.get(i).calTotal()) {
                    total = scores.get(i).calTotal();
                }
            }
            for (int i = 0; i < size; i++) {
                if (total == scores.get(i).calTotal()) {
                    names.add(scores.get(i).getName());
                    updateWinsInDatabase(scores.get(i).getName(), context);
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {

                if (total <= scores.get(i).calTotalAdv()) {
                    total = scores.get(i).calTotalAdv();
                }
            }
            for (int i = 0; i < size; i++) {
                if (total == scores.get(i).calTotalAdv()) {
                    names.add(scores.get(i).getName());
                    updateWinsInDatabase(scores.get(i).getName(), context);
                }
            }
        }

        for (int i = 0; i < size; i++){
            for (int j = 0; j < names.size(); j++) {
                if (names.get(j).equals(scores.get(i).getName())) {
                    scores.get(i).setWin(true);
                } else {
                    scores.get(i).setWin(false);
                }
            }
        }
        name = names.get(0);
        int count = 1;
        for (int j = 1; j < names.size(); j++){
            if (j < names.size() - 1) {
                name += ", " + names.get(j);
                count++;
            }
            else {
                name += " and " + names.get(j);
                count++;
            }
        }
        if (count == size +1 ){
            name = context.getString(R.string.remis);
        }
        else if (count > 1){
            name += " " + context.getString(R.string.win);
        }
        else {
            name += " " + context.getString(R.string.wins);
        }

        return name;
    }
    public static void updateWinsInDatabase(String name, Context context){
        Log.d("Utilyty", "dodaje do bazy danych " + name);
        Cursor cursor = context.getContentResolver().query(
                MyDatabaseContract.PeopleEntry.CONTENT_URI, new String[]
                        {MyDatabaseContract.PeopleEntry.COLUMN_WINS},
                MyDatabaseContract.PeopleEntry.COLUMN_NAME + " =?" , new String[] {name}
                , null);
        if (cursor.moveToFirst()){
            int win = cursor.getInt(cursor.getColumnIndex(MyDatabaseContract.PeopleEntry.COLUMN_WINS));
            win += 1;
            ContentValues values = new ContentValues();
            values.put(MyDatabaseContract.PeopleEntry.COLUMN_WINS, win);
            context.getContentResolver().update(MyDatabaseContract.PeopleEntry.CONTENT_URI, values,
                    MyDatabaseContract.PeopleEntry.COLUMN_NAME + " =?" , new String[] {name});
        }
        cursor.close();
    }

    public static void bonusFromYellow(List<Scores> scores){
        List<Pair<Integer, Integer>> pairList = new ArrayList<Pair<Integer, Integer>>();
        int size = scores.size();
        for (int i = 0; i < size; i++){
            pairList.add(new Pair<Integer, Integer>(i, scores.get(i).get(2)));
        }
        Collections.sort(pairList, new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> lhs, Pair<Integer, Integer> rhs) {
                return Integer.compare(lhs.second, rhs.second);
            }
        });

        for (int i = 0; i < size; i++){
            int bonus = (size - i - 1)*10;
            scores.get(pairList.get(i).first).setBonusFromYellow(bonus);
        }
    }
}
