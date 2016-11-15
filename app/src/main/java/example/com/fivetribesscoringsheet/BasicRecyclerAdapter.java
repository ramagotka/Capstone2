package example.com.fivetribesscoringsheet;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hania on 23.09.16.
 */
public class BasicRecyclerAdapter extends RecyclerView.Adapter<BasicRecyclerAdapter.MyRVViewHolder> {
    private final static String LOG_TAG = BasicRecyclerAdapter.class.getName();

    private ArrayList<String> mPeople;
    private List<Scores> pScores;
    private int mColumns;
    private Activity context;

    public static class MyRVViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        EditText editText;
        TextWatcher tw;

        MyRVViewHolder (View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.basic_textview);
            editText = (EditText) view.findViewById(R.id.basic_edittext);
            cardView = (CardView) view.findViewById(R.id.card_view_text);
        }
    }

    public BasicRecyclerAdapter(ArrayList<String> people, Activity context){
        mPeople = people;
        pScores = new ArrayList<Scores>(mPeople.size());
        mColumns = people.size() + 1;
        for (int i = 0; i < mPeople.size(); i++){
            Scores score = new Scores();
            score.setName(mPeople.get(i));
            score.initBool();
            pScores.add(i, score);
           // Log.d(LOG_TAG, "name " + mPeople.get(i));
        }
        this.context = context;
    }

    @Override
    public MyRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_text, parent, false);
        MyRVViewHolder myRVViewHolder = new MyRVViewHolder(view);
        return myRVViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyRVViewHolder holder, final int position) {

        //int columns = mPeople.size() + 1;
        holder.cardView.setVisibility(View.VISIBLE);
        holder.textView.setVisibility(View.INVISIBLE);
        holder.editText.setVisibility(View.VISIBLE);
        if(holder.tw != null)
            holder.editText.removeTextChangedListener(holder.tw);
        holder.tw = null;
        holder.editText.setText("");
        holder.editText.setEnabled(true);
        holder.editText.setHint("");


        if (position == 0) {
            holder.cardView.setVisibility(View.INVISIBLE);
        } else if (position < mColumns) {
            holder.editText.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(mPeople.get(position - 1));
        } else if (position % mColumns == 0) {
            holder.editText.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            if (position / mColumns == 1) {
                holder.textView.setText("money");
            }
            if (position / mColumns == 2) {
                holder.textView.setText("yellow");
            }
            if (position / mColumns == 3) {
                holder.textView.setText("white");
            }
            if (position / mColumns == 4) {
                holder.textView.setText("djinn");
            }
            if (position / mColumns == 5) {
                holder.textView.setText("palms");
            }
            if (position / mColumns == 6) {
                holder.textView.setText("palace");
            }
            if (position / mColumns == 7) {
                holder.textView.setText("camels");
            }
            if (position / mColumns == 8) {
                holder.textView.setText("cards");
            }
            if (position / mColumns == 9) {
                holder.textView.setText("total");
            }
        }
        else if (position/mColumns == 9){
            holder.editText.setVisibility(View.INVISIBLE);
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(String.valueOf(pScores.get(position % mColumns - 1).calTotal()));
        }
        else {
            final int i = position % mColumns - 1;
            final int j = position / mColumns;
            int ipkt = pScores.get(i).get(j);
            if (ipkt == 0){
                holder.editText.setText("");
            }
            else {
                holder.editText.setText("" + ipkt);
            }
            final BasicRecyclerAdapter me = this;


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
                  //  Log.d(LOG_TAG, "HAHA " + s.toString());
                    try {
                        ipkt = Integer.parseInt(s.toString());
                    } catch (Exception e) {
                     //   Log.d(LOG_TAG, "blad  w nowym" + e);
                        ipkt = 0;
                    }

                    pScores.get(i).set(j, ipkt);

                    me.notifyItemChanged(9 * mColumns + i + 1);

                }
            };
            holder.editText.addTextChangedListener(holder.tw);
        }
    }

    @Override
    public int getItemCount() {
        return (mPeople.size()+1)*10;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public List<Scores> getpScores(){
        return pScores;
    }
}
