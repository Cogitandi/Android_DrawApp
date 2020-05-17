package com.ism.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ism.thirdapp.MainActivity;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    // Indicate whether drawing thread is working or not
    private boolean isThreadWorking = false;
    // SurfaceHolder which control and monitor surface
    private SurfaceHolder mSurfaceHolder;
    // Lock for synchronized operation on surface
    private Object mLock = new Object();
    // Thread which draw in background
    private Thread mThread;
    // Tag
    private final String TAG = "MySurfaceView KLASA";

    private Bitmap receivedBitmap;
    // Main bitmap which is displayed
    private Bitmap mBitmap = null;
    // Canva used for drawing on main (above) bitmap
    private Canvas mCanvas = null;

    private Path path = new Path();

    // static kind of actions
    public static final int normal = 0;
    public static final int circle = 1;
    public static final int fillCircle = 2;
    private static int mode = 0;


    //private Paint paint = new Paint();
    private Paint paint = MainActivity.paint;
    private Paint dotPaint = MainActivity.dotPaint;

    public MySurfaceView(Context context, AttributeSet attrs) {
        // Constructor from super class
        super(context, attrs);
        // Get underlying surface
        mSurfaceHolder = getHolder();
        // Add callback to the surface - information about changes on surface
        mSurfaceHolder.addCallback(this);
    }

    // set mode of action
    public static void setMode(int mode) {
        MySurfaceView.mode = mode;
    }

    // run new thread
    public void startDrawing() {
        isThreadWorking = true;
        mThread = new Thread(this);
        mThread.start();
    }

    // stop thread
    public void stopDrawing() {
        isThreadWorking = false;
    }

    // clean surface - button
    public void cleanSurface() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawARGB(255, 255, 255, 255);
        path.reset();
    }

    // set vibile bitmap
    public void setBitmap(Bitmap bitmap) {
        this.receivedBitmap = bitmap;
    }

    // return used bitmap
    public Bitmap getBitmap() {
        return this.mBitmap;
    }
    // return used paint
    public Paint getPaint() {
        return paint;
    }
    // return dot paint
    public Paint getDotPaint() {
        return dotPaint;
    }

    @Override
    // action after touch
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        // only one thread to surface
        synchronized (mLock) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // touch screen
                    if (mode == 0) {
                        mCanvas.drawPoint(x, y, dotPaint);
                        path = new Path();
                        path.moveTo(x, y);
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    // move on screen
                    if (mode == normal) {
                        path.lineTo(x, y);
                        mCanvas.drawPath(path, paint);
                    }
                    if (mode == circle) {
                        mCanvas.drawCircle(x, y, 30, paint);
                    }

                    if (mode == fillCircle) {
                        mCanvas.drawCircle(x, y, 30, paint);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // touch up
                    if (mode == 0) {
                        mCanvas.drawPoint(x, y, dotPaint);
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    // create bitmap or set received
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mCanvas == null) {
            if (receivedBitmap == null) {
                // if not received screen create new bitmap
                mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);
                mCanvas.drawARGB(255, 255, 255, 255);
            } else {
                // set received bitmap
                int currentHeigh = getHeight();
                int currentWidth = getWidth();
                mBitmap = Bitmap.createScaledBitmap(receivedBitmap, currentWidth, currentHeigh, false);
                mCanvas = new Canvas(mBitmap);
                receivedBitmap = null;
            }


        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void run() {
        // run new thread which refresh bitmap
        while (isThreadWorking) {
            Canvas mCanvas = null;
            try {
                synchronized (mSurfaceHolder) {
                    if (!mSurfaceHolder.getSurface().isValid()) continue;
                    // get canva
                    mCanvas = mSurfaceHolder.lockCanvas(null);
                    synchronized (mLock) {
                        if (isThreadWorking) {
                            // set bitmap to canva
                            mCanvas.drawBitmap(mBitmap, 0, 0, null);
                        }
                    }
                }
            } finally {
                if (mCanvas != null) {
                    // refresh visible bitmap
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
            try {
                Thread.sleep(1000 / 25);
            } catch (InterruptedException e) {
            }
        }
    }

}
