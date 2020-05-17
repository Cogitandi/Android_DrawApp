package com.ism.thirdapp;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ism.logic.ColorDialog;
import com.ism.logic.MySurfaceView;
import com.ism.logic.PaintbrushSettings;
import com.ism.logic.iNoticeDialog;

public class MainActivity extends AppCompatActivity implements iNoticeDialog {

    // Declarate variables
    MySurfaceView mySurfaceView;
    Button paintBtn;
    Button colorBtn;
    Button cleanBtn;
    public static Paint paint = new Paint();
    public static Paint dotPaint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        getSupportActionBar().setTitle("Namaluj swój wymarzony świat");
            if (savedInstanceState != null) {
                // get bitmap after rotate
            mySurfaceView.setBitmap((Bitmap) savedInstanceState.getParcelable("bitmap"));

        }
    }


    @Override

    protected void onSaveInstanceState(Bundle outState) {
        // Save bit map ( rotate screen )
        outState.putParcelable("bitmap", mySurfaceView.getBitmap());
        super.onSaveInstanceState(outState);
    }

    // Initialize variables
    private void initialize() {
        // initialize
        mySurfaceView = findViewById(R.id.powierzchnia_rysunku);
        paintBtn = findViewById(R.id.main_paint);
        colorBtn = findViewById(R.id.main_color);
        cleanBtn = findViewById(R.id.main_clean);
        // on click actions
        clicks();
        // style of paint
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);



    }

    // On click actions
    private void clicks() {
        paintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog for choose paint
                DialogFragment newFragment = new PaintbrushSettings();
                newFragment.show(getSupportFragmentManager(), "paint");

            }
        });
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog for choose color
                DialogFragment newFragment = new ColorDialog();
                newFragment.show(getSupportFragmentManager(), "color");

            }
        });
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySurfaceView.cleanSurface();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start draw bitmap on screen
        mySurfaceView.startDrawing();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop showing bitmap
        mySurfaceView.stopDrawing();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if (dialog instanceof ColorDialog) {

            ColorDialog mDialog = (ColorDialog) dialog;
            int color = mDialog.getPicker().getColor();
            paint.setColor(color);
            dotPaint.setColor(color);
        }
        if (dialog instanceof PaintbrushSettings) {
            // Dialog for choose paintbrush
            PaintbrushSettings mDialog = (PaintbrushSettings) dialog;
            paint.setStyle(mDialog.getPaint().getStyle());
            paint.setStrokeWidth(mDialog.getPaint().getStrokeWidth());
            dotPaint.setStrokeWidth(4 * mDialog.getPaint().getStrokeWidth());
            dotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
