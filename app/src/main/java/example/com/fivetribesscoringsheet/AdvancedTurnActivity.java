package example.com.fivetribesscoringsheet;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.focusable;
import static android.R.attr.name;
import static android.R.id.list;

public class AdvancedTurnActivity extends AppCompatActivity implements OnDataClickListener,
        KilledDialogFragment.OnCompleteListener, OnDataClickListener2,
        AddSomeonePDialogFragment.OnCompleteListener {
    private final static String LOG_TAG = AdvancedTurnActivity.class.getName();

    private ArrayList<Scores> mScores;
    private AdvancedRecyclerAdapter mAdvancedRecyclerAdapter;
    private String mName;
    private boolean addP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_turn);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            mName = "";
        } else {
            mName = extras.getString("name");
            mScores = (ArrayList<Scores>) extras.get("list");
        }

        setTitle(mName);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_advanced);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(null);

        mAdvancedRecyclerAdapter = new AdvancedRecyclerAdapter(this, mName);

        recyclerView.setAdapter(mAdvancedRecyclerAdapter);

        addP = false;
        //Log.d(LOG_TAG,"tutaj");

    }

    public void onClickFinishTurn(View view){
        Scores score = mAdvancedRecyclerAdapter.getmScores();
        upDatemScores(score);
        Intent intent= new Intent();
        intent.putExtra("list", mScores);
        setResult(AdvancedActivity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDataClick(String string) {
        String[] list = new String [mScores.size()-1];
        int[] listCount = new int[mScores.size()];
        int count = 0;
        for (int i = 0; i < mScores.size(); i++){
            if (!mScores.get(i).getName().equals(mName)) {
                list[count] = mScores.get(i).getName();
                count++;
            }
            if (string.equals("white")){
                listCount[i] = mScores.get(i).get(3);
            }
            else {
                listCount[i] = mScores.get(i).get(2);
            }
        }
        DialogFragment newFragment = KilledDialogFragment.newInstance(list, string, listCount);
        newFragment.show(getSupportFragmentManager(),
                "white");
    }

    @Override
    public void onComplete(String name, int count, String color) {
        //Log.d(LOG_TAG, "tutaj stare onC");
        int number = 0;
        for (int i = 0; i < mScores.size(); i++){
            if (name.equals(mScores.get(i).getName())){
                number = i;
                break;
            }
        }
        if (color.equals("white")){
            mScores.get(number).set(3, count);
            mScores.get(number).updateRundTotal(-count*2);
        }
        else {
            mScores.get(number).set(2, count);
            mScores.get(number).updateRundTotal(-count);
        }
    }

    private void upDatemScores(Scores score){
        int number = 0;
        for (int i = 0; i < mScores.size(); i++){
            if (score.getName().equals(mScores.get(i).getName())){
                number = i;
                break;
            }
        }
        mScores.get(number).updateRundTotal(score.calRaundTotal());
        mScores.get(number).set(1, mScores.get(number).get(1) + score.get(1));
        mScores.get(number).set(2, mScores.get(number).get(2) + score.get(2));
        mScores.get(number).set(3, mScores.get(number).get(3) + score.get(3));
        mScores.get(number).set(4, mScores.get(number).get(4) + score.get(4));
        mScores.get(number).set(5, mScores.get(number).get(5) + score.get(5));
        mScores.get(number).set(6, mScores.get(number).get(6) + score.get(6));
        mScores.get(number).set(7, mScores.get(number).get(7) + score.get(7));
        mScores.get(number).set(8, mScores.get(number).get(8) + score.get(8));
        mScores.get(number).setTurn(mScores.get(number).getTurn() + 1);
    }

    @Override
    public void onDataClick2(String string) {
        if (addP) {
            Toast.makeText(getApplicationContext(), R.string.palm_error,
                    Toast.LENGTH_LONG).show();
            return;
        }
        String[] list = new String [mScores.size()-1];
        int count = 0;
        for (int i = 0; i < mScores.size(); i++) {
            if (!mScores.get(i).getName().equals(mName)) {
                list[count] = mScores.get(i).getName();
                count++;
            }
        }
        DialogFragment newFragment = AddSomeonePDialogFragment.newInstance(list, string);
        newFragment.show(getSupportFragmentManager(),
                "");
    }

    @Override
    public void onComplete2(String name, int count, String color) {
        addP = true;
        int number = 0;
        for (int i = 0; i < mScores.size(); i++){
            if (name.equals(mScores.get(i).getName())){
                number = i;
                break;
            }
        }
        if (color.equals("palm")){
            mScores.get(number).set(5, count);
        }
        else {
            mScores.get(number).set(6, count);
        }
        mScores.get(number).updateRundTotal(count);
    }
}

interface OnDataClickListener {
    void onDataClick(String string);
}

interface OnDataClickListener2 {
    void onDataClick2(String string);
}