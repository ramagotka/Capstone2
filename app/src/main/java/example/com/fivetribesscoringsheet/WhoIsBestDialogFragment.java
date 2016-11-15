package example.com.fivetribesscoringsheet;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

public class WhoIsBestDialogFragment extends DialogFragment {

    public static WhoIsBestDialogFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);

        WhoIsBestDialogFragment fragment = new WhoIsBestDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        String name = getArguments().getString("name");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle(name + " is the best this turn")
                .setPositiveButton(R.string.ok_button,
                        null);
        return builder.create();
    }
}
