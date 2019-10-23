package com.jljim.cowlogs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           HomeFragment.java
 * File purpose:        Fragment to the buttons with different breeds
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
