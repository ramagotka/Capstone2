package example.com.fivetribesscoringsheet;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hania on 20.10.16.
 */

public class AdvancedRecyclerAdapter extends RecyclerView.Adapter<AdvancedRecyclerAdapter.MyRVViewHolder> {
    private final static String LOG_TAG = AdvancedRecyclerAdapter.class.getName();

    private AdvancedTurnActivity mContext;
    private Scores mScore;

    public static class MyRVViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        EditText editText;
        TextWatcher tw;
        Button button;

        MyRVViewHolder (View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.advanced_textview);
            editText = (EditText) view.findViewById(R.id.advanced_edittext);
            cardView = (CardView) view.findViewById(R.id.card_view_text_advanced);
            button = (Button) view.findViewById(R.id.button_advanced_recycler);
        }
    }

    public AdvancedRecyclerAdapter(AdvancedTurnActivity context, String name){
        mScore = new Scores();
        mContext = context;
        mScore.setName(name);
        mScore.initBool();
    }

    @Override
    public MyRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advanced_turn_text, parent, false);
        return new AdvancedRecyclerAdapter.MyRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRVViewHolder holder, int position) {

        holder.cardView.setVisibility(View.VISIBLE);
        holder.textView.setVisibility(View.INVISIBLE);
        holder.editText.setVisibility(View.VISIBLE);
        holder.button.setVisibility(View.INVISIBLE);
        if(holder.tw != null)
            holder.editText.removeTextChangedListener(holder.tw);
        holder.tw = null;
        holder.editText.setText("");
        holder.editText.setEnabled(true);
        holder.editText.setHint("");

        if (position % 2 == 0) {
            holder.editText.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            if (position / 2 == 0){
                holder.textView.setText(R.string.auction);
            }
            if (position / 2 == 1) {
                holder.textView.setText(R.string.chart_money);
            }
            if (position / 2 == 2) {
                holder.textView.setText(R.string.chart_yellow);
            }
            if (position / 2 == 3) {
                holder.textView.setText(R.string.chart_white);
            }
            if (position / 2 == 4) {
                holder.textView.setText(R.string.chart_djinn);
            }
            if (position / 2 == 5) {
                holder.textView.setText(R.string.chart_palm);
            }
            if (position / 2 == 6) {
                holder.textView.setText(R.string.chart_palace);
            }
            if (position / 2 == 7) {
                holder.textView.setText(R.string.chart_camels);
            }
            if (position / 2 == 8) {
                holder.textView.setText(R.string.chart_carts);
            }
            if (position / 2 == 9) {
                holder.textView.setText(R.string.total);
            }
            if (position/2 == 10) {
                holder.textView.setText(R.string.adv_kill_white);
            }
            if (position/2 == 11) {
                holder.textView.setText(R.string.adv_kill_yellow);
            }
            if (position/2 == 12) {
                holder.textView.setText(R.string.adv_add_palm);
            }
            if (position/2 == 13) {
                holder.textView.setText(R.string.adv_add_palace);
            }
        }
        else if (position < 19){
            final int j = (position-3) / 2 + 1;
           // Log.d(LOG_TAG, "j  = " + j);
            int ipkt = mScore.get(j);
            if (ipkt == 0){
                holder.editText.setText("");
            }
            else {
                holder.editText.setText("" + ipkt);
            }
            final AdvancedRecyclerAdapter me = this;


            holder.tw = new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int ipkt;
                    try {
                        ipkt = Integer.parseInt(s.toString());
                    } catch (Exception e) {
                        //Log.d(LOG_TAG, "blad  w nowym" + e);
                        ipkt = 0;
                    }

                    mScore.set(j, ipkt);

                    me.notifyItemChanged(17);

                }
            };
            holder.editText.addTextChangedListener(holder.tw);
        }
        else if (position == 19){
            holder.editText.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(String.valueOf(mScore.calRaundTotal()));
        }
        else {
            holder.editText.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.INVISIBLE);
            holder.button.setVisibility(View.VISIBLE);
            //holder.cardView.setVisibility(View.INVISIBLE);
            if (position == 21){
                holder.button.setText(R.string.kill_button);
                holder.button.setContentDescription(mContext.getString(R.string.kill_white));
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.onDataClick("white");
                    }
                });
            }
            else if (position == 23){
                holder.button.setText(R.string.kill_button);
                holder.button.setContentDescription(mContext.getString(R.string.kill_yellow));
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.onDataClick("yellow");
                    }
                });
            }
            else if (position == 25){
                holder.button.setText(R.string.add_button);
                holder.button.setContentDescription(mContext.getString(R.string.add_button));
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.onDataClick2("palm");
                    }
                });
            }
            else if (position == 27){
                holder.button.setText(R.string.add_button);
                holder.button.setContentDescription(mContext.getString(R.string.add_button));
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.onDataClick2("palace");
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return 28;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public Scores getmScores()
    {
        return mScore;
    }
}
