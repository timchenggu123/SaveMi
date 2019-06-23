package me.timgu.enghack2019;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class NewAllergyItemDialogueFragment extends DialogFragment {

    public interface NewAllergyItemDialogueListener {

        public void onNewAllergyItemDialoguePositiveClick(DialogFragment dialog, String msg) throws IOException;
        public void onNewAllergyItemDialogueNegativeClick(DialogFragment dialog);
    }

    NewAllergyItemDialogueListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NewAllergyItemDialogueListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View dialogView = inflater.inflate(R.layout.dialogue_new_allergy_item,null);

        builder.setView(dialogView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText newName = dialogView.findViewById(R.id.flashcard_dialog_number);
                        String msg = newName.getText().toString();
                        try {
                            listener.onNewAllergyItemDialoguePositiveClick(NewAllergyItemDialogueFragment.this,msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNewAllergyItemDialogueNegativeClick(NewAllergyItemDialogueFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
