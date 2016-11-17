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
public class KilledDialogFragment extends DialogFragment {

    private OnCompleteListener mListener;

    public static interface OnCompleteListener {
        public abstract void onComplete(String name, int count, String color);
    }

    public static KilledDialogFragment newInstance(String[] list, String color, int[] counts){
        Bundle args = new Bundle();
        args.putStringArray("list", list);
        args.putString("color", color);
        args.putIntArray("counts", counts);

        KilledDialogFragment fragment = new KilledDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final String[] names = getArguments().getStringArray("list");
        final String color = getArguments().getString("color");
        final int[] counts = getArguments().getIntArray("counts");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setSingleChoiceItems(names, 0, null)
                .setView(inflater.inflate(R.layout.fragment_killed_dialog, null))
                .setTitle("Kill " + color)
                .setPositiveButton(R.string.kill_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListView lw = ((AlertDialog)dialog).getListView();
                                String name = names[lw.getCheckedItemPosition()];

                                EditText editText = (EditText) getDialog()
                                        .findViewById(R.id.kill_dialog_edittext);
                                String string = String.valueOf(editText.getText());
                                int count;
                                if (!string.equals("")) {
                                    count = Integer.parseInt(string);
                                }
                                else {
                                    count = 0;
                                }


                                if (count > counts[lw.getCheckedItemCount()]){
                                    Toast.makeText(getContext(), name + R.string.kill_error_toast,
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    sendData(name, count, color);
                                }
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
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    private void sendData(String name, int count, String color){
        this.mListener.onComplete(name, count, color);
    }
}
