package example.com.fivetribesscoringsheet;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

import static android.R.attr.name;

public class BarChartFragment extends Fragment {
    private final static String LOG_TAG = BarChartFragment.class.getName();

    private static final String[] SCORE_COLUMNS = {
            MyDatabaseContract.ScoresEntry.TABLE_NAME + "." + MyDatabaseContract.ScoresEntry._ID,
            MyDatabaseContract.ScoresEntry.COLUMN_WIN,
            MyDatabaseContract.ScoresEntry.COLUMN_CAMEL,
            MyDatabaseContract.ScoresEntry.COLUMN_DJINN,
            MyDatabaseContract.ScoresEntry.COLUMN_MERCHANDISE,
            MyDatabaseContract.ScoresEntry.COLUMN_MONEY,
            MyDatabaseContract.ScoresEntry.COLUMN_OASIS,
            MyDatabaseContract.ScoresEntry.COLUMN_PALACE,
            MyDatabaseContract.ScoresEntry.COLUMN_WHITE,
            MyDatabaseContract.ScoresEntry.COLUMN_YELLOW
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        BarChart chart = (BarChart) view.findViewById(R.id.chart2);
        TextView noStats = (TextView) view.findViewById(R.id.textview_no_stats);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        final String[] description = new String[] {getString(R.string.chart_money),
                getString(R.string.chart_yellow), getString(R.string.chart_white),
                getString(R.string.chart_djinn), getString(R.string.chart_palm),
                getString(R.string.chart_palace), getString(R.string.chart_camels),
                getString(R.string.chart_carts)
        };

        IAxisValueFormatter formatter = new IAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return description[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        };
        xAxis.setValueFormatter(formatter);

        chart.getAxisLeft().setDrawGridLines(false);

        chart.animateY(2500);

        chart.getLegend().setEnabled(false);

        Cursor cursor =  view.getContext().getContentResolver().query(MyDatabaseContract.ScoresEntry.CONTENT_URI,
                SCORE_COLUMNS, MyDatabaseContract.ScoresEntry.COLUMN_WIN + " =?" , new String[] {"1"},
                null);

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        int money = 0;
        int yellow = 0;
        int white = 0;
        int djinn = 0;
        int palm = 0;
        int palace = 0;
        int camels = 0;
        int carts = 0;

        int count = 0;

        while (cursor.moveToNext()){
            count ++;
            money += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_MONEY));
            yellow += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_YELLOW));
            white += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_WHITE));
            djinn += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_DJINN));
            palm += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_OASIS));
            palace += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_PALACE));
            camels += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_CAMEL));
            carts += cursor.getInt
                    (cursor.getColumnIndex(MyDatabaseContract.ScoresEntry.COLUMN_MERCHANDISE));
        }
        if (count > 0) {
            entries.add(new BarEntry(0, (float) money, R.string.chart_money));
            entries.add(new BarEntry(1, (float) yellow, R.string.chart_yellow));
            entries.add(new BarEntry(2, (float) white, R.string.chart_white));
            entries.add(new BarEntry(3, (float) djinn, R.string.chart_djinn));
            entries.add(new BarEntry(4, (float) palm, R.string.chart_palm));
            entries.add(new BarEntry(5, (float) palace, R.string.chart_palace));
            entries.add(new BarEntry(6, (float) camels, R.string.chart_camels));
            entries.add(new BarEntry(7, (float) carts, R.string.chart_carts));
            cursor.close();

            BarDataSet dataSet = new BarDataSet(entries, "wins");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            BarData data = new BarData(dataSet);
            data.setBarWidth(0.9f);
            data.setValueTextSize(10f);
            chart.setData(data);

            chart.setFitBars(true);

            Description description1 = chart.getDescription();
            description1.setEnabled(false);

            chart.invalidate();
        }
        else {
            noStats.setVisibility(View.VISIBLE);
            chart.setVisibility(View.GONE);
        }

        return view;
    }
}
