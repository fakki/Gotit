package com.mycompany.gotit;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MoveableOnTouchListener implements View.OnTouchListener {

    private static final String TAG = "MoveableOnTouchListener";

    private float dX,dY;
    private int lastAction;

    @Override
    @SuppressLint("NewApi")
    public boolean onTouch(View view, MotionEvent event){

        DisplayMetrics displaymetrics = view.getResources().getDisplayMetrics();
        int screenHeight = view.getBottom();
        int screenWidth = displaymetrics.widthPixels;

        float newX, newY;

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:

                    dX = view.getX() - event.getRawX();
                    dY = view.getY() - event.getRawY();
                    lastAction = MotionEvent.ACTION_DOWN;
                    Log.d(TAG, "down view get x "+ view.getX());
                    Log.d(TAG, "down view get y "+ view.getY());
                    Log.d(TAG, "down event get x "+ event.getX());
                    Log.d(TAG, "down event get y "+ event.getY());
                    Log.d(TAG, "down event get raw x "+ event.getRawX());
                    Log.d(TAG, "down event get raw y "+ event.getRawY());
                    break;

                case MotionEvent.ACTION_MOVE:

                    newX = event.getRawX() + dX;
                    newY = event.getRawY() + dY;

                    Log.d(TAG, "move view get x "+ view.getX());
                    Log.d(TAG, "move view get y "+ view.getY());
                    Log.d(TAG, "move event get x "+ event.getX());
                    Log.d(TAG, "move event get y "+ event.getY());
                    Log.d(TAG, "move event get raw x "+ event.getRawX());
                    Log.d(TAG, "move event get raw y "+ event.getRawY());
                    Log.d(TAG, "move dx "+ dX);
                    Log.d(TAG, "move dy "+ dY);

                    // check if the view out of screen
                    if ((newX <= 0 || newX >= screenWidth-view.getWidth()) || (newY <= 0 || newY >= screenHeight-view.getHeight()))
                    {
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;
                    }

                    view.setX(newX);
                    view.setY(newY);

                    lastAction = MotionEvent.ACTION_MOVE;

                    break;

                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "up view get x "+ view.getX());
                    Log.d(TAG, "up view get y "+ view.getY());
                    Log.d(TAG, "up event get x "+ event.getX());
                    Log.d(TAG, "up event get y "+ event.getY());
                    Log.d(TAG, "up event get raw x "+ event.getRawX());
                    Log.d(TAG, "up event get raw y "+ event.getRawY());
                    if (lastAction == MotionEvent.ACTION_DOWN)
                        view.callOnClick();
                    break;

                default:
                    return false;
            }
            return true;
        }
    }
