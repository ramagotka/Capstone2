package example.com.fivetribesscoringsheet;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.RemoteViews;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

import static android.R.attr.name;
import static example.com.fivetribesscoringsheet.R.id.chart;
import static java.security.AccessController.getContext;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//      //  views.setTextViewText(R.id.appwidget_text, widgetText);
//
////        Cursor cursor =  getContext().getContentResolver().query(MyDatabaseContract.PeopleEntry.CONTENT_URI,
////                PEOPLE_COLUMNS, null, null,
////                MyDatabaseContract.PeopleEntry.COLUMN_NAME + " ASC");
////
//        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
////
////        while (cursor.moveToNext()){
////            String name = cursor.getString
////                    (cursor.getColumnIndex(MyDatabaseContract.PeopleEntry.COLUMN_NAME));
////            int wins = cursor.getInt(cursor.getColumnIndex(MyDatabaseContract.PeopleEntry.COLUMN_WINS));
////            if (wins != 0) {
////                entries.add(new PieEntry(wins, name));
////            }
////        }
////        cursor.close();
//
//        entries.add(new PieEntry(3, "Hania"));
//        entries.add(new PieEntry(2, "Przemek"));
//        entries.add(new PieEntry(5, "Marysia"));
//
//        PieDataSet dataSet = new PieDataSet(entries, "wins");
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
//
//        dataSet.setColors(colors);
//
//        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
//
//        PieChart chart = new PieChart(context);
//
//        chart.setData(data);
//
//        Description description = chart.getDescription();
//        description.setEnabled(false);
//
//        chart.getLegend().setEnabled(false);
//        chart.setEntryLabelColor(Color.BLACK);
//
//        chart.measure(View.MeasureSpec.makeMeasureSpec(500,View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(500,View.MeasureSpec.EXACTLY));
//        chart.layout(0, 0, chart.getMeasuredWidth(), chart.getMeasuredHeight());
//
//        Bitmap chartBitmap = chart.getChartBitmap();
//        views.setImageViewBitmap(R.id.image_view_widget, chartBitmap);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
       // for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            Intent intent = new Intent(context, StatisticActivity.class);

            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            PendingIntent pendingIntent =
                TaskStackBuilder.create(context)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(intent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.image_view_widget, pendingIntent);
            //updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
       // }

//            Intent clickIntentTemplate = new Intent(context, StatisticActivity.class);
//            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                    .addNextIntentWithParentStack(clickIntentTemplate)
//                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setPendingIntentTemplate(R.id.image_view_widget, clickPendingIntentTemplate);
            context.startService(new Intent(context, UpdateService.class));
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static class UpdateService extends Service{
        private static final String[] PEOPLE_COLUMNS = {
                MyDatabaseContract.PeopleEntry.TABLE_NAME + "." + MyDatabaseContract.PeopleEntry._ID,
                MyDatabaseContract.PeopleEntry.COLUMN_NAME,
                MyDatabaseContract.PeopleEntry.COLUMN_WINS
        };

        @Override
        public void onStart(Intent intent, int startId) {
            // Build the widget update for today
            RemoteViews updateViews = buildUpdate(this.getApplicationContext());

            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, NewAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

        public RemoteViews buildUpdate(Context context) {

            Cursor cursor =  context.getContentResolver().query(MyDatabaseContract.PeopleEntry.CONTENT_URI,
                PEOPLE_COLUMNS, null, null,
                MyDatabaseContract.PeopleEntry.COLUMN_NAME + " ASC");

            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        while (cursor.moveToNext()){
            String name = cursor.getString
                    (cursor.getColumnIndex(MyDatabaseContract.PeopleEntry.COLUMN_NAME));
            int wins = cursor.getInt(cursor.getColumnIndex(MyDatabaseContract.PeopleEntry.COLUMN_WINS));
            if (wins != 0) {
                entries.add(new PieEntry(wins, name));
            }
        }
        cursor.close();


            RemoteViews updateViews = null;
            updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            PieDataSet dataSet = new PieDataSet(entries, "wins");

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());

            PieChart chart = new PieChart(context);

            chart.setUsePercentValues(true);

            chart.setData(data);

            Description description = chart.getDescription();
            description.setEnabled(false);

            chart.getLegend().setEnabled(false);
            chart.setEntryLabelColor(Color.BLACK);

            chart.measure(View.MeasureSpec.makeMeasureSpec(500,View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(500,View.MeasureSpec.EXACTLY));
            chart.layout(0, 0, chart.getMeasuredWidth(), chart.getMeasuredHeight());

            Bitmap chartBitmap = chart.getChartBitmap();
            updateViews.setImageViewBitmap(R.id.image_view_widget, chartBitmap);

            // Instruct the widget manager to update the widget
            //appWidgetManager.updateAppWidget(appWidgetId, updateViews);


            return updateViews;
        }



        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

}

