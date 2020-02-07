package com.mycompany.gotit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieFragment extends Fragment {
    private static final String TAG = "MovieFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle onSaveInstanceState){
        Log.d(TAG, "create view");
        View v = inflater.inflate(R.layout.fragment_movie, parent, false);
        return v;
    }
}
