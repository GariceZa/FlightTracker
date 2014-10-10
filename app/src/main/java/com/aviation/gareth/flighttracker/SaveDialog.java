package com.aviation.gareth.flighttracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Gareth on 30/09/2014.
 */
public class SaveDialog extends DialogFragment {

    /*
       allows a new instance of the fragment to be created
       and accepts arguments to be displayed
    */
    static SaveDialog newInstance(){
        SaveDialog save = new SaveDialog();

        return save;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
            creates a dialog that allows input from the user to input flight info.
            button clicks call doSave/doNotSave methods in mapsActivity
        */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("Describe the flight");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.save_dialog_layout, null);
        final EditText etDesc = (EditText) inflate.findViewById(R.id.etFlightDescription);

        builder.setView(inflate)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         ((MapsActivity) getActivity()).doSave(etDesc.getText().toString());
                    }
                })
                .setNegativeButton("Dont Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((MapsActivity) getActivity()).doNotSave();
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
