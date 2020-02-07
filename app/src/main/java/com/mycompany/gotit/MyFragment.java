package com.mycompany.gotit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle onSaveInstanceState){
        View v = inflater.inflate(R.layout.fragment_my, parent, false);
        return v;
    }
}
