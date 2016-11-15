package example.com.fivetribesscoringsheet;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

public class PieChartFragment extends Fragment {

    private static final String[] PEOPLE_COLUMNS = {
            MyDatabaseContract.PeopleEntry.TABLE_NAME + "." + MyDatabaseContract.PeopleEntry._ID,
            MyDatabaseContract.PeopleEntry.COLUMN_NAME,
            MyDatabaseContract.PeopleEntry.COLUMN_WINS
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        PieChart chart = (PieChart) view.findViewById(R.id.chart);
        TextView noStats = (TextView) view.findViewById(R.id.textview_no_stats_pie);

        chart.setUsePercentValues(true);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setRotationAngle(0);

        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        Cursor cursor =  view.getContext().getContentResolver().query(MyDatabaseContract.PeopleEntry.CONTENT_URI,
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


        if (entries.size() > 0) {

            PieDataSet dataSet = new PieDataSet(entries, "wins");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

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
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);

            chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

            // entry label styling
            chart.setEntryLabelColor(Color.BLACK);
            //     chart.setEntryLabelTypeface(mTfRegular);
            chart.setEntryLabelTextSize(12f);

            chart.setData(data);

            chart.highlightValues(null);

            Description description = chart.getDescription();
            description.setEnabled(false);

            chart.getLegend().setEnabled(false);
            chart.invalidate();
        }
        else {
            noStats.setVisibility(View.VISIBLE);
            chart.setVisibility(View.GONE);
        }


        return view;

    }

}
