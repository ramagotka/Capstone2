package example.com.fivetribesscoringsheet;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.com.fivetribesscoringsheet.data.MyDatabaseContract;

public class BasicActivity extends AppCompatActivity {
    private final static String LOG_TAG = BasicActivity.class.getName();

    BasicRecyclerAdapter mBasicRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//
//        int a =  (displaymetrics.heightPixels*90)/100;



        ArrayList<String> list;
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                list = null;
            } else {
                list = extras.getStringArrayList("people");
            }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.getLayoutParams().height =a;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                list.size() + 1);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(null);

        mBasicRecyclerAdapter = new BasicRecyclerAdapter(list, this);

        recyclerView.setAdapter(mBasicRecyclerAdapter);

    }

    public void onClickFinishBasic(View view){

        List<Scores> scores = mBasicRecyclerAdapter.getpScores();

        int size = scores.size();
        String name  = Utility.whoFirst(scores, getApplicationContext(), getString(R.string.bas));

        Utility.whoFirstInEach(scores);

        for (int i = 0; i < size; i++){
            scores.get(i).insertToDatabase(this);
        }

        DialogFragment newFragment = WhoWinDialogFragment.newInstance(name);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

}
