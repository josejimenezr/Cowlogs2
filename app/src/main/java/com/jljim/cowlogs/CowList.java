package com.jljim.cowlogs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           CowLis.java
 * File purpose:        Fragment to display a list of an specific breed.
 * A simple {@link Fragment} subclass.
 */
public class CowList extends ListFragment {

    //Array to show in the fragment
    ArrayList<String> stringCowList = new ArrayList<>();

    Button returnButton;

    public CowList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cow_list, container, false);

        // String with the breed in capital letters
        String breedButton = MainActivity.pageNames[CowFragment.cow].toUpperCase();

        // Get the return button
        returnButton = (Button) view.findViewById(R.id.returnBreedButton);

        // Add the text to the button
        returnButton.setText("RETURN TO "+breedButton);

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Iterate over all element in the array to display and select the ones in the breed
        for ( CowLogs cowLog : MainActivity.cowLogsArrayList) {

            // Use the variable in CowFragment to compare the breed
            if (MainActivity.pageNames[CowFragment.cow].equals(cowLog.getBreed())){
                stringCowList.add(cowLog.toString()); // Add in the array
            }

        }

        // Create a String array
        String[] cowListString = stringCowList.toArray(new String[stringCowList.size()]);

        // Set the list in the fragment
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cowListString));

        // clean the array list
        stringCowList.clear();
    }

}
