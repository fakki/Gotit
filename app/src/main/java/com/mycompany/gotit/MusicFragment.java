package com.mycompany.gotit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle onSaveInstanceState){
        View v = inflater.inflate(R.layout.fragment_music, parent, false);
        return v;
    }
}
