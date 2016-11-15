package example.com.fivetribesscoringsheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by hania on 10.11.16.
 */

public class AddSomeonePDialogFragment extends DialogFragment {

    private AddSomeonePDialogFragment.OnCompleteListener mListener;

    public static interface OnCompleteListener {
        public abstract void onComplete2(String name, int count, String color);
    }

    public static AddSomeonePDialogFragment newInstance(String[] list, String what){
        Bundle args = new Bundle();
        args.putStringArray("list", list);
        args.putString("what", what);

        AddSomeonePDialogFragment fragment = new AddSomeonePDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] names = getArguments().getStringArray("list");
        final String what = getArguments().getString("what");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setSingleChoiceItems(names, 0, null)
                .setTitle("Add " + what)
                .setPositiveButton(R.string.add_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListView lw = ((AlertDialog)dialog).getListView();
                                String name = names[lw.getCheckedItemPosition()];
                                sendData(name, 1, what);
                            }
                        }).setNegativeButton(R.string.add_new_player_negative_button,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (AddSomeonePDialogFragment.OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    private void sendData(String name, int count, String color){
        this.mListener.onComplete2(name, count, color);
    }
}
