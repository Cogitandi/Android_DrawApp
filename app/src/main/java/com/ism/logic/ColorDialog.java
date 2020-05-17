package com.ism.logic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ism.thirdapp.MainActivity;
import com.ism.thirdapp.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public class ColorDialog extends AppCompatDialogFragment {

    // Listener which is use to make connection with main activity
    iNoticeDialog listener;
    // Picker - contains choosed color
    ColorPicker picker;

    // Public geter for picker
    public ColorPicker getPicker() {
        return picker;
    }

    @Override
    // create dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create builder for dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate layout from xml
        View v = inflater.inflate(R.layout.color_dialog, null);
        // Set inflated view as a content of dialog
        builder.setView(v);

        // Initialize components
        picker = (ColorPicker) v.findViewById(R.id.picker);
        SVBar svBar = (SVBar) v.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) v.findViewById(R.id.opacitybar);
        SaturationBar saturationBar = (SaturationBar) v.findViewById(R.id.saturationbar);
        ValueBar valueBar = (ValueBar) v.findViewById(R.id.valuebar);

        // Integrate color picker with adjusting bar
        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);
        picker.setOldCenterColor(MainActivity.paint.getColor());
        // Add save and cancel button
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send choosed color to the main activity
                listener.onDialogPositiveClick(ColorDialog.this);
            }
        })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });

        // return builder
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the DialogListener so we can send events to the host
            listener = (iNoticeDialog) getContext();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
