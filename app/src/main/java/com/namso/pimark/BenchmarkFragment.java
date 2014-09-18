package com.namso.pimark;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namso.smartspecs.SpecSheet;


/**
 * Created by Kamal on 12/09/2014.
 */
public class BenchmarkFragment extends Fragment {

    public BenchmarkFragment(){}
    View rootView;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_benchmark, container, false);

       return rootView;
    }
}
