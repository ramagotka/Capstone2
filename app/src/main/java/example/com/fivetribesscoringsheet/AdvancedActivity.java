package example.com.fivetribesscoringsheet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AdvancedActivity extends AppCompatActivity {
    private static final String LOG_TAG = AdvancedActivity.class.getName();
    private ArrayList<Scores> mScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity= this;
        setContentView(R.layout.activity_advanced);

        final ArrayList<String> list;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            list = null;
        } else {
            list = extras.getStringArrayList("people");
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("list")){
            mScores = (ArrayList<Scores>) extras.get("list");
        }
        else {
            mScores = new ArrayList<Scores>(list.size());
            for (int i = 0; i < list.size(); i++){
                Scores score = new Scores();
                score.setName(list.get(i));
                score.initBool();
                score.setTurn(0);
                mScores.add(i, score);
            }
        }

        final int x;
        if (list.size() == 2){
            x = 2;
        }
        else {
            x = 1;
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.advanced_linear);

        for (int i = 0; i < list.size(); i++){
            final int ii = i;
            Button button = new Button(this);
            button.setText(list.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mScores.get(ii).getTurn() < x) {
                        Intent intent = new Intent(getApplicationContext(), AdvancedTurnActivity.class);
                        intent.putExtra("name", list.get(ii));
                        intent.putExtra("list", mScores);
                        startActivityForResult(intent, 1);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.button_advanced_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            linearLayout.addView(button);
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)button.getLayoutParams();
            ll.gravity = Gravity.CENTER;
            ll.height = ActionBar.LayoutParams.WRAP_CONTENT;
            ll.width = ActionBar.LayoutParams.WRAP_CONTENT;
            button.setLayoutParams(ll);
            //button.setLayoutParams();
        }

        Button button = new Button(this);
        button.setText(R.string.button_advanced);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mScores.size();
                String name  = Utility.whoFirst(mScores, getApplicationContext(),
                        getString(R.string.adv));

                Utility.whoFirstInEach(mScores);

                for (int i = 0; i < size; i++){
                    mScores.get(i).insertToDatabase(activity);
                }

                DialogFragment newFragment = WhoWinDialogFragment.newInstance(name);
                newFragment.show(getSupportFragmentManager(), "dialog");
            }
        });
        linearLayout.addView(button);
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams)button.getLayoutParams();



        ll.setMargins(getps(10), getps(30), getps(10), getps(10));
        ll.gravity = Gravity.CENTER;
        ll.height = ActionBar.LayoutParams.WRAP_CONTENT;
        ll.width = ActionBar.LayoutParams.WRAP_CONTENT;
        button.setLayoutParams(ll);
    }

    private int getps(int dp) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            int count = 0;
            String name = "";
            int total = 0;
            int size = mScores.size();
            int x;
            if (size == 2){
                x = 2;
            }
            else {
                x = 1;
            }

            mScores = (ArrayList<Scores>) data.getExtras().get("list");
            for (int i = 0; i < size; i++){
                if (mScores.get(i).getTurn() == x){
                    count++;
                }
                else {
                    break;
                }
            }
            if (count == size){
                Utility.bonusFromYellow(mScores);
                for (int i = 0; i < mScores.size(); i++){
                    mScores.get(i).setTurn(0);
                    mScores.get(i).resetRundTotal();
                    if (mScores.get(i).getRundTotal() > total){
                        total = mScores.get(i).getRundTotal();
                        name = mScores.get(i).getName();
                    }
                }
                DialogFragment newFragment = WhoIsBestDialogFragment.newInstance(name);
                newFragment.show(getSupportFragmentManager(),
                        name);
            }
        }
        else {
            //Log.d(LOG_TAG, "Problem z pobieraniem danych zwracanych");
        }
    }
}
