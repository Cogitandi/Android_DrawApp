package com.ism.logic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ism.thirdapp.MainActivity;
import com.ism.thirdapp.R;

public class PaintbrushSettings extends AppCompatDialogFragment {

    AlertDialog.Builder builder;
    // Listener which is use to make connection with main activity
    iNoticeDialog listener;
    // Picker - contains choosed color
    Paint paint;
    ImageView minusIV;
    ImageView plusIV;
    TextView sizeTV;
    Spinner styleSPN;

    public Paint getPaint() {
        return paint;
    }

    @Override
    // create dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create builder for dialog
        builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate layout from xml
        View v = inflater.inflate(R.layout.paintbrush_dialog, null);
        // Set inflated view as a content of dialog
        builder.setView(v);

        // Initialize components
        minusIV = v.findViewById(R.id.paintBrush_minus);
        plusIV = v.findViewById(R.id.paintBrush_plus);
        sizeTV = v.findViewById(R.id.paintBrush_sizeTV);
        styleSPN = v.findViewById(R.id.paintBrush_style);
        paint = new Paint(MainActivity.paint);

        sizeTV.setText(paint.getStrokeWidth() + "");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.paint_styles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSPN.setAdapter(adapter);
        styleSPN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choosed = parent.getItemAtPosition(position).toString();
                if (choosed.equals("Bez wypełnienia")) {
                    paint.setStyle(Paint.Style.STROKE);
                    MySurfaceView.setMode(MySurfaceView.normal);
                }
                if (choosed.equals("Z wypełnieniem")) {
                    paint.setStyle(Paint.Style.FILL);
                    MySurfaceView.setMode(MySurfaceView.normal);
                }
                if (choosed.equals("Okrąg")) {
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                    MySurfaceView.setMode(MySurfaceView.circle);

                }
                if (choosed.equals("Koło")) {
                    paint.setStyle(Paint.Style.STROKE);
                    MySurfaceView.setMode(MySurfaceView.fillCircle);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        onClick();
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

    public void onClick() {
        minusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setStrokeWidth(paint.getStrokeWidth() - (float) 0.5);
                sizeTV.setText(paint.getStrokeWidth() + "");
            }
        });
        plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setStrokeWidth(paint.getStrokeWidth() + (float) 0.5);
                sizeTV.setText(paint.getStrokeWidth() + "");
            }
        });
        // Add save and cancel button
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send choosed color to the main activity
                listener.onDialogPositiveClick(PaintbrushSettings.this);
            }
        });
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
            }
        });


    }

}
