package com.namso.pimark;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kamal on 12/09/2014.
 */
public class MyPiFragment extends Fragment {

    public MyPiFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_pi, container, false);
        return rootView;
    }
}